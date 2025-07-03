package br.com.app.src.main.kotlin.com.habitus.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FlexibleBottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
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
import compose.icons.lineawesomeicons.AwardSolid
import compose.icons.lineawesomeicons.CogSolid
import compose.icons.lineawesomeicons.HomeSolid
import compose.icons.lineawesomeicons.PlusSolid

@Composable
fun TopAppBarForOtherScreens(
    title: String? = null,
    onIconClick: () -> Unit = {},
    isBackIconVisible: Boolean = true
) {
    Box(
        modifier = Modifier
            .background(color = Color.Transparent)
            .height(120.dp)
            .fillMaxWidth()
            .paint(
                painter = painterResource(R.drawable.bc_top_appbar),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
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
                    tint = MaterialTheme.colorScheme.onPrimary,
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
                color = MaterialTheme.colorScheme.onPrimary,
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
    onNavigateToPreRegisterHabits: () -> Unit,
    user: UserEntity
) {
    Box(
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth()
            .paint(
                painter = painterResource(R.drawable.bc_top_appbar),
                contentScale = ContentScale.FillWidth,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
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
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(12.dp)
                    .clickable {
                        onNavigateToPreRegisterHabits()
                    }
            )
        }

        // Nome do usuário e instrução
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp, bottom = 8.dp, top = 28.dp),
        ) {
            Text(
                "Olá, ${user.username.replaceFirstChar { it.uppercase() }}!",
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
            Text("Vamos criar hábitos!", color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BottomAppBar(
    onHomeClick: () -> Unit = {},
    onRankingClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    scrollBehavior: BottomAppBarScrollBehavior,
) {
    val bottomItems = listOf(
        "Home" to LineAwesomeIcons.HomeSolid,
        "Ranking" to LineAwesomeIcons.AwardSolid,
        "Settings" to LineAwesomeIcons.CogSolid
    )
    FlexibleBottomAppBar(
        modifier = Modifier
            .fillMaxWidth(),
        scrollBehavior = scrollBehavior,
        windowInsets = BottomAppBarDefaults.windowInsets
    ) {
        // Estado para controlar o item selecionado de acordo com o índice da lista
        var selectedIndex by remember { mutableIntStateOf(0) }
        bottomItems.forEachIndexed { index, item ->
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clickable { selectedIndex = index },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(
                            if (selectedIndex == index) MaterialTheme.colorScheme.primary.copy(0.6f)
                            else Color.Transparent,
                            shape = MaterialTheme.shapes.medium
                        ),
                    contentAlignment = Alignment.Center
                ){}

                IconButton(
                    onClick = {
                        when (item.first) {
                            "Home" -> onHomeClick()
                            "Ranking" -> onRankingClick()
                            "Settings" -> onSettingsClick()
                        }
                        selectedIndex = index
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(MaterialTheme.shapes.medium)
                ) {
                    Icon(
                        imageVector = item.second,
                        contentDescription = null,
                        tint = if (selectedIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            }

            if (item != bottomItems.last()) {
                VerticalDivider(
                    modifier = Modifier
                        .padding(vertical = 24.dp)
                )
            }
        }
    }
}
