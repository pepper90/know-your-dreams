package com.jpdevzone.knowyourdreams.database

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "dream_list_table")
data class Dream(
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    @ColumnInfo
    var dreamItem: String,

    @ColumnInfo
    var dreamDefinition: String,

    @ColumnInfo
    var isChecked: Boolean,

    @ColumnInfo
    var inHistory: Boolean,

    @ColumnInfo
    var modifiedAt: Long?,

    @ColumnInfo
    var visitedAt: Long?
) : BaseObservable() {
    @Ignore
    @Bindable
    fun getStatusChecked(): Boolean {
        return isChecked
    }

    @Ignore
    fun setStatusChecked(value: Boolean) {
        if (isChecked != value)
            isChecked = value
        notifyPropertyChanged(BR.statusChecked)
    }
}
