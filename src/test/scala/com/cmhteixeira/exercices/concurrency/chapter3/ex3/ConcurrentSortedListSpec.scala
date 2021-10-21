package com.cmhteixeira.exercices.concurrency.chapter3.ex3

import org.scalatest.flatspec.AnyFlatSpec

class ConcurrentSortedListSpec extends AnyFlatSpec{
  val sortedList: ConcurrentSortedList[Int] = ConcurrentSortedList.empty[Int]
  val elems = (1 to 15674)

  val runnables: Seq[Runnable] = (1 to 10).map(i => new Runnable {
    override def run(): Unit = {
      val t = elems.map(elem => elems.last*(i-1) + elem)
      val shuffledList = scala.util.Random.shuffle(t)
      shuffledList.foreach(elem => sortedList.add(elem))
    }
  })

  "foo" should "bar" in {
    val threads: Seq[Thread] = runnables.map { p =>
      val thread = new Thread(p)
      //      thread.setDaemon(false)
      thread
    }

    threads.foreach(_.start())
    threads.foreach(_.join())


    val res = sortedList.iterator.toList
    println(res == res.sorted)
    println(res.size)
  }



}
