package de.mineformers.visum.event

import de.mineformers.visum.bean.Observable
import de.mineformers.visum.bean.value.ObservableValue

/**
 * InvalidationEvent
 *
 * Fired whenever an observable gets invalidated (e.g. changes its value)
 *
 * @author PaleoCrafter
 */
case class Invalidation(observable: Observable) extends Event

case class ValueChanged[A](observable: ObservableValue[A], oldValue: A, newValue: A) extends Event