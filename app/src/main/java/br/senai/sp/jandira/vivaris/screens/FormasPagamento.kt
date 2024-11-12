package br.senai.sp.jandira.vivaris.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.senai.sp.jandira.vivaris.R
import br.senai.sp.jandira.vivaris.model.CartaoResponse
import br.senai.sp.jandira.vivaris.model.Cartoes
import br.senai.sp.jandira.vivaris.service.RetrofitFactory
import retrofit2.Call
import java.util.Date

@Composable
fun AddCartao() {
    var numeroCartao by remember { mutableStateOf("") }
    var nome by remember { mutableStateOf("") }
    var mesValidade by remember { mutableStateOf("") }
    var anoValidade by remember { mutableStateOf("") }
    var cvc by remember { mutableStateOf("") }

    val context = LocalContext.current
    val retrofitFactory = RetrofitFactory()
    val cartaoService = retrofitFactory.getCartoesService()

    var selectedTab by remember { mutableStateOf("Debito") } // Sem acento
    var saveCardChecked by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color(0xFF3E9C81)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.vivarislogo),
                        contentDescription = "Logo Vivaris",
                        modifier = Modifier.size(40.dp),
                        contentScale = ContentScale.Fit
                    )

                    Text(
                        text = "Cartões Salvos",
                        textAlign = TextAlign.Center,
                        modifier = Modifier,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Toggle between Débito and Crédito
            Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Débito",
                        modifier = Modifier
                            .clickable {
                                selectedTab = "Debito" // Sem acento
                                Log.d("AddCartao", "Selected Tab: $selectedTab") // Log
                            }
                            .background(
                                if (selectedTab == "Debito") Color(0xFF3E9C81) else Color.Transparent,
                                RoundedCornerShape(16.dp)
                            )
                            .padding(8.dp),
                        color = if (selectedTab == "Debito") Color.White else Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Crédito",
                        modifier = Modifier
                            . clickable {
                                selectedTab = "Credito" // Sem acento
                                Log.d("AddCartao", "Selected Tab: $selectedTab") // Log
                            }
                            .background(
                                if (selectedTab == "Credito") Color(0xFF3E9C81) else Color.Transparent,
                                RoundedCornerShape(16.dp)
                            )
                            .padding(8.dp),
                        color = if (selectedTab == "Credito") Color.White else Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Card Details
                Text(
                    text = "Dados do cartão",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Card Number Input
                OutlinedTextField(
                    value = numeroCartao,
                    onValueChange = {
                        numeroCartao = it
                        Log.d("AddCartao", "Número do cartão: $numeroCartao") // Log
                    },
                    label = { Text("Número do cartão") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Name Input
                OutlinedTextField(
                    value = nome,
                    onValueChange = {
                        nome = it
                        Log.d("AddCartao", "Nome: $nome") // Log
                    },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Expiry Date and CVV
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = mesValidade,
                        onValueChange = {
                            mesValidade = it
                            Log.d("AddCartao", "Mês de validade: $mesValidade") // Log
                        },
                        label = { Text("Mês") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = anoValidade,
                        onValueChange = {
                            anoValidade = it
                            Log.d("AddCartao", "Ano de validade: $anoValidade") // Log
                        },
                        label = { Text("Ano") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = cvc,
                        onValueChange = {
                            cvc = it
                            Log.d("AddCartao", "CVV: $cvc") // Log
                        },
                        label = { Text("CVV") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Save Card Checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = saveCardChecked,
                        onCheckedChange = {
                            saveCardChecked = it
                            Log.d("AddCartao", "Salvar cartão: $saveCardChecked") // Log
                        }
                    )
                    Text(text = "Salvar cartão para futuros pagamentos")
                }

            }

            Button(
                onClick = {
                    val modalidade = selectedTab
                    val validade = Date()
                    val cartao = Cartoes(
                        id = 0,
                        modalidade = modalidade,
                        numero_cartao = numeroCartao,
                        nome = nome,
                        validade = validade.toString(),
                        cvc = cvc
                    )

                    Log.d("AddCartao", "Dados do cartão a serem enviados: $cartao")
                    // Chamada para cadastrar o cartão
                    cartaoService.cadastrarCartao(cartao).enqueue(object : retrofit2.Callback<CartaoResponse> {
                        override fun onResponse(call: Call<CartaoResponse>, response: retrofit2.Response<CartaoResponse>) {
                            if (response.isSuccessful) {
                                // Cartão cadastrado com sucesso
                                Toast.makeText(context, "Cartão cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                                Log.d("AddCartao", "Cartão cadastrado com sucesso!") // Log
                            } else {
                                // Tratamento de erro
                                Toast.makeText(context, "Erro ao cadastrar cartão!", Toast.LENGTH_SHORT).show()


                                Log.e("AddCartao", "Erro ao cadastrar cartão: ${response.errorBody()?.string()}") // Log de erro
                            }
                        }

                        override fun onFailure(call: Call<CartaoResponse>, t: Throwable) {
                            // Tratamento de falha na chamada
                            Toast.makeText(context, "Falha na chamada: ${t.message}", Toast.LENGTH_SHORT).show()
                            Log.e("AddCartao", "Falha na chamada: ${t.message}") // Log de erro
                        }
                    })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 90.dp)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF67DEBC)),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(text = "Concluir", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFormasPagamento() {
    MaterialTheme {
        AddCartao()
    }
}