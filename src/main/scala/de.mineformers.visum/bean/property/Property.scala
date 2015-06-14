package de.mineformers.visum.bean.property

import de.mineformers.visum.bean.value.ObservableValue

/**
 * Property
 *
 * @author PaleoCrafter
 */
trait Property[@specialized A] extends ObservableValue[A] {
  def bean: AnyRef

  def name: String

  override final def toString: String = s"Property($name, $value)"
}
