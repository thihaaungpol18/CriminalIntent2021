package com.thiha.android4k.criminalintent

import androidx.lifecycle.ViewModel

class CrimeListViewModel : ViewModel() {
    private val crimeRepo = CrimeRepository.get()
    val crimeListLiveData = crimeRepo.getCrimes()

    init {
        //Populating Dummies
        for (i in 1 until 101) {
            insertCrime(
                Crime(
                    title = "Crime : $i",
                    isSolved = i % 3 == 0,
                    requirePolice = i % 2 == 0
                )
            )
        }
    }

    private fun insertCrime(crime: Crime) {
        crimeRepo.insertCrime(crime)
    }
}