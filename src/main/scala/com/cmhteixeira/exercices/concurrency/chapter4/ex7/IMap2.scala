package com.cmhteixeira.exercices.concurrency.chapter4.ex7
import scala.collection.concurrent
import scala.collection.concurrent.TrieMap
import scala.concurrent.{Future, Promise}

class IMap2[K, V] private (underlying: concurrent.Map[K, Promise[V]]) {
  def update(k: K, v: V): Unit = {
    val newPromise = Promise[V]()
    newPromise success v
    underlying.putIfAbsent(k, newPromise) match {
      case Some(value) => value success v
      case None => ()
    }
  }

  def apply(k: K): Future[V] = {
    val newPromise = Promise[V]()
    underlying.putIfAbsent(k, newPromise) match {
      case Some(value) => value.future
      case None => newPromise.future
    }
  }
}

object IMap2 {
  def empty[K, V]: IMap2[K, V] = new IMap2[K, V](TrieMap.empty[K, Promise[V]])
}
