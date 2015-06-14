package de.mineformers.visum.test.bean

import de.mineformers.visum.bean.binding.SimpleBinding
import de.mineformers.visum.bean.property.SimpleMutableProperty
import de.mineformers.visum.bean.value.{Value, MutableValue}
import de.mineformers.visum.macros.property
import org.scalatest.{FlatSpec, Matchers}
import de.mineformers.visum.Includes._

/**
 * BindingSpec
 *
 * @author PaleoCrafter
 */
class BindingSpec extends FlatSpec with Matchers {
  @property
  val test = ""

  "A binding" should "have its underlying value's value when queried" in {
    val property = SimpleMutableProperty(this, "test", 0)
    val value = MutableValue(1)
    property.bind(value)
    val a = Value(1)
    val b = Value(2)
    val binding = SimpleBinding(value)
    val test = value map (_ + 1)
    val sum = value + value

    property.value should be(1)
    binding.value should be(1)
    test.value should be(2)
    sum.value should be(2)

    value.value = 2

    property.value should be(2)
    binding.value should be(2)
    test.value should be(3)
    sum.value should be(4)

    val ex = intercept[RuntimeException] {
      property() = 3
    }
    ex.getMessage should be("A bound value cannot be set")
  }
}
