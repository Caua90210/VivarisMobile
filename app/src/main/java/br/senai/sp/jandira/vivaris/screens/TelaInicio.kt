package br.senai.sp.jandira.vivaris.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.sqlite.db.SupportSQLiteCompat.Api16Impl.deleteDatabase
import br.senai.sp.jandira.vivaris.R
import br.senai.sp.jandira.vivaris.security.DatabaseHelper
import br.senai.sp.jandira.vivaris.security.TokenRepository
import kotlinx.coroutines.delay

@SuppressLint("RestrictedApi")
@Composable
fun SplashScreen(navController: NavHostController) {
    val infiniteTransition = rememberInfiniteTransition()
    val context = LocalContext.current

    val tokenRepository = TokenRepository(context)

    // Novo gradiente dinâmico
    val dynamicGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF4EB8A1),  // Verde claro
            Color(0xFF78E5B1),  // Verde pastel
            Color(0xFF23B991),  // Verde dominante
            Color(0xFF1F9B83)   // Verde um pouco mais escuro
        ),
        start = Offset.Zero,
        end = Offset.Infinite
    )

    // Animação de movimento para o fundo
    val animatedOffset = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 500f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Animações para a escala e rotação do logo
    val scaleAnimation = infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val rotationAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Efeito de digitação para o nome do aplicativo
    val appName = "VIVARIS"
    var typedText by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        for (i in appName.indices) {
            delay(150)
            typedText = appName.substring(0, i + 1)
        }
    }


    // Estado para controlar a navegação
    var isNavigating by remember { mutableStateOf(false) }

    // Verificação do token em um LaunchedEffect separado
    LaunchedEffect(Unit) {
        delay(3000)
        val token = tokenRepository.getToken()
        if (token != null) {
            val userId = tokenRepository.getUserId()
            val isPsicologo = tokenRepository.getIsPsicologo()
            val userName = tokenRepository.getUserName()

            // Verifica se o userId é nulo
            if (userId != null) {
                Log.d("Dados passados", "$userId, $isPsicologo, $userName ")

                navController.navigate("home/$userId/$isPsicologo/$userName") {
                    popUpTo("splash") { inclusive = true }
                }
            } else {
                // Limpa os dados do token se o userId não for encontrado
                tokenRepository.clearData()
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        } else {
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    // UI da Tela de Splash
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Colocando o gradiente dinâmico no fundo
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(dynamicGradient)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.vivarislogo),
                contentDescription = "Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .scale(scaleAnimation.value)
                    .rotate(rotationAnimation.value)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = typedText,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}