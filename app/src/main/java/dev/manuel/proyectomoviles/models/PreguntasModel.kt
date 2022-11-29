package dev.manuel.proyectomoviles.models

data class PreguntasModel(
    var question:String ?= null,
    var answer:String ?= null,
    val correctAnswer:String ?= null,
    val supportingText:String ?= null)
