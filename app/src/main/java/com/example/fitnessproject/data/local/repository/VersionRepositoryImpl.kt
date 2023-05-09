package com.example.fitnessproject.data.local.repository

import com.example.fitnessproject.data.local.dao.VersionDao
import com.example.fitnessproject.data.local.entity.DatabaseVersion

class VersionRepositoryImpl(private val versionDao: VersionDao) : VersionRepository {
    override suspend fun getLastVersion(): DatabaseVersion {
        return versionDao.getLastVersion()
    }
}