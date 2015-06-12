package de.mineformers.visum.bean

import de.mineformers.visum.bean.binding.BinaryBinding
import de.mineformers.visum.bean.value.{ObservableValue, Value}

trait Ops[@specialized A] {
  this: ObservableValue[A] =>
}

trait BooleanOps extends Ops[Boolean] {
  this: ObservableValue[Boolean] =>

  def &&(that: ObservableValue[Boolean]) = BinaryBinding(this, that)(_ && _)

  def ||(that: ObservableValue[Boolean]) = BinaryBinding(this, that)(_ || _)

  def unary_!() = Value(!value)
}

trait ByteOps extends Ops[Byte] {
  this: ObservableValue[Byte] =>

  def +(that: ObservableValue[Byte]) = BinaryBinding(this, that)(_ + _)

  def -(that: ObservableValue[Byte]) = BinaryBinding(this, that)(_ - _)

  def *(that: ObservableValue[Byte]) = BinaryBinding(this, that)(_ * _)

  def /(that: ObservableValue[Byte]) = BinaryBinding(this, that)(_ / _)
}

trait ShortOps extends Ops[Short] {
  this: ObservableValue[Short] =>

  def +(that: ObservableValue[Short]) = BinaryBinding(this, that)(_ + _)

  def -(that: ObservableValue[Short]) = BinaryBinding(this, that)(_ - _)

  def *(that: ObservableValue[Short]) = BinaryBinding(this, that)(_ * _)

  def /(that: ObservableValue[Short]) = BinaryBinding(this, that)(_ / _)
}

trait IntOps extends Ops[Int] {
  this: ObservableValue[Int] =>

  def +(that: ObservableValue[Int]) = BinaryBinding(this, that)(_ + _)

  def -(that: ObservableValue[Int]) = BinaryBinding(this, that)(_ - _)

  def *(that: ObservableValue[Int]) = BinaryBinding(this, that)(_ * _)

  def /(that: ObservableValue[Int]) = BinaryBinding(this, that)(_ / _)
}

trait LongOps extends Ops[Long] {
  this: ObservableValue[Long] =>

  def +(that: ObservableValue[Long]) = BinaryBinding(this, that)(_ + _)

  def -(that: ObservableValue[Long]) = BinaryBinding(this, that)(_ - _)

  def *(that: ObservableValue[Long]) = BinaryBinding(this, that)(_ * _)

  def /(that: ObservableValue[Long]) = BinaryBinding(this, that)(_ / _)
}

trait FloatOps extends Ops[Float] {
  this: ObservableValue[Float] =>

  def +(that: ObservableValue[Float]) = BinaryBinding(this, that)(_ + _)

  def -(that: ObservableValue[Float]) = BinaryBinding(this, that)(_ - _)

  def *(that: ObservableValue[Float]) = BinaryBinding(this, that)(_ * _)

  def /(that: ObservableValue[Float]) = BinaryBinding(this, that)(_ / _)
}

trait DoubleOps extends Ops[Double] {
  this: ObservableValue[Double] =>

  def +(that: ObservableValue[Double]) = BinaryBinding(this, that)(_ + _)

  def -(that: ObservableValue[Double]) = BinaryBinding(this, that)(_ - _)

  def *(that: ObservableValue[Double]) = BinaryBinding(this, that)(_ * _)

  def /(that: ObservableValue[Double]) = BinaryBinding(this, that)(_ / _)
}