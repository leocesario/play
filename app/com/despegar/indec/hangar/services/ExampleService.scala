package com.despegar.indec.hangar.services

import scaldi.{Injectable, Injector}

class ExampleService(implicit inj: Injector) extends Injectable {

 def exampleMethod(): Map[String,Int] = {
   Map("example" -> 123)
 }

}
