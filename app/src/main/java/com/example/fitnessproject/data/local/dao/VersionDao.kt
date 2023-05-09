package com.example.fitnessproject.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.fitnessproject.data.local.entity.DatabaseVersion

@Dao
interface VersionDao {
    @Query("SELECT * FROM database_version ORDER BY ID DESC LIMIT 1")
    fun getLastVersion(): DatabaseVersion
}