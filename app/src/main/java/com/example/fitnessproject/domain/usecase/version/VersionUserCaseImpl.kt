package com.example.fitnessproject.domain.usecase.version

import com.example.fitnessproject.data.local.entity.DatabaseVersion
import com.example.fitnessproject.data.local.repository.VersionRepository

class VersionUserCaseImpl(private val versionRepository: VersionRepository) : VersionUseCase {
    override suspend fun getVersion(): DatabaseVersion {
        return versionRepository.getLastVersion()
    }
}