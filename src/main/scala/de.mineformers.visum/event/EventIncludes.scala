package de.mineformers.visum.event

/**
 * EventIncludes
 *
 * @author PaleoCrafter
 */
trait EventIncludes {
  def eventDelivery[E <: Event] = new EventDelivery[E]
}
