package br.senai.sp.jandira.vivaris.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.pm.ShortcutInfoCompat.Surface
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.senai.sp.jandira.vivaris.R

@Composable
fun developing(modifier: Modifier = Modifier, controleNavegacao: NavHostController) {
    Column(
        modifier = Modifier
            .background(color = Color(0xFF3E9C81))
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.vivarislogo),
            contentDescription = "",
            modifier = Modifier
                .size(90.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.vivaristext),
            contentDescription = "",
            modifier = Modifier
                .width(250.dp)
                .height(80.dp)
                .offset(x = 0.dp, y = -30.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.construction),
            contentDescription = "",
            modifier = Modifier
                .size(350.dp)
                .offset(x = 0.dp, y = -20.dp)
        )

        Text(
            text = "Funcionalidade em desenvolvimento...",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .offset(x = 0.dp, y = -20.dp)
        )

        Spacer(modifier = Modifier.height(50.dp))

        Button(
            modifier = Modifier
                .height(70.dp)
                .width(200.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF5F5DC),
                contentColor = Color(0xFF296856),
                disabledContainerColor = Color(0xFFF5F5DC),
                disabledContentColor = Color(0xFF296856)
            ),
            onClick = {
                controleNavegacao.popBackStack()
            }
        ) {
            Text(
                text = "Voltar",
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun prevDeveloping() {
    developing(modifier = Modifier, controleNavegacao = rememberNavController())
}
