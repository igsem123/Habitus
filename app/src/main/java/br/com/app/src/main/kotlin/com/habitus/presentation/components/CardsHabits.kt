package br.com.app.src.main.kotlin.com.habitus.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.app.src.main.kotlin.com.habitus.ui.theme.azulMarinho
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.CheckSolid
import compose.icons.lineawesomeicons.PlusSolid

@Composable
fun CardHabits(
    isChecked: Boolean,
    checkedHabits: MutableState<Set<String>>,
    habit: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .shadow(
                elevation = if (isChecked) 0.dp else 0.5.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = if (isChecked) Color(0xFFDFF2E1) else MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                checkedHabits.value = if (isChecked)
                    checkedHabits.value - habit
                else
                    checkedHabits.value + habit
            }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(habit, fontSize = 16.sp)
            Icon(
                imageVector = if (isChecked) LineAwesomeIcons.CheckSolid else LineAwesomeIcons.PlusSolid,
                contentDescription = null,
                tint = if (isChecked) Color.White else azulMarinho,
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = if (isChecked) Color(0xFF4CAF50) else Color.White,
                        shape = CircleShape
                    )
                    .padding(4.dp)
            )
        }
    }
}