package com.cmhteixeira.exercices.concurrency.chapter4.ex2
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

class IVar[T] private (thePromise: Promise[T]) {
  def apply(): T = thePromise.future.value match {
    case Some(Success(value)) => value
    case Some(Failure(error)) => throw error
    case None => throw new IllegalStateException("Obtaining element from empty variable.")
  }
  def :=(x: T): Unit = try { thePromise.success(x) }
  catch {
    case _: IllegalStateException =>
      throw new IllegalStateException(s"Assigning element when variable is full.")
  }
}

object IVar {
  def empty[T]: IVar[T] = new IVar[T](Promise[T]())
}
