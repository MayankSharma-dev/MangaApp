package com.ms.mangaapp.presentation.sign_in

import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ms.mangaapp.util.showToast
import kotlinx.coroutines.launch

@Composable
fun SignScreenUI(
    viewModel: SignInViewModel,
    onNavigate: () -> Unit
){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

//    LaunchedEffect(Unit) {
//        if (viewModel.getSignedInUser()) {
//            onNavigate()
//            //navController.navigate(MainScreen)
//        }
//    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSignInSuccessful) {
        if (state.isSignInSuccessful) {
            context.showToast("SignIn successfully.")
            onNavigate()
            //navController.navigateToMainScreen()
            viewModel.resetState()
        }
    }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == RESULT_OK) {
                scope.launch {
                    if (result.data == null)
                        return@launch
                    else {
                        viewModel.signInWithIntent(result.data!!)
                    }
                }
            }
        }
    )

    SignScreenUI(
        state = state,
        //modifier = modifier,
        email = viewModel.email,
        password = viewModel.password,
        emailError = viewModel.emailError,
        passwordError = viewModel.passwordError,
        onEmailValueChange = { viewModel.setEmail(it) },
        onPasswordValueChange = { viewModel.setPassword(it) },
        onOneTapLogin = {
            scope.launch {
                val signInIntentSender = viewModel.onOneTapLogin()
                launcher.launch(
                    IntentSenderRequest.Builder(
                        signInIntentSender ?: return@launch
                    ).build()
                )
            }
        },
        onSignInClick = { email, password ->
            scope.launch {
                viewModel.onSignInCustom(email, password)
            }
        },
        onSignUpClick = { email, password ->
            scope.launch {
                viewModel.onSignUpCustom(email, password)
            }
        },
        onForgetPasswordClick = {

        }
    )
}

@Composable
fun SignScreenUI(
    state: SignInState,
    email: String,
    password: String,
    emailError: String,
    passwordError: String,
    modifier: Modifier = Modifier,
    onEmailValueChange: (String) -> Unit,
    onPasswordValueChange: (String) -> Unit,
    onOneTapLogin: () -> Unit,
    onSignInClick: (email: String, password: String) -> Unit,
    onSignUpClick: (email: String, password: String) -> Unit,
    onForgetPasswordClick: () -> Unit
) {

    var isSignUp by rememberSaveable { mutableStateOf(false) }

    val greetingMessage = if (!isSignUp) "Welcome Back" else "Welcome"
    val enterDetailText =
        if (!isSignUp) "Please enter your details to Sign in" else "Please enter your details to Sign up"
    val signType = if (!isSignUp) "Sign In" else " Sign Up"
    val signOption =
        if (isSignUp) "Already have an account? Sign In" else "Don't have an account? Sign Up"

    val context = LocalContext.current

    LaunchedEffect(state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Zenithra", fontSize = 24.sp)
        Text(text = greetingMessage, fontSize = 28.sp)
        Text(text = enterDetailText, color = Color.Gray, fontSize = 14.sp)

        GoogleButton(onClick = onOneTapLogin)

        TextWithPartialHorizontalLines(text = "OR")
        TextFieldValidation(
            value = email,
            isPassword = false,
            placeholder = "Enter your email",
            isError = emailError.isNotEmpty(),
            errorMessage = emailError,
            onChange = onEmailValueChange
        )
        TextFieldValidation(
            value = password,
            isPassword = true,
            placeholder = "Enter your password",
            isError = passwordError.isNotEmpty(),
            errorMessage = passwordError,
            onChange = onPasswordValueChange
        )

        /*
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Your Email Address") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            trailingIcon = {
                Icon(
                    if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (showPassword) "Show Password" else "Hide Password",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onShowPasswordChange(!showPassword) }
                )
            },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )*/
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                // errorMessage = null  // Remove this, error message is now managed externally
                if (isSignUp) {
                    onSignUpClick(email, password)
                } else {
                    onSignInClick(email, password)
                }
            }
        ) {
            Text(text = signType)
        }
        TextButton(
            onClick = { isSignUp = !isSignUp }
        ) {
            Text(signOption)
        }
        /*
        if (errorMessage != null) { // Use the errorMessage passed as a parameter
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }
        */
        if (state.signInError != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = state.signInError, color = MaterialTheme.colorScheme.error)
        }
    }
}


/*
@Composable
fun SignScreenUI(
state: SignInState,
modifier: Modifier = Modifier,
onOneTapLogin: () -> Unit,
onSignInClick: (email: String, password: String) -> Unit,
onSignUpClick: (email: String, password: String) -> Unit,
onForgetPasswordClick: () -> Unit
) {
var email by remember { mutableStateOf("") }
var password by remember { mutableStateOf("") }
var isSignUp by remember { mutableStateOf(false) }
var errorMessage by remember { mutableStateOf<String?>(null) }
var showPassword by rememberSaveable { mutableStateOf(false) }

val greetingMessage = if(!isSignUp)"Welcome Back" else "Welcome"
val enterDetailText = if(!isSignUp) "Please enter your details to Sign in" else "Please enter your details to Sign up"
val signType = if(!isSignUp) "Sign In" else " Sign Up"
val signOption = if (!isSignUp) "Already have an account? Sign In" else "Don't have an account? Sign Up"


val context = LocalContext.current

LaunchedEffect(state.signInError) {
state.signInError?.let { error ->
    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
}
}

Column(
modifier = Modifier
    .fillMaxSize()
    .padding(16.dp),
verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
horizontalAlignment = Alignment.CenterHorizontally
) {

Text("Zenithra", fontSize = 24.sp)
Text(text = greetingMessage, fontSize = 28.sp)
Text(text = enterDetailText, color = Color.Gray, fontSize = 14.sp)

GoogleButton(onClick = onOneTapLogin)

TextWithPartialHorizontalLines(text = "OR")

OutlinedTextField(
    value = email,
    onValueChange = { email = it },
    label = { Text("Your Email Address") },
    modifier = Modifier.fillMaxWidth()
)
OutlinedTextField(
    value = password,
    onValueChange = { password = it },
    label = { Text("Password") },
    trailingIcon = {
        Icon(if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
            contentDescription = if (showPassword) "Show Password" else "Hide Password",
            tint = Color.Gray,
            modifier = Modifier
                .size(24.dp)
                .clickable { showPassword = !showPassword })
    },
    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
    modifier = Modifier.fillMaxWidth()
)
Button(
    modifier = Modifier.fillMaxWidth(),
    onClick = {
        errorMessage = null
        if(isSignUp){
            onSignUpClick(email, password)
        }else{
            onSignInClick(email, password)
        }
    }
) {
    Text(text = signType)
}
TextButton(
    onClick = { isSignUp = !isSignUp }
) {
    Text(signOption)
}
//        errorMessage?.let {
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(text = it, color = MaterialTheme.colorScheme.error)
//        }
if (state.signInError != null) {
    Spacer(modifier = Modifier.height(8.dp))
    Text(text = state.signInError, color = MaterialTheme.colorScheme.error)
}
}
}*/
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
//        MangaAppTheme {
//            AuthScreen()
//        }

//    AuthScreen(onOneTapLogin = {},
//        onSignIn = { email, password -> },
//        onSignUp = { email, password -> })

//    val configuration = androidx.work.Configuration()
//    CompositionLocalProvider(
//        // ... other providers ...
//        LocalConfiguration provides configuration
//    ) {
//        // ... rest of the preview ...
//    }
}