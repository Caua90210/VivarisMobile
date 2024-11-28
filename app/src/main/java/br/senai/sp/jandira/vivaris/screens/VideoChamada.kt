package br.senai.sp.jandira.vivaris.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import br.senai.sp.jandira.vivaris.R

@Composable
fun videoCall(modifier: NavHostController, controleNavegacao: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xD0D0D0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Spacer(modifier = modifier.width(10.dp))
            Image(
                painter = painterResource(
                    id = R.drawable.seta_esquerda,
                ),
                modifier = Modifier
                    .width(25.dp)
                    .height(30.dp)
                    .clickable {
                        controleNavegacao.navigate("videochamada")
                    },
                contentDescription = ""
            )
            Spacer(modifier = modifier.width(40.dp))
            Column(
                modifier = Modifier
                    .width(250.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(
                        id = R.drawable.vivarislogo
                    ),
                    contentDescription = "",
                    modifier = Modifier.size(60.dp)
                )
                Row(
                    modifier = Modifier.height(100.dp)
                ) {
                    Text(
                        text = "Em chamada com",
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.width(2.dp))

                    Text(
                        text = "Giovana Costa",
                        color =  Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        Column(
            modifier = Modifier
                .width(380.dp)
                .offset(x = 6.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp))
                .background(color = Color.Red)
                .height(610.dp)
        ) {}
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 16.dp))
                .background(color = Color(0xFF3E9C81))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.camera
                ),
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp)
                    .clickable {},
                contentDescription = "",
            )

            Image(
                painter = painterResource(
                    id = R.drawable.video
                ),
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp)
                    .clickable {},
                contentDescription = "",
            )

            Image(
                painter = painterResource(
                    id = R.drawable.microfone
                ),
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp)
                    .clickable {},
                contentDescription = "",
            )

            Image(
                painter = painterResource(
                    id = R.drawable.desligar
                ),
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp)
                    .clickable {},
                contentDescription = "",
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showSystemUi = true)
@androidx.compose.runtime.Composable
private fun PreviewVideoCall() {
    videoCall()
}