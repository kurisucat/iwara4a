package com.rerere.iwara4a.ui.screen.login

import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.rerere.iwara4a.R
import com.rerere.iwara4a.ui.activity.RouterActivity
import com.rerere.iwara4a.ui.component.Md3TopBar
import com.rerere.iwara4a.util.openUrl
import com.rerere.iwara4a.util.stringResource
import com.vanpra.composematerialdialogs.*

@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel = hiltViewModel()) {
    Scaffold(
        topBar = {
            TopBar(navController)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .navigationBarsPadding()
                .imePadding(),
            contentAlignment = Alignment.Center
        ) {
            Content(loginViewModel, navController)
        }
    }
}

@Composable
private fun Content(loginViewModel: LoginViewModel, navController: NavController) {
    val context = LocalContext.current
    var showPassword by remember {
        mutableStateOf(false)
    }

    // 登录进度对话框
    val progressDialog = rememberMaterialDialogState()
    MaterialDialog(progressDialog) {
        iconTitle(
            text = stringResource(R.string.screen_login_progress_dialog_title),
            icon = { CircularProgressIndicator(Modifier.size(30.dp)) }
        )
        message(stringResource(R.string.screen_login_progree_dialog_message))
    }
    // 登录失败
    val failedDialog = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = failedDialog,
        buttons = {
            positiveButton("好的") {
                failedDialog.hide()
            }
        }
    ) {
        title(stringResource(R.string.screen_login_failed_dialog_title))
        message(loginViewModel.errorContent)
    }

    // 内容
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // LOGO
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = R.drawable.miku,
                contentDescription = null
            )
        }

        // Username
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = loginViewModel.userName,
            onValueChange = { loginViewModel.userName = it },
            label = {
                Text(
                    text = stringResource(R.string.username)
                )
            },
            singleLine = true
        )

        // Password
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = loginViewModel.password,
            onValueChange = { loginViewModel.password = it },
            label = {
                Text(
                    text = stringResource(R.string.password)
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            trailingIcon = {
                Crossfade(targetState = showPassword) {
                    IconButton(onClick = {
                        showPassword = !showPassword
                    }) {
                        Icon(
                            if (it) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            null
                        )
                    }
                }
            }
        )

        // Login
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (loginViewModel.userName.isBlank() || loginViewModel.password.isBlank()) {
                    Toast.makeText(
                        context,
                        context.stringResource(R.string.screen_login_toast_must_not_empty),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                progressDialog.show()
                loginViewModel.login((context as RouterActivity).viewModel) {
                    // 处理结果
                    if (it) {
                        // 登录成功
                        Toast.makeText(
                            context,
                            context.stringResource(R.string.login_successful),
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.navigate("index") {
                            popUpTo("login") {
                                inclusive = true
                            }
                        }
                    } else {
                        // 登录失败
                        failedDialog.show()
                    }
                    progressDialog.hide()
                }
            }
        ) {
            Text(text = stringResource(R.string.login))
        }

        // Register
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                context.openUrl("https://ecchi.iwara.tv/user/register")
            }
        ) {
            Text(text = stringResource(R.string.register))
        }
    }
}

@Composable
private fun TopBar(navController: NavController) {
    Md3TopBar(
        title = {
            Text(text = stringResource(R.string.screen_login_title))
        }
    )
}