package com.example.fitnessproject.data.local.repository

import com.example.fitnessproject.data.local.entity.DatabaseVersion

interface VersionRepository {
    suspend fun getLastVersion(): DatabaseVersion
}