package com.ms.mangaapp.presentation.sign_in

import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class AuthRepository(
    private val googleAuthUiClient: GoogleAuthUiClient,
) {
    private val auth = Firebase.auth

    suspend fun signUpCustom(email: String, password: String): SignInResult {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            // Sign-up successful
            println("Sign-up successful")

            val accountInfo = auth.currentUser?.run {
                AccountInfo(
                    userID = uid,
                    userName = displayName,
                    profilePictureUrl = photoUrl?.toString(),
                    emailId = email
                )
            }

            if (accountInfo != null) println(">>>>>>>>>>> LoggedIn User Details: ${accountInfo.emailId} ::: ${accountInfo.userID}")

            SignInResult(accountInfo, null)
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                data = null, errorMessage = e.message
            )
        }
    }

    suspend fun signInCustom(email: String, password: String): SignInResult {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            println("Sign-in successful")

            val accountInfo = auth.currentUser?.run {
                AccountInfo(
                    userID = uid,
                    userName = displayName,
                    profilePictureUrl = photoUrl?.toString(),
                    emailId = email
                )
            }

            if (accountInfo != null) println(">>>>>>>>>>> LoggedIn User Details: ${accountInfo.emailId} ::: ${accountInfo.userID}")

            SignInResult(accountInfo, null)
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                data = null, errorMessage = e.message
            )
        }
    }

    suspend fun signInOneTap() = googleAuthUiClient.signIn()

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        return googleAuthUiClient.signInWithIntent(intent)
    }

    suspend fun signOutUser() {
        val auth = FirebaseAuth.getInstance()
        val providerId = getAuthenticationMethod()

        when (providerId) {
            "google.com" -> {
                // Google One Tap sign-out
                googleAuthUiClient.signOut()
            }

            "password" -> {
                // Email/Password sign-out
                auth.signOut()
            }

            else -> {
                // Handle unknown or unlinked providers (optional)
                auth.signOut()
            }
        }
    }


    fun getSignInUser(): AccountInfo? = auth.currentUser?.run {
        AccountInfo(
            userID = uid,
            userName = displayName,
            profilePictureUrl = photoUrl?.toString(),
            emailId = email
        )
    }

    private fun getAuthenticationMethod(): String? {
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        return user?.providerData?.firstOrNull()?.providerId
    }
}