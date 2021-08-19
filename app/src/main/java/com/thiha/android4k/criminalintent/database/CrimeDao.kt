package com.thiha.android4k.criminalintent.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.thiha.android4k.criminalintent.Crime
import java.util.*

@Dao
interface CrimeDao {
    @Query("SELECT * FROM crime")
    fun getCrimes(): LiveData<List<Crime>>

    @Query("SELECT * FROM crime WHERE id=(:id)")
    fun getCrime(id: UUID): LiveData<Crime?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCrime(crime: Crime)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateCrime(crime: Crime)

}