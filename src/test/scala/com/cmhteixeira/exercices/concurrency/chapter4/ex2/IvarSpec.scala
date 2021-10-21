package com.cmhteixeira.exercices.concurrency.chapter4.ex2
import org.scalatest.flatspec.AnyFlatSpec

class IvarSpec extends AnyFlatSpec {

  "foo" should "bar" in {
    val iVar = IVar.empty[Int]
    iVar := 2
    println(iVar())
    println(iVar())
  }
}
