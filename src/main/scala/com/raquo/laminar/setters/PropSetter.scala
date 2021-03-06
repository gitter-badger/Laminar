package com.raquo.laminar.setters

import com.raquo.domtypes.generic.Modifier
import com.raquo.domtypes.generic.keys.Prop
import com.raquo.laminar.DomApi
import com.raquo.laminar.experimental.airstream.core.Observable
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom

class PropSetter[V, DomV](
  val prop: Prop[V, DomV],
  $value: Observable[V]
) extends Modifier[ReactiveHtmlElement[dom.html.Element]] {

  override def apply(element: ReactiveHtmlElement[dom.html.Element]): Unit = {
    element.subscribe(
      $value,
      (value: V) => DomApi.htmlElementApi.setProperty(element, prop, value)
    )
  }
}
