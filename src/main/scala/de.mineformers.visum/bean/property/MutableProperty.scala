package de.mineformers.visum.bean.property

import de.mineformers.visum.bean.value.{ObservableValue, MutableObservableValue}

/**
 * Property
 *
 * @author PaleoCrafter
 */
trait MutableProperty[@specialized A] extends Property[A] with MutableObservableValue[A]
{
  def bind(value: ObservableValue[A]): Unit

  def unbind(value: ObservableValue[A]): Unit

  def bound: Boolean

  def bindBidirectional(property: MutableProperty[A]): Unit

  def unbindBidirectional(property: MutableProperty[A]): Unit

  def <==(value: ObservableValue[A]) = bind(value)

  def <=!(value: ObservableValue[A]) = unbind(value)

  def <===>(property: MutableProperty[A]) = bindBidirectional(property)

  def <=!=>(property: MutableProperty[A]) = unbindBidirectional(property)
}
