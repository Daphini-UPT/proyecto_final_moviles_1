package com.example.proyecto_final_moviles_1.screens.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.proyecto_final_moviles_1.R
import com.example.proyecto_final_moviles_1.components.EmailInput
import com.example.proyecto_final_moviles_1.components.MangaLogo
import com.example.proyecto_final_moviles_1.components.PasswordInput
import com.example.proyecto_final_moviles_1.navigation.MangaScreens


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(navController: NavController,
                viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
                ){

    val showLoginForm = rememberSaveable { mutableStateOf(true) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top){
            MangaLogo()
            if(showLoginForm.value) UserForm(loading = false,isCreateAccount = false){
                email,password->


                viewModel.singInWithEmailAndPassword(email, password){
                    navController.navigate(MangaScreens.MangaHomeScreen.name)  // como creamos un lambda ahora mandamos el navcontroller que esta creado para poder navergar al homescreen
                }

            }
            else{
                UserForm(loading = false,isCreateAccount = true){email,password ->
                    viewModel.createUserWithEmailAndPassword(email, password){
                        navController.navigate(MangaScreens.MangaHomeScreen.name)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(15.dp))

        Row(modifier = Modifier.padding(15.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
            ){
            val text = if (showLoginForm.value) "Registrarse" else "Iniciar Sesion"
            Text(text = "¿Nuevo Usuario?")
            Text(text,
            modifier = Modifier
                .clickable {  //hacemos el texto clickeable
                    showLoginForm.value = !showLoginForm.value
                }
                .padding(start = 5.dp),
            fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.secondaryVariant
            )
        }

        
    }

}

@ExperimentalComposeUiApi
@Preview
@Composable
fun UserForm(
    loading: Boolean = false,
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit = {email, pwd ->}
){
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }
    val passwordFocusRequest = FocusRequester.Default
    val keyboarController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }

    val modifier = Modifier
        .height(250.dp)
        .background(MaterialTheme.colors.background)
        .verticalScroll(rememberScrollState())

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        if(isCreateAccount) Text(text = stringResource(id = R.string.create_acct)
            , modifier = Modifier.padding(4.dp)) else Text("")

        EmailInput(emailState = email, enabled = !loading, onAction = KeyboardActions{
            passwordFocusRequest.requestFocus()  // sirve que cuando pongamos enter salte al sgte
        })

        PasswordInput(
            modifier=Modifier.focusRequester(passwordFocusRequest),
            passwordState = password,
            labelId = "Password",
            enabled = !loading, // sirve para cambiar el estado del boton
            passwordVisibility = passwordVisibility,
            onAction = KeyboardActions{
                if(!valid) return@KeyboardActions
                onDone(email.value.trim(), password.value.trim()) // esto se llama cuando se elija la opcion done
            }

        )
        SubmitButton(
            textId = if(isCreateAccount)"Crear Cuenta" else "Iniciar Sesión",
            loading = loading,
            validInputs = valid // se valida si los textfield estan llenos
        ){
            onDone(email.value.trim(), password.value.trim())
            keyboarController?.hide()
        }


    }



}

@Composable
fun SubmitButton(textId: String,
                 loading: Boolean,
                 validInputs: Boolean,
                 onClick:() -> Unit) {
    Button(onClick = onClick,
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        enabled = !loading && validInputs,
        shape = CircleShape
    ) {
        if (loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
        else Text(text = textId , modifier = Modifier.padding((5.dp)))
    }
}




