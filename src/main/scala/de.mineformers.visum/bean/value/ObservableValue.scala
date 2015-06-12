package de.mineformers.visum.bean.value

import de.mineformers.visum.Includes._
import de.mineformers.visum.bean.Observable
import de.mineformers.visum.bean.binding.MultiBinding
import de.mineformers.visum.event.{Invalidation, ValueChanged}

/**
 * ObservableValue
 *
 * @author PaleoCrafter
 */
trait ObservableValue[@specialized A] extends Observable {
  val onChange = eventDelivery[ValueChanged[A]]
  private var currentValue: A = _

  def apply(): A = value

  def value: A

  def ===(that: ObservableValue[A]) = value == that.value

  def =!=(that: ObservableValue[A]) = value != that.value

  def map[B](f: A => B): ObservableValue[B] = new MultiBinding(Seq(this), f(value))

  def fireChangeEvent(): Unit = {
    onInvalidate.fire(Invalidation(this))
    val oldValue = currentValue
    val newValue = value
    onChange.fire(ValueChanged(this, oldValue, newValue))
    currentValue = newValue
  }
}
