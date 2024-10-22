package br.senai.sp.jandira.vivaris.screens

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.senai.sp.jandira.vivaris.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    val infiniteTransition = rememberInfiniteTransition()

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

    // Navegar para a tela de login após 5 segundos
    LaunchedEffect(Unit) {
        delay(5000)
        navController.navigate("login")
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
            // Imagem do logo com animações de escala e rotação
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

            // Texto do nome do aplicativo com animação de digitação
            Text(
                text = typedText,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
