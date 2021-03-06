package com.jpdevzone.knowyourdreams.favourites

import android.app.Application
import androidx.lifecycle.*
import com.jpdevzone.knowyourdreams.database.Dream
import com.jpdevzone.knowyourdreams.database.DreamDatabaseDao
import com.jpdevzone.knowyourdreams.randomInt
import kotlinx.coroutines.*

class FavouritesViewModel(
    val database: DreamDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    val favourites = database.getFavourites(true)

    private val _navigateToFavouritesData = MutableLiveData<Int?>()
    val navigateToFavouritesData: LiveData<Int?>
        get() = _navigateToFavouritesData

    fun onDreamClicked(id: Int) {
        _navigateToFavouritesData.value = id
    }

    fun onDreamNavigated() {
        _navigateToFavouritesData.value = null
    }

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun updateChecked(dream: Dream) {
        uiScope.launch {
            update(dream)
        }
    }

    private suspend fun update(dream: Dream) {
        withContext(Dispatchers.IO) {
            database.update(dream)
        }
    }

    fun deleteAll() {
        uiScope.launch {
            updateAllToFalse()
        }
    }

    private suspend fun updateAllToFalse() {
        withContext(Dispatchers.IO) {
            database.updateAllFavouritesToFalse(false)
        }
    }

    val favouritesIsNotEmpty = Transformations.map(favourites) {
        it.isNotEmpty()
    }

    private val randomDream = MediatorLiveData<Dream>()

    fun getRandomDream() = randomDream

    init {
        randomDream.addSource(database.get(randomInt), randomDream::setValue)
    }

    fun updateRandomDream(id: Int, status: Boolean) {
        uiScope.launch {
            updateRandomChecked(id, status)
        }
    }

    private suspend fun updateRandomChecked(id: Int, status: Boolean) {
        withContext(Dispatchers.IO) {
            database.updateFavouritesById(id, status, System.currentTimeMillis())
        }
    }

    fun addToHistory(id: Int) {
        uiScope.launch {
            updateInHistory(id)
        }
    }

    private suspend fun updateInHistory(id: Int) {
        withContext(Dispatchers.IO) {
            database.updateHistoryById(id, true, System.currentTimeMillis())
        }
    }
}