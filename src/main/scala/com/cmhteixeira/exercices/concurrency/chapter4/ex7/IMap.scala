package com.cmhteixeira.exercices.concurrency.chapter4.ex7
import scala.concurrent.{Future, Promise}
import scala.collection.concurrent
import scala.collection.concurrent.TrieMap

class IMap[K, V] private (underlying: concurrent.Map[K, Promise[V]]) {
  // only works if the '.getOrElseUpdate' does not have the problem
  def update(k: K, v: V): Unit = {
    val newPromise = Promise[V]()
    newPromise success v
    val resultingPromise = underlying.getOrElseUpdate(k, newPromise)
    if (!newPromise.eq(resultingPromise)) resultingPromise success v
  }

  // only works if the '.getOrElseUpdate' does not have the problem
  def apply(k: K): Future[V] = underlying.getOrElseUpdate(k, Promise[V]()).future
}

object IMap {
  def empty[K, V]: IMap[K, V] = new IMap[K, V](TrieMap.empty[K, Promise[V]])
}
