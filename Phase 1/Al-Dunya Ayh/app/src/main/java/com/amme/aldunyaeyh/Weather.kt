package com.amme.aldunyaeyh

data class Weather(val main: Main,
                   val name: String)

data class Main(
    val temp: Double
)
