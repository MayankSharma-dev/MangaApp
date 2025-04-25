package com.ms.mangaapp.presentation.sign_in

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ms.mangaapp.BuildConfig
import com.ms.mangaapp.R
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class GoogleAuthUiClient(
    val context: Context, private val oneTapClient: SignInClient
) {
    private val auth = Firebase.auth

    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                beginSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            //if (e is CancellationException) throw e
            return signUp()
        }
        return result?.pendingIntent?.intentSender
    }

    private suspend fun signUp(): IntentSender? {
        val resultSignUp = try {
            oneTapClient.beginSignIn(
                buildSignUpRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return resultSignUp?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credentials = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credentials.googleIdToken
        // the Credentials we need to LogIn into Google Account.
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val authResult = auth.signInWithCredential(googleCredentials).await()
            //val user = authResult.user
//            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
//            if (isNewUser) {
//                addUserToFireStore()
//            }
            val user = authResult.user
            SignInResult(
                data = user?.run {
                    AccountInfo(
                        userID = uid,
                        userName = displayName,
                        profilePictureUrl = photoUrl?.toString(),
                        emailId = email
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                data = null, errorMessage = e.message
            )
        }
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    fun getSignInUser(): AccountInfo? =
        auth.currentUser?.run {
            AccountInfo(
                userID = uid,
                userName = displayName,
                profilePictureUrl = photoUrl?.toString(),
                emailId = email
            )
        }


    /// new check for custom auth email and password
    fun getFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }


    private fun beginSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder().setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.Builder().setSupported(true)
                .setFilterByAuthorizedAccounts(false)
                //.setServerClientId(context.getString(R.string.web_client_id)).build()
                .setServerClientId(BuildConfig.WEB_CLIENT_ID).build()
        ).setAutoSelectEnabled(true).build()
    }

    private fun buildSignUpRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder().setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder().setSupported(true)
                //.setServerClientId(context.getString(R.string.web_client_id))
                .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                .setFilterByAuthorizedAccounts(false).build()
        ).build()
    }


}