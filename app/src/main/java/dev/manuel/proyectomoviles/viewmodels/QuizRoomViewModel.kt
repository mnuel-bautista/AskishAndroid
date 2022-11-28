package dev.manuel.proyectomoviles.viewmodels

import androidx.lifecycle.ViewModel
import dev.manuel.proyectomoviles.repositories.QuizzRoomRepository

class QuizRoomViewModel(): ViewModel() {

    val repository = QuizzRoomRepository()

    override fun onCleared() {
        repository.removeListeners()
    }

}