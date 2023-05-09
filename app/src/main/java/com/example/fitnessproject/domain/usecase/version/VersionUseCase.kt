package com.example.fitnessproject.domain.usecase.version

import com.example.fitnessproject.data.local.entity.DatabaseVersion

interface VersionUseCase {
    suspend fun getVersion(): DatabaseVersion
}