package com.raquo.laminar.experimental.airstream.ownership

import scala.scalajs.js

/** Owner decides when to kill its possessions ([[Owned]]-s).
  * - Ownership is defined at creation of the possession (the [[Owned]] instance)
  * - Ownership is non-transferable
  * - There is no way to unkill a possession
  * - In other words: Owner can only own a possession once,
  *   and a possession can only ever be owned by its initial owner
  * - Owner can still be used after calling killPossessions, but the canonical
  *   use case is for the Owner to kill its possessions when the owner itself
  *   is discarded (e.g. a UI component is unmounted).
  */
trait Owner {

  /** Note: This is enforced to be a set outside the type system. #performance */
  protected[this] val possessions: js.Array[Owned] = js.Array()

  protected[this] def killPossessions(): Unit = {
    possessions.foreach(_.onKilledByOwner())
    possessions.clear()
  }

  /** This method will be called when this [[Owner]] has just started owning this resource.
    * You can override it to add custom behaviour.
    * Note: You can rely on this base method being empty.
    */
  protected[this] def onOwned(owned: Owned): Unit = ()

  /** Some possessions can be killed externally, e.g. `WriteBusSource.` */
  def onKilledExternally(owned: Owned): Unit = {
    val index = possessions.indexOf(owned)
    if (index != -1) {
      possessions.splice(index, deleteCount = 1)
    }
  }

  private[ownership] def own(owned: Owned): Unit = {
    possessions.push(owned)
    onOwned(owned)
  }
}
