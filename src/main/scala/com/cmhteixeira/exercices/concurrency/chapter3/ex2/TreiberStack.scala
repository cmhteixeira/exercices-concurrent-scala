package com.cmhteixeira.exercices.concurrency.chapter3.ex2

import java.util.concurrent.atomic.AtomicReference
import scala.annotation.tailrec


final class TreiberStack[A] private(helperList: AtomicReference[List[A]]) {

  @tailrec
  def push(a: A): Unit = helperList.get() match {
    case current =>
      if (helperList.compareAndSet(current, ::(a, current))) ()
      else push(a)
  }

  @tailrec
  def pop(): A = helperList.get() match {
    case current@head :: tail =>
      if (helperList.compareAndSet(current, tail)) head
      else pop()
    case Nil => pop()
  }
}

object TreiberStack {
  def empty[A]: TreiberStack[A] = new TreiberStack[A](new AtomicReference(List.empty[A]))

  def apply[A](a: A*): TreiberStack[A] = {
    val treiberStack = TreiberStack.empty[A]
    for {i <- a} yield treiberStack.push(i)
    treiberStack
  }
}
