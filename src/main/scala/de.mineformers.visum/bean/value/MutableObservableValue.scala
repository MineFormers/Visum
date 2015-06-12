package de.mineformers.visum.bean.value

/**
 * MutableValue
 *
 * @author PaleoCrafter
 */
trait MutableObservableValue[@specialized A] extends ObservableValue[A]
{
  def update(newValue: A) = value = newValue

  def value_=(newValue: A)
}
