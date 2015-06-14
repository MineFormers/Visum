package de.mineformers.visum.bean.property

import de.mineformers.visum.bean.value.ObservableValue
import de.mineformers.visum.event.Invalidation

/**
 * AccessControlProperty
 *
 * @author PaleoCrafter
 */
case class ViewableProperty[@specialized A](bean: AnyRef, name: String, private var _value: A)
  extends MutableProperty[A] {
  private[this] var observable: Option[ObservableValue[A]] = None
  private lazy val observableListener = (e: Invalidation) => {
    invalidate()
  }
  
  lazy val view: Property[A] = new PropertyView

  override def value_=(newValue: A): Unit = observable match {
    case None =>
      if (_value != newValue) {
        _value = newValue
        invalidate()
      }
    case _ =>
      throw new RuntimeException("A bound value cannot be set")
  }

  override def value: A = {
    valid = true
    observable map (_.value) getOrElse _value
  }

  override def bound: Boolean = observable.isDefined

  override def bind(value: ObservableValue[A]): Unit = {
    if (observable.isEmpty || observable.get != value) {
      unbind()
      observable = Some(value)
      value.onInvalidate += observableListener
      invalidate()
    }
  }

  override def unbind(): Unit =
    observable match {
      case Some(obs) =>
        _value = obs.value
        obs.onInvalidate -= observableListener
        observable = None
      case _ =>
    }

  override def bindBidirectional(property: MutableProperty[A]): Unit = ???

  override def unbindBidirectional(property: MutableProperty[A]): Unit = ???

  private class PropertyView extends Property[A] {
    override def bean: AnyRef = ViewableProperty.this.bean

    override def name: String = ViewableProperty.this.name

    override def value: A = ViewableProperty.this.value
  }
}
