package de.mineformers.visum.event

/**
 * Event
 *
 * @author PaleoCrafter
 */
trait Event
{
  private var _consumed = false

  def consumed = _consumed

  def consume() = _consumed = true
}
