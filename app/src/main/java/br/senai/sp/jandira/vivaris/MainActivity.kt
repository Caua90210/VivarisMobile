package br.senai.sp.jandira.vivaris

import Cadastro
import Login
import PerfilPsicologo
import PreferenciasScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import br.senai.sp.jandira.vivaris.screens.AddCartao
import br.senai.sp.jandira.vivaris.screens.Configuracoes
import br.senai.sp.jandira.vivaris.screens.Home
import br.senai.sp.jandira.vivaris.screens.PagamentoScreen
import br.senai.sp.jandira.vivaris.screens.PerfilCliente
import br.senai.sp.jandira.vivaris.screens.PsicologoPesquisa
import br.senai.sp.jandira.vivaris.screens.SplashScreen
import br.senai.sp.jandira.vivaris.screens.videoCall
import br.senai.sp.jandira.vivaris.security.TokenRepository
import br.senai.sp.jandira.vivaris.service.PagamentoService
import br.senai.sp.jandira.vivaris.service.RetrofitFactory
import br.senai.sp.jandira.vivaris.ui.theme.VivarisTheme



class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VivarisTheme {
                val context = LocalContext.current
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val controleDeNavegacao = rememberNavController()
                    NavHost(navController = controleDeNavegacao, startDestination = "splash") {

                        val tokenRepository = TokenRepository(context)



                        // Splash Screen
                        composable(route = "splash") {
                            SplashScreen(navController = controleDeNavegacao)
                        }

                        // Login Screen
                        composable(route = "login") {
                            Login(controleDeNavegacao)
                        }

                        // Cadastro Screen
                        composable(route = "cadastro") {
                            Cadastro(controleDeNavegacao)
                        }

                        composable(
                            route = "home/{id}/{isPsicologo}/{nome}",
                            arguments = listOf(
                                navArgument("id") { type = NavType.IntType },
                                navArgument("isPsicologo") { type = NavType.BoolType },
                                navArgument("nome") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val userId = backStackEntry.arguments?.getInt("id") ?: 0
                            val isPsicologo = backStackEntry.arguments?.getBoolean("isPsicologo") ?: false
                            val nomeUsuario = backStackEntry.arguments?.getString("nome") ?: ""

                            Home(
                                controleDeNavegacao = controleDeNavegacao,
                                userId = userId,
                                isPsicologo = isPsicologo,
                                nomeUsuario = nomeUsuario
                            )
                        }

                        composable("disponibilidade/{idPsicologo}") { backStackEntry ->
                            val idPsicologo = backStackEntry.arguments?.getString("idPsicologo")?.toIntOrNull()
                            if (idPsicologo != null) {
                                DisponibilidadeScreenV4(controleDeNavegacao, idPsicologo)
                            }
                        }

                        composable(
                            "preferencias/{clienteId}",
                            deepLinks = listOf(navDeepLink { uriPattern = "android-app://br.senai.sp.jandira.vivaris/preferencias/{clienteId}" })
                        ) { backStackEntry ->
                            val clienteId = backStackEntry.arguments?.getString("clienteId")?.toIntOrNull()
                            PreferenciasScreen(controleDeNavegacao, clienteId ?: 0)
                        }

                        composable(route = "configuracoes") {
                            Configuracoes(
                                controleDeNavegacao,
                                clearData = {

                                    tokenRepository.clearData()

                                }
                            )
                        }

                        composable(route = "addcartao"){
                            AddCartao()
                        }

                        composable(
                            route = "pesquisapsicologo/{isPsicologo}",
                            arguments = listOf(
                                navArgument("isPsicologo") { type = NavType.BoolType }
                            )
                        ) { backStackEntry ->
                            val isPsicologo = backStackEntry.arguments?.getBoolean("isPsicologo") ?: false
                            PsicologoPesquisa(
                                controleDeNavegacao = controleDeNavegacao,
                                isPsicologo = isPsicologo
                            )
                        }


                        composable(
                            route = "perfilcliente/{id}",
                            arguments = listOf(navArgument("id") {
                                type = NavType.IntType
                            })
                        ){  backStackEntry ->
                            val id = backStackEntry.arguments?.getInt("id")
                            if (id != null){
                                PerfilCliente(controleDeNavegacao, id)
                            }

                        }

                        composable(
                            route = "perfilpsicologo/{id}?isPsicologo={isPsicologo}",
                            arguments = listOf(
                                navArgument("id") { type = NavType.IntType },
                                navArgument("isPsicologo") { type = NavType.BoolType; defaultValue = false } // Set a default value
                            )
                        ) { backStackEntry ->
                            val isPsicologo = backStackEntry.arguments?.getBoolean("isPsicologo") ?: false
                            val id = backStackEntry.arguments?.getInt("id")
                            if (id != null) {
                                PerfilPsicologo(controleDeNavegacao, id, isPsicologo = isPsicologo)
                            }
                        }
                        composable(
                            route = "pagamento/{sessionId}",
                            arguments = listOf(navArgument("sessionId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val sessionId = backStackEntry.arguments?.getString("sessionId") ?: ""
                            val pagamentoService = RetrofitFactory(context).getPagamentoService()

                            PagamentoScreen(sessionId = sessionId, pagamentoService = pagamentoService)
                        }

                        composable(route = "videochamada"){
                            videoCall(modifier = Modifier, controleDeNavegacao)
                        }

                    }
                }
            }
        }
    }
}
