package de.mineformers.visum.macros

import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.reflect.macros.whitebox

/**
 * property
 *
 * @author PaleoCrafter
 */
@compileTimeOnly("Could not expand property, make sure that Macro Paradise is enabled")
class property extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro propertyMacro.impl
}

object propertyMacro {
  def impl(c: whitebox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._

    def reportInvalidAnnotationTarget() {
      c.error(c.enclosingPosition, "This annotation can only be used on vars or vals")
    }

    val inputs = annottees.map(_.tree).toList
    val outputs = inputs match {
      case (field: ValDef) :: Nil =>
        val generator = new DelegateGenerator[c.type](c)(
          delegateFieldSuffix = "property",
          delegateGetterName = "value",
          delegateSetterName = "value_$eq")
        generator.generate(field, {
          mutable =>
            val select = Select(
              Select(
                Select(
                  Select(
                    Select(
                      Ident(
                        TermName("de")),
                      TermName("mineformers")),
                    TermName("visum")),
                  TermName("bean")),
                TermName("property")),
              TermName("Simple" + (if (mutable) "Mutable" else "") + "Property"))
            val name = Literal(Constant(field.name.decodedName.toString))
            Some(Apply(select, List(This(c.enclosingClass.symbol), name, field.rhs)))
        }, javaBean = false)
      case _ => reportInvalidAnnotationTarget(); inputs
    }
    c.Expr[Any](Block(outputs, Literal(Constant(()))))
  }
}
