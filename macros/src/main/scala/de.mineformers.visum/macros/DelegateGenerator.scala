package de.mineformers.visum.macros

import scala.reflect.macros.whitebox

/**
 * DelegateGenerator
 *
 * @author PaleoCrafter
 */
class DelegateGenerator[C <: whitebox.Context](val c: C)
                                              (delegateFieldSuffix: String = "delegate",
                                               delegateGetterName: String = "get",
                                               delegateSetterName: String = "set") {

  import c.universe._

  val delegateGetName = TermName(delegateGetterName)
  val delegateSetName = TermName(delegateSetterName)
  var defaultDelegateType: Boolean => Option[c.universe.Type] = b => None

  def computeType(valDef: ValDef): Type = {
    if (valDef.tpe != null) {
      valDef.tpe
    } else {
      val checkedDef = c.typecheck(valDef.duplicate, silent = true, withMacrosDisabled = true)
      val result = if (valDef.tpe == null) checkedDef.tpe else valDef.tpe

      if (result == NoType) {
        val symbol = checkedDef.symbol
        if (symbol != null)
          symbol.info
        else
          c.typecheck(valDef.rhs, silent = true, withMacrosDisabled = true).tpe.erasure
      } else {
        result
      }
    }
  }

  def generate(field: ValDef, instance: Boolean => Option[Tree], javaBean: Boolean) = {
    val name = field.name
    val decodedName = name.decodedName.toString
    val delegateName = TermName(decodedName + delegateFieldSuffix.capitalize)
    val getName = TermName(decodedName)
    val setName = TermName(decodedName + "_$eq")
    val beanGetName = TermName("get" + decodedName.capitalize)
    val beanSetName = TermName("set" + decodedName.capitalize)
    val fieldType = computeType(field)
    val Apply(Select(Apply(Select(New(newType), _), params), _), _) = c.macroApplication
    //    val instance =
    //      if (params.isEmpty) None
    //      else params.zipWithIndex find {
    //        case (AssignOrNamedArg(Ident(TermName("instance")), tree), _) => true
    //        case (tree: Tree, 0) => true
    //        case d => false
    //      } map {
    //        case (AssignOrNamedArg(Ident(TermName("instance")), tree), _) => tree
    //        case (tree: Tree, 0) => tree
    //      }
    //    val javaBean =
    //      if (params.isEmpty) false
    //      else params.zipWithIndex find {
    //        case (AssignOrNamedArg(Ident(TermName("javaBeanStyle")), Literal(Constant(b: Boolean))), _) => true
    //        case (Literal(Constant(b: Boolean)), 1) => true
    //        case _ => false
    //      } exists {
    //        case (AssignOrNamedArg(Ident(TermName("javaBeanStyle")), Literal(Constant(b: Boolean))), _) => b
    //        case (Literal(Constant(b: Boolean)), 1) => b
    //        case _ => false
    //      }
    val mutable = field.mods.hasFlag(Flag.MUTABLE)
    val delegate = analyzeDelegate(newType, instance(mutable), fieldType, field.rhs, mutable)
    delegate map {
      case (initializer, refinedFieldType) =>
        if (mutable)
          List(
            q"""
             val $delegateName = $initializer
           """,
            q"""
             def $getName: $refinedFieldType = $delegateName.get
           """,
            q"""
             def $setName(newValue: $refinedFieldType): Unit = $delegateName.$delegateSetName(newValue)
           """) ++ (
            if (javaBean)
              List(
                q"""
                  def $beanGetName: $refinedFieldType = $delegateName.$delegateGetName
                """,
                q"""
                  def $beanSetName(newValue: $refinedFieldType): Unit = $delegateName.$delegateSetName(newValue)
                """)
            else
              Nil)
        else
          List(
            q"""
             val $delegateName = $initializer
           """,
            q"""
             def $getName: $refinedFieldType = $delegateName.$delegateGetName
           """) ++ (
            if (javaBean)
              List(
                q"""
                  def $beanGetName: $refinedFieldType = $delegateName.$delegateGetName
                """)
            else
              Nil)
    } getOrElse Nil
  }

  def analyzeDelegate(newType: Tree, instance: Option[Tree],
                      fieldType: Type, initialValue: Tree, isMutable: Boolean) = {
    var refinedFieldType = fieldType

    def checkGetter(symbol: Symbol, t: Type): Boolean = {
      if (symbol == NoSymbol) {
        c.error(c.enclosingPosition, s"Var/Val delegate $t must have 'get' method!")
        return false
      }
      if (!symbol.alternatives.exists(_.isMethod)) {
        c.error(c.enclosingPosition, s"Delegate $t must have a 'get' *method* of type $refinedFieldType!")
        return false
      }
      val getters = symbol.alternatives.filter(_.isMethod).map(_.asMethod).filter(_.paramLists.isEmpty)
      if (getters.isEmpty) {
        c.error(c.enclosingPosition,
          s"""Delegate $t's getter must have no parameters at all!""".stripMargin)
        return false
      }
      if (refinedFieldType =:= typeOf[Null])
        refinedFieldType = getters.headOption.
          map(_.returnType.dealias.typeSymbol.asType.toTypeIn(t)).getOrElse(fieldType)
      if (!getters.exists(_.returnType.dealias.typeSymbol.asType.toTypeIn(t) <:< refinedFieldType)) {
        c.error(c.enclosingPosition,
          s"""Delegate $t's getter has insufficient type!""".stripMargin)
        return false
      }
      true
    }

    def checkSetter(symbol: Symbol, t: Type): Boolean = {
      if (!isMutable)
        return true
      if (symbol == NoSymbol) {
        c.error(c.enclosingPosition, s"Var delegate $t must have 'set' method!")
        return false
      }
      if (!symbol.alternatives.exists(_.isMethod)) {
        c.error(c.enclosingPosition, s"Delegate $t must have a 'set' *method*!")
        return false
      }
      val setters = symbol.alternatives.filter(_.isMethod).map(_.asMethod)
      if (!setters.exists(_.paramLists.length == 1)) {
        c.error(c.enclosingPosition, s"Delegate $t must have at least one single parameter list set method!")
        return false
      }
      if (!setters.filter(_.paramLists.length == 1).exists(_.paramLists.head.length == 1)) {
        c.error(c.enclosingPosition,
          s"Delegate $t's set method must have only one parameter of type $refinedFieldType!")
        return false
      }

      if (!setters.filter(m => m.paramLists.length == 1 && m.paramLists.head.length == 1).
        exists(_.paramLists.head.head.info.dealias.typeSymbol.asType.toTypeIn(t) <:< refinedFieldType)) {
        c.error(c.enclosingPosition, s"Delegate $t's set method has a parameter with insufficent type!")
        return false
      }
      true
    }

    def validate(t: Type) = {
      val getterValid = checkGetter(t.decl(delegateGetName), t)
      val setterValid = checkSetter(t.decl(delegateSetName), t)
      if (!getterValid || !setterValid)
        None
      else
        Some(t)
    }

    (if (instance.isEmpty)
      (newType, defaultDelegateType(isMutable)) match {
        case (AppliedTypeTree(_, delegateType :: Nil), _) =>
          delegateType match {
            case simple: Ident => validate(c.typecheck(tq"${simple.name.toTypeName}", c.TYPEmode).tpe)
            case a: AppliedTypeTree => validate(c.typecheck(tq"$a", c.TYPEmode).tpe)
          }
        case (_, t) if t.isDefined =>
          t
        case _ =>
          c.error(c.enclosingPosition, "Could not identify type of delegate")
          None
      }
    else
      instance flatMap {
        apply => validate(c.typecheck(apply, c.TERMmode).tpe)
      }) map (t => (instance.getOrElse(q"new $t($initialValue)"), refinedFieldType))
  }
}
