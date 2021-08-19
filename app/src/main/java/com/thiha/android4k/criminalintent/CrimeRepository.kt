package com.thiha.android4k.criminalintent

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.thiha.android4k.criminalintent.database.CrimeDao
import com.thiha.android4k.criminalintent.database.CrimeDatabase
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class CrimeRepository private constructor(context: Context) {

    private val database: CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME
    ).build()
    private val executor = Executors.newSingleThreadExecutor()
    private val crimeDao: CrimeDao = database.crimeDao()

    fun getCrimes(): LiveData<List<Crime>> = crimeDao.getCrimes()

    fun getCrime(id: UUID): LiveData<Crime?> = crimeDao.getCrime(id)

    fun insertCrime(crime: Crime) {
        executor.execute {
            crimeDao.insertCrime(crime)
        }
    }

    fun updateCrime(crime: Crime) {
        executor.execute { crimeDao.updateCrime(crime) }
    }

    companion object {
        private const val DATABASE_NAME = "crime-database"
        private var INSTANCE: CrimeRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }

}