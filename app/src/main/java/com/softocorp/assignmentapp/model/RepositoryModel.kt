package com.softocorp.assignmentapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RepositoryModel(
    var full_name: String? = null,
    var name: String? = null,
    var description: String? = null,
    var has_issues: Boolean? = null,
    var html_url: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}