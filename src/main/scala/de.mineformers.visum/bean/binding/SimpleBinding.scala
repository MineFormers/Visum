package de.mineformers.visum.bean.binding

import de.mineformers.visum.bean.Observable
import de.mineformers.visum.bean.value.ObservableValue

/**
 * SimpleBinding
 *
 * @author PaleoCrafter
 */
case class SimpleBinding[@specialized A](bound: ObservableValue[A]) extends Binding[A] {
  bound.onChange += {
    e => invalidate()
  }

  override def computeValue: A = bound.value

  override def dependencies: Seq[Observable] = Seq(bound)
}
