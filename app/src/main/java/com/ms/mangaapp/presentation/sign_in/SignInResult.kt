package com.ms.mangaapp.presentation.sign_in


data class SignInResult(
    val data: AccountInfo?, val errorMessage: String?
)

data class AccountInfo(
    val userID: String, val userName: String?, val profilePictureUrl: String?, val emailId: String?
)
