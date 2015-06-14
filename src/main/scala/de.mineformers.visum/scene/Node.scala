package de.mineformers.visum.scene

import de.mineformers.visum.bean.property.ViewableProperty

/**
 * Node
 *
 * @author PaleoCrafter
 */
trait Node {
  private val _parent = new ViewableProperty[Parent](this, "parent", null) {
    private var oldParent = value

    override def invalidated(): Unit = {
    }
  }

  lazy val parentProperty = _parent.view

  def parent = _parent()

  protected def parent_=(parent: Parent) = _parent() = parent
}
