package com.keeply.ocrtest

import kastree.ast.psi.Parser

class kastree


fun main() {
    val src = "var a = A.init()"
    val file = Parser.parseFile(src)

    println(file)
}


