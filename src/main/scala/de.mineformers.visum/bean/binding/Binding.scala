package de.mineformers.visum.bean.binding

import de.mineformers.visum.bean.Observable
import de.mineformers.visum.bean.value.ObservableValue

/**
 * Binding
 *
 * @author PaleoCrafter
 */
trait Binding[@specialized A] extends ObservableValue[A] {
  private var _value: A = _
  private var _valid = false

  override def value: A = {
    if(!valid) {
      _value = computeValue
      _valid = true
    }

    _value
  }

  def valid: Boolean = _valid

  def invalidate(): Unit = {
    if(valid) {
      _valid = false
      fireChangeEvent()
    }
  }

  def computeValue: A

  def dependencies: Seq[Observable]

  def dispose(): Unit = ()
}
