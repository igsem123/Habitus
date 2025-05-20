package br.com.app.src.main.kotlin.com.habitus.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import br.com.app.src.main.kotlin.com.habitus.data.entity.TaskEntity

@Dao
interface TaskDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertTask(task: TaskEntity)
}