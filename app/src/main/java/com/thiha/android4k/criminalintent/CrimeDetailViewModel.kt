package com.thiha.android4k.criminalintent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

class CrimeDetailViewModel : ViewModel() {

    private var crimeRepo = CrimeRepository.get()
    private var crimeIdLiveData = MutableLiveData<UUID>()

    val crimeLiveData: LiveData<Crime?> = Transformations.switchMap(crimeIdLiveData) {
        crimeRepo.getCrime(it)
    }

    fun loadCrime(crimeId: UUID) {
        crimeIdLiveData.value = crimeId
    }

    fun updateCrime(crime: Crime) = crimeRepo.updateCrime(crime)

}