package com.keeply

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KeeplyApplication

fun main(args: Array<String>) {
    runApplication<KeeplyApplication>(*args)
}
