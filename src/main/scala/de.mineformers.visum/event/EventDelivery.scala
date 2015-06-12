package de.mineformers.visum.event

import scala.collection.mutable.ArrayBuffer

/**
 * EventDelivery
 *
 * Base class for all specialised event publishing, including filters.
 *
 * @author PaleoCrafter
 */
class EventDelivery[E <: Event]
{
  private val filters = ArrayBuffer.empty[E => Unit]
  private val listeners = ArrayBuffer.empty[E => Unit]

  def fire(event: E) = {
    filters.foreach(_(event))
    if(!event.consumed)
      listeners.foreach(_(event))
  }

  def filter(filter: E => Unit) = filters += filter

  def +=(listener: E => Unit) = listeners += listener

  def -=(listener: E => Unit) = listeners -= listener
}
