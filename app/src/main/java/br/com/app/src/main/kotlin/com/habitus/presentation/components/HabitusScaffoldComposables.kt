package br.com.app.src.main.kotlin.com.habitus.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.app.src.main.kotlin.com.habitus.R
import br.com.app.src.main.kotlin.com.habitus.data.entity.UserEntity
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.ArrowLeftSolid
import compose.icons.lineawesomeicons.PlusSolid

@Composable
fun TopAppBarForOtherScreens(
    title: String? = null,
    onIconClick: () -> Unit = {},
    isBackIconVisible: Boolean = true
) {
    Box(
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth()
            .paint(
                painter = painterResource(R.drawable.bc_top_appbar),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary),
                contentScale = ContentScale.FillWidth
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            if (isBackIconVisible) {
                Icon(
                    imageVector = LineAwesomeIcons.ArrowLeftSolid,
                    contentDescription = "Adicionar",
                    tint = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            onIconClick()
                        }
                        .align(Alignment.CenterStart)
                )
            }

            Text(
                text = title ?: "",
                color = MaterialTheme.colorScheme.onTertiary,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Light),
                modifier = Modifier
                    .align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }

    }
}

@Composable
fun TopAppBarForHomeScreen(
    onNavigateToRegisterHabits: () -> Unit,
    user: UserEntity
) {
    Box(
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth()
            .paint(
                painter = painterResource(R.drawable.bc_top_appbar),
                contentScale = ContentScale.FillWidth,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.tertiary)
            ),
    ) {
        // Botão flutuante de adicionar
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp)
                .size(50.dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                LineAwesomeIcons.PlusSolid,
                contentDescription = "Adicionar",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(12.dp)
                    .clickable {
                        onNavigateToRegisterHabits()
                    }
            )
        }

        // Nome do usuário e instrução
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp, bottom = 8.dp)
        ) {
            Text("Olá, ${user.username.replaceFirstChar { it.uppercase() }}!", color = Color.White, fontWeight = FontWeight.Bold)
            Text("Vamos criar hábitos!", color = Color.White.copy(alpha = 0.8f))
        }
    }
}
