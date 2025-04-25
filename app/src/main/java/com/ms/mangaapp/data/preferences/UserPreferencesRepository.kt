package com.ms.mangaapp.data.preferences

import android.util.Log
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val userPreferencesStore: DataStore<UserPreferences>
) {
    val userPreferencesFlow: Flow<UserPreferences> = userPreferencesStore.data.catch { exception ->
        if (exception is IOException) {
            Log.e(TAG, "Error reading preferences.", exception)
            emit(UserPreferences()) // Emit default value on error
        } else {
            throw exception
        }
    }

    suspend fun updateUserPreferences(userPreferences: UserPreferences) {
        try {
            userPreferencesStore.updateData {
                userPreferences
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating secret token.", e)
            throw e
        }
    }

    companion object {
        //const val USER_DATASTORE_FILE_NAME = "user-settings.json"
        const val USER_DATASTORE_FILE_NAME = "user-preferences.pb"
        private const val TAG = "PreferencesData"
    }
}