package com.ms.mangaapp.presentation.sign_in

import android.content.Intent
import android.content.IntentSender
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ms.mangaapp.data.preferences.UserPreferences
import com.ms.mangaapp.data.preferences.UserPreferencesRepository
import com.ms.mangaapp.connectivity.ConnectivityObserver
import com.ms.mangaapp.connectivity.NetworkConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferencesRepository: UserPreferencesRepository,
    private val networkConnectivityObserver: NetworkConnectivityObserver
) : ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun getSignedInUser(): Boolean {
        return authRepository.getSignInUser() != null
    }

    var email by mutableStateOf("")
        internal set

    var emailError by mutableStateOf("")
        internal set

    var password by mutableStateOf("")
        internal set

    var passwordError by mutableStateOf("")
        internal set

    var signInError by mutableStateOf("")
        internal set

    init {
        viewModelScope.launch {
           val userCred = preferencesRepository.userPreferencesFlow.first()
            email = userCred.email ?: ""
            password = userCred.password ?: ""
        }

    }

    val networkConnect = networkConnectivityObserver.observer()
        .stateIn(viewModelScope, SharingStarted.Lazily, ConnectivityObserver.Status.Unavailable)


    //private val eventTrigger = Channel<Sign>

    fun onSignInResult(result: SignInResult) {
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    fun resetState() {
        _state.update { SignInState() }
        println(">>>>>>>>>>>>> reset state")
    }

    suspend fun onOneTapLogin(): IntentSender? {
        return authRepository.signInOneTap()
    }

    suspend fun onSignInCustom(email: String, password: String) {
        if (!validation(email, password)) {
            // error
            return
        }

        val signInResult = authRepository.signInCustom(email, password)
        if (signInResult.errorMessage == null)
            preferencesRepository.updateUserPreferences(UserPreferences(email, password))
        onSignInResult(signInResult)
    }

    suspend fun onSignUpCustom(email: String, password: String) {
        if (!validation(email, password)) {
            // error
            return
        }

        val signInResult = authRepository.signUpCustom(email, password)
        if (signInResult.errorMessage == null)
            preferencesRepository.updateUserPreferences(UserPreferences(email, password))
        onSignInResult(signInResult)
    }

    fun setEmail(value: String) {
        email = value.trim()
    }

    fun setPassword(value: String) {
        password = value.trim()
    }

    private fun validation(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            emailError = "Please enter valid email"
            return false
        }
        if (password.isEmpty()) {
            passwordError = "Please enter valid password"
            return false
        }

        return true
    }

    suspend fun signInWithIntent(intent: Intent) {
        onSignInResult(authRepository.signInWithIntent(intent))
    }

    fun sendSnackbarEvent(){
        viewModelScope.launch {

        }
    }

}
