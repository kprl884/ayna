package com.techtactoe.ayna.presentation.screens.detail

import androidx.lifecycle.ViewModel
import com.techtactoe.ayna.data.MuseumObject
import com.techtactoe.ayna.data.MuseumRepository
import kotlinx.coroutines.flow.Flow

class DetailViewModel(private val museumRepository: MuseumRepository) : ViewModel() {
    fun getObject(objectId: Int): Flow<MuseumObject?> =
        museumRepository.getObjectById(objectId)
}
