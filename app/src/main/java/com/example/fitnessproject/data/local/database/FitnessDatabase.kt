package com.example.fitnessproject.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.fitnessproject.data.exception.ErrorException
import com.example.fitnessproject.data.exception.MyException
import com.example.fitnessproject.data.local.TimeConverter
import com.example.fitnessproject.data.local.dao.*
import com.example.fitnessproject.data.local.database.FitnessDatabase.Companion.DATABASE_VERSION
import com.example.fitnessproject.data.local.entity.*
import com.example.fitnessproject.data.network.entity.TopicResponse
import kotlinx.coroutines.CoroutineScope


@Database(
    entities = [User::class,
        DatabaseVersion::class,
        Topic::class,
        TopicDetail::class,
        TopicDetailSelected::class,
        TopicSelected::class,
        UserInformation::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(TimeConverter::class)
abstract class FitnessDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun versionDao(): VersionDao
    abstract fun topicDao(): TopicDao
    abstract fun topicDetailDao(): TopicDetailDao
    abstract fun weightDao(): WeightDao


    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_VERSION_ERROR = -1

//        var MIGRATION_1_TO_2: Migration = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
////                database.execSQL("ALTER TABLE question ADD COLUMN answerD TEXT")
//            }
//        }

        @Volatile
        private var INSTANCE: FitnessDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): FitnessDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitnessDatabase::class.java,
                    "fitness_database"
                ).addCallback(WordDatabaseCallback(scope, context))
//                    .addMigrations(MIGRATION_1_TO_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class WordDatabaseCallback(
        private val scope: CoroutineScope,
        private val context: Context
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // use this solution to pre-populate to make sure data insert success
            // after use database
            try {
                val jsonString = context.assets.open("topic.json").bufferedReader()
                    .use { it.readText() }
                val topicResponse = TopicResponse.fromJson(jsonString)
                val stringTopic = StringBuilder()
                val stringTopicDetail = StringBuilder()
                stringTopic.append("INSERT INTO topic (id_topic, name, type) VALUES ")
                stringTopicDetail.append("INSERT INTO detail_topic (name, description, id_video, tutorial, id_topic) VALUES ")
                for (i in topicResponse.topic.indices) {
                    val item = topicResponse.topic[i]
                    val idTopic = item.id
                    val value = "('$idTopic', '${item.name}', '${item.type}')"
                    stringTopic.append(value)
                    if (i == topicResponse.topic.size - 1) {
                        stringTopic.append(";")
                    } else {
                        stringTopic.append(", ")
                    }
                    for (j in item.items.indices) {
                        val detail = item.items[j]
                        val valueDetail =
                            "('${detail.name}', '${detail.description}', '${detail.idVideo}', '${detail.tutorial}', '$idTopic')"
                        stringTopicDetail.append(valueDetail)
                        if (i == topicResponse.topic.size - 1 && j == item.items.size - 1) {
                            stringTopicDetail.append(";")
                        } else {
                            stringTopicDetail.append(", ")
                        }
                    }
                }
                db.execSQL(stringTopic.toString())
                db.execSQL(stringTopicDetail.toString())
                db.execSQL("INSERT INTO database_version (version) VALUES ($DATABASE_VERSION)")
            } catch (e: Exception) {
                db.execSQL("INSERT INTO database_version (version) VALUES (-1)")
                throw MyException(error = ErrorException.SQLITE_ERROR)
            }


            //Use liveData to get data change if use this solution to pre-populate
//            INSTANCE?.let { database ->
            //pre-populate topic
            //test to pre-populate database
//                scope.launch {
//                    val userDao = database.userDao()
//                    val user2 = User(
//                        name = "name",
//                        password = "pass",
//                        gender = Gender.MALE.gender,
//                        height = 10.1,
//                        weight = 120.1
//                    )
//                    val list = mutableListOf<User>()
//                    for (i in 0..50) {
//                        list.add(user2)
//                    }
//                    userDao.insertUserList(list)
//                }
//            }
        }
    }
}