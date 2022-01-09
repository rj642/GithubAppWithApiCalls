package com.softocorp.assignmentapp.db

import androidx.room.*
import com.softocorp.assignmentapp.model.RepositoryModel

@Dao
interface RepositoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRepository(vararg repositoryModel: RepositoryModel)

    @Delete
    fun deleteRepository(repositoryModel: RepositoryModel)

    @Query("select * from RepositoryModel")
    fun queryAllRepository(): List<RepositoryModel>

    @Query("select * from RepositoryModel where id = :id")
    fun queryRepositoryById(id: Long): RepositoryModel

    @Query("delete from RepositoryModel where id = :id")
    fun queryDeleteById(id: Long)

}