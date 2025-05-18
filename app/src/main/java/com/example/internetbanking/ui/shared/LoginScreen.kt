package com.example.internetbanking.ui.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LockPerson
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.internetbanking.R
import com.example.internetbanking.ui.theme.custom_dark_green
import com.example.internetbanking.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    navController: NavHostController
) {
    val loginUiState by loginViewModel.uiState.collectAsState()
    var idInput by remember { mutableStateOf("nnt0406") }
    var passwordInput by remember { mutableStateOf("test123") }
    var isPasswordShowing by remember { mutableStateOf(false) }

    AppBackground(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            modifier = Modifier
                .systemBarsPadding()
                .padding(horizontal = 40.dp)
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.padding(top = 100.dp)
                )
                Text(
                    text = "Welcome to GreenVault",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = custom_dark_green,
                    modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
                )
                Text(
                    text = "\"Grow Safe, Go Green\"",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = custom_dark_green
                )
                Spacer(modifier = Modifier.height(20.dp))
                LoginTextField(
                    value = idInput,
                    onValueChange = { idInput = it },
                    label = "Account ID",
                    leadingIcon = Icons.Filled.Person,
                    onTrailingIconClick = { idInput = "" },
                    contentDesc = "User's ID",
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                LoginTextField(
                    value = passwordInput,
                    onValueChange = { passwordInput = it },
                    label = "Password",
                    leadingIcon = Icons.Filled.LockPerson,
                    onTrailingIconClick = { passwordInput = "" },
                    onShowPasswordIconClick = { isPasswordShowing = !isPasswordShowing },
                    contentDesc = "User's Password",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    isContentShowing = isPasswordShowing
                )
                Spacer(modifier = Modifier.height(5.dp))
                Box(
                    contentAlignment = Alignment.CenterEnd,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = loginUiState.loginFailedMessage,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                GreenGradientButton(
                    onButtonClick = {
                        loginViewModel.login(
                            userId = idInput,
                            password = passwordInput,
                            navController = navController
                        )
                    },
                    buttonText = "Sign In",
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    onTrailingIconClick: () -> Unit,
    contentDesc: String,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier,
    isContentShowing: Boolean = true,
    onShowPasswordIconClick: () -> Unit = {}
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = label)
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = contentDesc,
                tint = custom_dark_green
            )
        },
        trailingIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                if (contentDesc == "User's Password") {
                    IconButton(
                        onClick = onShowPasswordIconClick
                    ) {
                        if (isContentShowing) {
                            Icon(
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = "Show Password",
                                tint = Color.Gray,
                                modifier = Modifier.padding(0.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = "Hide Password",
                                tint = Color.Gray
                            )
                        }
                    }
                }
                IconButton(
                    onClick = onTrailingIconClick
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear text field",
                        tint = Color.Gray
                    )
                }
            }
        },
        visualTransformation = if (!isContentShowing) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedTextColor = Color.Black,
            focusedTextColor = Color.Black,
            unfocusedLabelColor = Color.Gray,
            focusedLabelColor = Color.Gray
        ),
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
    )
}

@Preview(
    showBackground = true
)
@Composable
fun ScreenPreview(){
    val fakeViewModel: LoginViewModel = viewModel()
    val fakeNavController: NavHostController = rememberNavController()

    LoginScreen(fakeViewModel, fakeNavController)
}