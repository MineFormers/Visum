package de.mineformers.visum.bean.binding

import de.mineformers.visum.bean.value.ObservableValue

/**
 * MultiBinding
 *
 * @author PaleoCrafter
 */
class MultiBinding[@specialized A](val dependencies: Seq[ObservableValue[_]], compute: => A) extends Binding[A] {
  dependencies.foreach(_.onChange += {
    e =>
      invalidate()
  })

  override def computeValue: A = compute
}
