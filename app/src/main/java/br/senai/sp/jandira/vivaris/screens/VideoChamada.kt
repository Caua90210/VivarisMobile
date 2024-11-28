package br.senai.sp.jandira.vivaris.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.senai.sp.jandira.vivaris.R

@Composable
fun videoCall(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xD0D0D0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
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
                    .height(30.dp),
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
                Row {
                    Text(
                        text = "Em chamada com",
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.width(2.dp))

                    Text(
                        text = "Giovana Costa",
                        color =  Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .width(380.dp)
                .offset(x= 6.dp)
                .background(color = Color.Red)
                .height(620.dp)

        ){

        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showSystemUi = true)
@androidx.compose.runtime.Composable
private fun PreviewVideoCall() {
    videoCall()
}