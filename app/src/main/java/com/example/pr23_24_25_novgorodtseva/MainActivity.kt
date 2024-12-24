package com.example.pr23_24_25_novgorodtseva

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pr23_24_25_novgorodtseva.ui.theme.Pr23_24_25_novgorodtsevaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Pr23_24_25_novgorodtsevaTheme {
                val navController = rememberNavController()
                Surface(color = Color.White) {
                    Scaffold(
                        bottomBar = {

                            if (navController.currentBackStackEntryAsState().value?.destination?.route != "onboarding") {
                                BottomNavigationBar(navController = navController)
                            }
                        },
                        content = { padding ->
                            NavHostContainer(navController = navController, padding = padding)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar(
        containerColor = Color(0xFFFAFAFA)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        Constants.BottomNavItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    navController.navigate(navItem.route)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = navItem.iconResId),
                        contentDescription = navItem.label,
                        modifier = Modifier.size(30.dp),
                        tint = if (currentRoute == navItem.route) Color(0xFF0F9D58) else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = navItem.label,
                        color = if (currentRoute == navItem.route) Color(0xFF0F9D58) else Color.Gray
                    )
                },
                alwaysShowLabel = true
            )
        }
    }
}

@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = "onboarding",
        modifier = Modifier.padding(paddingValues = padding),
    ) {
        composable("onboarding") {
            OnboardingPager(navController)
        }
        composable("Анализы") {
            AnalyzesScreen()
        }
        composable("Результаты") {
            ResultsScreen()
        }
        composable("Поддержка") {
            SupportScreen()
        }
        composable("Профиль") {
            ProfileScreen()
        }
    }
}

@Composable
fun OnboardingPager(navController: NavHostController) {
    val context = LocalContext.current
    var currentPage by remember { mutableStateOf(0) }
    val pages = listOf(
        OnboardingPage("Анализы", "Экспресс сбор и получение проб", R.drawable.page1),
        OnboardingPage("Уведомления", "Вы быстро узнаете о результатах", R.drawable.page2),
        OnboardingPage("Мониторинг", "Наши врачи всегда наблюдают за вашими показателями здоровья", R.drawable.page3)
    )
    val swipeThreshold = 70.dp
    val swipeThresholdPx = with(LocalDensity.current) { swipeThreshold.toPx() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (dragAmount > swipeThresholdPx) {
                        if (currentPage > 0) {
                            currentPage -= 1
                        }
                    }
                    else if (dragAmount < -swipeThresholdPx) {
                        if (currentPage < pages.size - 1) {
                            currentPage += 1
                        }
                        if (currentPage == pages.size - 1) {
                            val intent = Intent(context, LoginActivity::class.java)
                            context.startActivity(intent)
                        }
                    }
                }
            }
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = if (currentPage == 2) "Завершить" else "Пропустить",
                color = Color(0xFF42A5F5),
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.Top)
                    .padding(start = 16.dp)
                    .clickable {
                        if (currentPage == 2) {
                            val intent = Intent(context, LoginActivity::class.java)
                            context.startActivity(intent)
                        } else {
                            currentPage = 2
                        }
                    }
            )
            Image(
                painter = painterResource(id = R.drawable.buttonadd),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.Top)
            )
        }

        Spacer(modifier = Modifier.height(50.dp))
        val onboardingPage = pages[currentPage]

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = onboardingPage.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color(0xFF4CAF50)
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = onboardingPage.description,
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(50.dp))
            PagerIndicator(pages.size, currentPage)

            Spacer(modifier = Modifier.height(40.dp))
            Image(
                painter = painterResource(id = onboardingPage.imageRes),
                contentDescription = null,
                modifier = Modifier.size(300.dp).padding(top = 16.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (currentPage > 0) {
                Text(
                    text = "Назад",
                    color = Color(0xFF42A5F5),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { currentPage -= 1 }
                )
            }
            if (currentPage < pages.size - 1) {
                Text(
                    text = "Далее",
                    color = Color(0xFF42A5F5),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { currentPage += 1 }
                )
            }
        }
    }
}
@Composable
fun PagerIndicator(pagesCount: Int, currentPage: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        for (i in 0 until pagesCount) {
            val color = if (i == currentPage) Color.Blue else Color.LightGray
            Canvas(modifier = Modifier.size(8.dp)) {
                drawCircle(color = color)
            }
        }
    }
}
data class OnboardingPage(
    val title: String,
    val description: String,
    val imageRes: Int
)