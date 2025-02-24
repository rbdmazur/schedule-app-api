package com.example.testdata

import com.example.testdata.ui.AddStudyKotlin
import io.ktor.server.application.*

fun Application.configureTest() {
    AddStudyKotlin.createGUI()
}