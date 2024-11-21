package br.senai.sp.jandira.vivaris

import Cadastro
import DisponibilidadeScreenV3
import Login
import PreferenciasScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import br.senai.sp.jandira.vivaris.screens.AddCartao
import br.senai.sp.jandira.vivaris.screens.Configuracoes
import br.senai.sp.jandira.vivaris.screens.Home
import br.senai.sp.jandira.vivaris.screens.SplashScreen
import br.senai.sp.jandira.vivaris.ui.theme.VivarisTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VivarisTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val controleDeNavegacao = rememberNavController()
                    NavHost(navController = controleDeNavegacao, startDestination = "home/1/false/pedro") {

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
                                navArgument("nome") { type = NavType.StringType }  // Added this for the "nome" argument
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
                                DisponibilidadeScreenV3(controleDeNavegacao, idPsicologo)
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
                           Configuracoes(controleDeNavegacao)
                        }

                        composable(route = "addcartao"){
                            AddCartao()
                        }


                    }
                }
            }
        }
    }
}
