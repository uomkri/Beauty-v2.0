package com.devrock.beautyappv2.db

import android.content.Context
import androidx.room.*

@Database(entities = [Session::class], version = 1, exportSchema = false)
abstract class SessionDB : RoomDatabase() {

    abstract val sessionDBDao: SessionDao

    companion object {
        @Volatile
        private var INSTANCE: SessionDB? = null

        fun getInstance(context: Context): SessionDB {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SessionDB::class.java,
                        "session_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}

@Entity(tableName = "session_table")
data class Session (
    @PrimaryKey
    val id: Int = 0,

    @ColumnInfo(name = "session")
    val session: String
)

@Dao
interface SessionDao {
    @Insert
    fun insert(session: Session)

    @Update
    fun update(session: Session)

    @Query("SELECT * from session_table WHERE id = :key")
    fun get(key: Int): Session?

    @Query("DELETE FROM session_table")
    fun clear()
}