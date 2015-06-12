package de.mineformers.visum.bean.binding

import de.mineformers.visum.bean.Observable
import de.mineformers.visum.bean.value.ObservableValue

/**
 * BinaryBinding
 *
 * @author PaleoCrafter
 */
case class BinaryBinding[@specialized A, @specialized B](a: ObservableValue[A],
                                                         b: ObservableValue[A])
                                                        (op: (A, A) => B) extends Binding[B] {
  Seq(a, b).foreach(_.onChange += {
    e =>
      invalidate()
  })

  override def computeValue: B = op(a.value, b.value)

  override def dependencies: Seq[Observable] = Seq(a, b).distinct
}
