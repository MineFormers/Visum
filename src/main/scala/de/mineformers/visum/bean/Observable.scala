package de.mineformers.visum.bean

import de.mineformers.visum.Includes._
import de.mineformers.visum.event.Invalidation

/**
 * Observable
 *
 * @author PaleoCrafter
 */
trait Observable {
  val onInvalidate = eventDelivery[Invalidation]

  def fireInvalidationEvent(): Unit = {
    onInvalidate.fire(Invalidation(this))
  }
}
