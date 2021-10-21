package com.cmhteixeira.exercices.concurrency.chapter3.ex3

import com.cmhteixeira.exercices.concurrency.chapter3.ex3.ConcurrentSortedList._
import java.util.concurrent.atomic.AtomicReference
import scala.annotation.tailrec
import scala.annotation.unchecked.uncheckedVariance

class ConcurrentSortedList[A: Ordering] private (xs: AtomicReference[HelperList[A]]) {

  @tailrec
  private def innerHelper(lastTail: AtomicReference[HelperList[A]], a: A): Unit =
    lastTail.get() match {
      case node @ Node(head, _) if Ordering[A].compare(head, a) >= 0 =>
        val newTail = Node(a, new AtomicReference[HelperList[A]](node))
        if (!lastTail.compareAndSet(node, newTail)) add(a)
      case Node(head, tail) if Ordering[A].compare(head, a) < 0 => innerHelper(tail, a)
      case emptyNode @ EmptyNode =>
        val newTail = Node(a, new AtomicReference[HelperList[A]](emptyNode))
        if (!lastTail.compareAndSet(emptyNode, newTail)) add(a)
    }

  def add(a: A): Unit = innerHelper(xs, a)

  def iterator: Iterator[A] = new Iterator[A] {
    var foo = xs.get()

    override def hasNext: Boolean = foo match {
      case Node(_, _) => true
      case _ => false
    }

    override def next(): A = foo match {
      case Node(head, tail) =>
        foo = tail.get()
        head
      case EmptyNode => throw new NoSuchElementException("next on empty iterator.")
    }
  }
}

object ConcurrentSortedList {

  private sealed trait HelperList[+A]
  private case class Node[+A](head: A, tail: AtomicReference[HelperList[A]] @uncheckedVariance)
      extends HelperList[A]
  private case object EmptyNode extends HelperList[Nothing]

  def empty[A: Ordering]: ConcurrentSortedList[A] =
    new ConcurrentSortedList[A](new AtomicReference(EmptyNode))

  def apply[A: Ordering](elems: A*): ConcurrentSortedList[A] = {
    val emptyList = empty
    for (i <- elems) emptyList.add(i)
    emptyList
  }
}
