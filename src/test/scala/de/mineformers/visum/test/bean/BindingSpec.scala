package de.mineformers.visum.test.bean

import de.mineformers.visum.bean.binding.SimpleBinding
import de.mineformers.visum.bean.value.{Value, MutableValue}
import org.scalatest.{FlatSpec, Matchers}
import de.mineformers.visum.Includes._

/**
 * BindingSpec
 *
 * @author PaleoCrafter
 */
class BindingSpec extends FlatSpec with Matchers {
  "A binding" should "have its underlying value's value when queried" in {
    val value = MutableValue(1)
    val a = Value(1)
    val b = Value(2)
    val binding = SimpleBinding(value)
    val test = value map (_ + 1)
    val sum = value + value

    binding.value should be(1)
    test.value should be(2)
    sum.value should be(2)

    value.value = 2

    binding.value should be(2)
    test.value should be(3)
    sum.value should be(4)
  }
}
