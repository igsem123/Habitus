package br.com.app.src.main.kotlin.com.habitus.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.app.src.main.kotlin.com.habitus.R
import br.com.app.src.main.kotlin.com.habitus.ui.theme.HabitusTheme
import br.com.app.src.main.kotlin.com.habitus.ui.theme.PrimaryDarkColor
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.CannabisSolid
import compose.icons.lineawesomeicons.Envelope
import compose.icons.lineawesomeicons.LockSolid
import compose.icons.lineawesomeicons.PhoneSolid
import compose.icons.lineawesomeicons.User
import compose.icons.lineawesomeicons.UserLockSolid

/**
 * Tela inicial do aplicativo, onde o usuário pode se cadastrar.
 * @param modifier Modificador para personalizar a aparência da tela.
 * @param onNavigateToHome Função de callback chamada quando o usuário clica no botão "Cadastrar!".
 */
@Composable
fun InitialFormScreen(modifier: Modifier = Modifier, onNavigateToHome: () -> Unit) {
    // Cores do gradiente
    val startColor = Color(0xFF007FFF) // Azul vibrante
    val endColor = Color(0xFF00FFFF) // Azul turquesa/ciano

    val secondStartColor = Color(0xFF00C9A7) // Verde
    val secondEndColor = Color(0xFF00FFFF) // Azul turquesa/ciano

    // Primeiro gradiente linear
    val gradientBrush = Brush.linearGradient(
        colors = listOf(startColor, endColor, startColor, endColor),
        start = Offset(0f, 0f), // Início do gradiente (canto superior esquerdo)
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY) // Fim do gradiente (canto inferior direito)
    )

    // Segundo gradiente linear
    val secondGradientBrush = Brush.linearGradient(
        colors = listOf(secondStartColor, secondEndColor, secondStartColor, secondEndColor),
        start = Offset(0f, 0f), // Início do gradiente (canto superior esquerdo)
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY) // Fim do gradiente (canto inferior direito)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .graphicsLayer(
                        compositingStrategy = CompositingStrategy.Offscreen
                    )
                    .drawWithContent {
                        drawContent()
                        drawRect(
                            brush = gradientBrush,
                            blendMode = BlendMode.SrcIn
                        )
                    }
                    .size(250.dp)
            )
            Text(
                text = "Que bom que você está aqui!",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge.copy(
                    brush = gradientBrush
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Vamos juntos transformar sua vida em um hábito saudável e sustentável.",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Light
            )
            Text(
                text = "Vamos começar?",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleLarge.copy(
                    brush = secondGradientBrush
                ),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
            )
            Text(
                text = "Mas antes, precisamos de algumas informações.",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Light,
            )
            CustomOutlinedTextField(
                value = "",
                onValueChange = {},
                label = "Nome",
                placeholder = "Digite seu nome...",
                leadingIcon = LineAwesomeIcons.User,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrectEnabled = true,
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                )
            )
            CustomOutlinedTextField(
                value = "",
                onValueChange = {},
                label = "E-mail",
                placeholder = "Digite seu e-mail...",
                leadingIcon = LineAwesomeIcons.Envelope,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrectEnabled = true,
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                )
            )
            CustomOutlinedTextField(
                value = "",
                onValueChange = {},
                label = "Telefone",
                placeholder = "",
                leadingIcon = LineAwesomeIcons.PhoneSolid,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                prefix = {
                    Text(
                        text = "+55",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            )
            CustomOutlinedTextField(
                value = "",
                onValueChange = {},
                label = "Senha",
                placeholder = "Digite sua senha...",
                leadingIcon = LineAwesomeIcons.LockSolid,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    autoCorrectEnabled = true,
                    imeAction = ImeAction.Next
                )
            )
            CustomOutlinedTextField(
                value = "",
                onValueChange = {},
                label = "Confirme sua senha",
                placeholder = "Digite sua senha novamente...",
                leadingIcon = LineAwesomeIcons.UserLockSolid,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    autoCorrectEnabled = true,
                    imeAction = ImeAction.Send
                )
            )

            TextButton(
                onClick = {
                    onNavigateToHome()
                },
                modifier = Modifier
                    .padding(16.dp)
                    .border(width = 1.dp, brush = secondGradientBrush, shape = RoundedCornerShape(24.dp))
                    .padding(4.dp),
            ) {
                Text(
                    text = "Cadastrar!",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleLarge.copy(
                        brush = secondGradientBrush
                    ),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
private fun CustomOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    label: String,
    placeholder: String,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    isError: Boolean = false,
    isErrorValue: String = "",
    isErrorLabel: String = "",
    isErrorPlaceholder: String = "",
    isErrorLeadingIcon: @Composable (() -> Unit)? = null,
    isErrorTrailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation? = VisualTransformation.None,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null,
    prefix: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.secondary
            )
        },
        singleLine = true,
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, top = 4.dp)
            .fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedBorderColor = Color(0xFF00AC90),
            focusedBorderColor = Color(0xFF00AC90),
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
            focusedTrailingIconColor = MaterialTheme.colorScheme.secondary
        ),
        leadingIcon = {
            leadingIcon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = "Ícone de usuário",
                    tint = Color(0xFF00AC90)
                )
            }
        },
        trailingIcon = {
            trailingIcon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = it.name,
                    tint = Color(0xFF00AC90)
                )
            }
        },
        placeholder = {
            Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.secondary
            )
        },
        shape = RoundedCornerShape(16.dp),
        visualTransformation = visualTransformation ?: VisualTransformation.None,
        keyboardOptions = keyboardOptions ?: KeyboardOptions.Default,
        keyboardActions = keyboardActions ?: KeyboardActions.Default,
        prefix = prefix
    )
}

@Preview (showBackground = true, showSystemUi = true)
@Composable
private fun InitialFormScreenPreview() {
    HabitusTheme {
        InitialFormScreen(
            modifier = Modifier,
            onNavigateToHome = {}
        )
    }
}