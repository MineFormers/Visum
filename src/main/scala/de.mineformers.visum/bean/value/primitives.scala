package de.mineformers.visum.bean.value

import de.mineformers.visum.bean._
import de.mineformers.visum.bean.binding.{Binding, SimpleBinding}

import scala.language.implicitConversions

/**
 * Value
 *
 * @author PaleoCrafter
 */
case class Value[@specialized A](value: A) extends ObservableValue[A]

case class MutableValue[@specialized A](private var _value: A) extends MutableObservableValue[A] {
  override def value: A = _value

  override def value_=(newValue: A): Unit = {
    _value = newValue
    fireChangeEvent()
  }
}

trait PrimitiveValueIncludes {
  implicit def value2binding[@specialized A](observable: ObservableValue[A]): Binding[A] =
    SimpleBinding(observable)

  implicit def boolean2ops(observable: ObservableValue[Boolean]): ObservableValue[Boolean] with BooleanOps =
    new SimpleBinding(observable) with BooleanOps

  implicit def byte2ops(observable: ObservableValue[Byte]): ObservableValue[Byte] with ByteOps =
    new SimpleBinding(observable) with ByteOps

  implicit def short2ops(observable: ObservableValue[Short]): ObservableValue[Short] with ShortOps =
    new SimpleBinding(observable) with ShortOps

  implicit def int2ops(observable: ObservableValue[Int]): ObservableValue[Int] with IntOps =
    new SimpleBinding(observable) with IntOps

  implicit def long2ops(observable: ObservableValue[Long]): ObservableValue[Long] with LongOps =
    new SimpleBinding(observable) with LongOps

  implicit def float2ops(observable: ObservableValue[Float]): ObservableValue[Float] with FloatOps =
    new SimpleBinding(observable) with FloatOps

  implicit def double2ops(observable: ObservableValue[Double]): ObservableValue[Double] with DoubleOps =
    new SimpleBinding(observable) with DoubleOps
}