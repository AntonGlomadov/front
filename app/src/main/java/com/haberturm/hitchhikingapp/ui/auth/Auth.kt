package com.haberturm.hitchhikingapp.ui.auth

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.haberturm.hitchhikingapp.ui.nav.NavRoute

object AuthRoute : NavRoute<AuthViewModel> {
    override val route: String = "auth/"

    @Composable
    override fun Content(viewModel: AuthViewModel) = Auth(viewModel::onAuthClicked)

    @Composable
    override fun viewModel(): AuthViewModel = hiltViewModel()

}

@Composable
fun Auth(
    onAuthClicked: () -> Unit
) {
    Text(text = "Hello World!")
    Button(onClick = onAuthClicked) {
        Text(text = "Auth")
    }
}

