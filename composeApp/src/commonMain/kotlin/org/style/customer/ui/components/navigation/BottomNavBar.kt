import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Calendar
import compose.icons.feathericons.Home
import compose.icons.feathericons.Search
import compose.icons.feathericons.User


sealed class BottomNavItem(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : BottomNavItem("Home", FeatherIcons.Home) // Use FeatherIcons
    object Search : BottomNavItem("Search", FeatherIcons.Search)
    object Appointments : BottomNavItem("Appointments", FeatherIcons.Calendar)
    object Profile : BottomNavItem("Profile", FeatherIcons.User)
}

@Composable
fun BottomNavBar(
    selected: Int,
    onTabSelected: (Int) -> Unit
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Appointments,
        BottomNavItem.Profile
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 0.dp,
            modifier = Modifier
                .height(56.dp)
                .background(Color.White)
                .padding(bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding())
                .drawTopBorder(1.dp, Color(0x1A000000))
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = selected == index,
                    onClick = { onTabSelected(index) },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if (selected == index) Color(0xFF007AFF) else Color(0xFF8E8E93),
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = item.label,
                            fontSize = 10.sp,
                            color = if (selected == index) Color(0xFF007AFF) else Color(0xFF8E8E93)
                        )
                    },
                    alwaysShowLabel = false
                )
            }
        }
    }
}

fun Modifier.drawTopBorder(thickness: Dp, color: Color): Modifier =
    this.then(
        drawBehind {
            drawRect(
                color = color,
                topLeft = androidx.compose.ui.geometry.Offset(0f, 0f),
                size = androidx.compose.ui.geometry.Size(size.width, thickness.toPx())
            )
        }
    )