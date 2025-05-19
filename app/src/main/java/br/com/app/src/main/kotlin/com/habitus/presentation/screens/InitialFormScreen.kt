package br.com.app.src.main.kotlin.com.habitus.presentation.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.clickable
import androidx.compose.animation.with
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import br.com.app.src.main.kotlin.com.habitus.R

/**
 * Tela inicial do app com navegação entre splash, cadastro e login por swipe vertical.
 *
 * O usuário começa na tela de splash. Ao deslizar para cima, ele é levado para o cadastro.
 * Dentro do cadastro, ele pode ir para a tela de login, e de lá pode voltar para o cadastro.
 *
 * @param modifier Modificador opcional para customizar a aparência da tela.
 * @param onNavigateToHome Função chamada quando o usuário completa o cadastro ou login com sucesso,
 *                         levando para a tela principal do app.
 */


@OptIn(androidx.compose.animation.ExperimentalAnimationApi::class)
@Composable
fun InitialFormScreen(modifier: Modifier = Modifier, onNavigateToHome: () -> Unit) {
    var currentScreen by remember { mutableStateOf("splash") }

    //Detecta quando o usuário desliza para cima para mudar a tela splash para cadastro
    val swipeGesture = Modifier.pointerInput(Unit) {
        detectVerticalDragGestures { _, dragAmount ->
            if (dragAmount < -50) {
                currentScreen = "cadastro"
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .then(swipeGesture),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = {
                (fadeIn() + slideInVertically(initialOffsetY = { it })) with
                        (fadeOut() + slideOutVertically(targetOffsetY = { -it }))
            },
            label = "ScreenTransition"
        ) { screen ->
            when(screen) {
                "splash" -> SplashContent()
                "cadastro" -> CadastroForm(
                    onNavigateToHome = onNavigateToHome,
                    onGoToLogin = { currentScreen = "login" }
                )
                "login" -> LoginForm(
                    onNavigateToHome = onNavigateToHome,
                    onGoToCadastro = { currentScreen = "cadastro" }
                )
            }
        }
    }
}

/**
 * Tela de splash do app.
 *
 * Exibe apenas o logo centralizado, servindo como introdução inicial antes do usuário interagir.
 */

@Composable
fun SplashContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.logo_app),
                contentDescription = "Logo Habitus",
                modifier = Modifier.size(200.dp),
                colorFilter = null
            )
        }
    }
}

/**
 * Tela de cadastro para criação de conta.
 *
 * Exibe o formulário para o usuário preencher nome, e-mail, senha e confirmação de senha.
 * Ao concluir, pode seguir para a tela principal ou ir para a tela de login.
 *
 * @param onNavigateToHome Chamada quando o usuário finaliza o cadastro com sucesso.
 * @param onGoToLogin Chamada quando o usuário escolhe ir para a tela de login.
 */

@Composable
fun CadastroForm(onNavigateToHome: () -> Unit, onGoToLogin: () -> Unit) {
    //Campos do formulário de cadastro
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }

    val azulMarinho = Color(0xFF003366)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(azulMarinho),
            contentAlignment = Alignment.BottomCenter
        ) { }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_app1),
                contentDescription = "Logo Habitus",
                modifier = Modifier
                    .height(120.dp)
                    .width(120.dp)
                    .padding(bottom = 10.dp)
            )
            Text(
                "Crie sua conta",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.padding(top = 8.dp)
            )


            Spacer(modifier = Modifier.height(24.dp))

            //Campo para o nome
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome de usuário") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50.dp)
            )

            //Campo para o e-mail
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(50.dp)
            )

            //Campo para a senha
            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                label = { Text("Senha") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(50.dp)
            )

            //Campo para confirmar a senha
            OutlinedTextField(
                value = confirmarSenha,
                onValueChange = { confirmarSenha = it },
                label = { Text("Confirme sua senha") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                shape = RoundedCornerShape(50.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onNavigateToHome() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = azulMarinho),
                shape = RoundedCornerShape(50)
            ) {
                Text("Cadastrar", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            //Link para ir para a tela de login
            Row {
                Text("Já tem uma conta? ")
                Text(
                    text = "Login",
                    color = azulMarinho,
                    modifier = Modifier.clickable {
                        onGoToLogin()

                    }
                )
            }
        }
    }
}

/**
 * Tela de login do usuário.
 *
 * Permite que o usuário entre com e-mail e senha. Também pode acessar a tela de cadastro
 * caso ainda não tenha uma conta.
 *
 * @param onNavigateToHome Chamada quando o login é feito com sucesso.
 * @param onGoToCadastro Chamada quando o usuário escolhe ir para a tela de cadastro.
 */

@Composable
fun LoginForm(onNavigateToHome: () -> Unit, onGoToCadastro: () -> Unit) {
    //Campos do formulário de login
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    val azulMarinho = Color(0xFF003366)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(azulMarinho),
            contentAlignment = Alignment.BottomCenter
        ) {}

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_app1),
                contentDescription = "Logo Habitus",
                modifier = Modifier
                    .height(120.dp)
                    .width(120.dp)
                    .padding(bottom = 10.dp)
            )
            Text(
                "Faça login",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            //Campo para e-mail
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(50.dp)
            )

            //Campo para senha
            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                label = { Text("Senha") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                shape = RoundedCornerShape(50.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onNavigateToHome() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = azulMarinho),
                shape = RoundedCornerShape(50)
            ) {
                Text("Entrar", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text("Não tem uma conta? ")
                Text(
                    text = "Cadastre-se",
                    color = azulMarinho,
                    modifier = Modifier.clickable {
                        onGoToCadastro()
                    }
                )
            }
        }
    }
}
