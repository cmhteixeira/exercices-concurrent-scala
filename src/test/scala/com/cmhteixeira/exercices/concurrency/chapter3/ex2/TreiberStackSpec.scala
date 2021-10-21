package com.cmhteixeira.exercices.concurrency.chapter3.ex2

import org.scalatest.flatspec.AnyFlatSpec

class TreiberStackSpec extends AnyFlatSpec{
  val treiberStack: TreiberStack[String] = TreiberStack.empty[String]
  val elems = (1 to 10)

  val runnables: Seq[Runnable] = (1 to 10).map(i => new Runnable {
    override def run(): Unit = {
      val t = elems.map(elem => elems.last*(i-1) + elem)
      t.foreach(j => treiberStack.push(s"Runnable-$i,Elem:$j"))
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

//    println(treiberStack.pop())
    (1 to 100000).foreach{ _ =>
      println(treiberStack.pop())
    }
  }



}
