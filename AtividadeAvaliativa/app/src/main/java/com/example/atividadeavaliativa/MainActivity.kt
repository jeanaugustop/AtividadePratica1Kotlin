package com.example.atividadeavaliativa

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLayout(){

}

@Composable
fun TelaInicial(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize().padding(15.dp)) {
        Button(onClick = {
            navController.navigate("cadastroProduto")  // Navega para a tela de cadastro
        }) {
            Text(text = "Cadastro")
        }

        Spacer(modifier = Modifier.height(15.dp))

        Button(onClick = {
            navController.navigate("telaLista")  // Navega para a tela de lista
        }) {
            Text(text = "Lista")
        }
    }
}


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CadastroProduto(navController: NavHostController) {

        var nome by remember { mutableStateOf("")}
        var categoria by remember { mutableStateOf("")}
        var preco by remember { mutableStateOf("")}
        var estoque by remember { mutableStateOf("")}
        var context = LocalContext.current

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center){

            Text(text = "CADASTRO DE PRODUTO")

            Spacer(modifier = Modifier.height(15.dp))

            TextField(value = nome ,
                onValueChange = {nome = it},
                label = { Text(text = "Nome do Produto:",
                    modifier = Modifier.fillMaxWidth())})

            Spacer(modifier = Modifier.height(15.dp))

            TextField(value = categoria ,
                onValueChange = {categoria = it},
                label = { Text(text = "Categoria:")},
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(15.dp))
            TextField(value = preco ,
                onValueChange = {preco = it},
                label = { Text(text = "Preço:",
                    modifier = Modifier.fillMaxWidth())},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))

            Spacer(modifier = Modifier.height(15.dp))

            TextField(value = estoque,
                onValueChange = {estoque = it},
                label = { Text(text = "Estoque:")},
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(15.dp))

            Button(onClick = {
                val precoDouble = preco.toDoubleOrNull()
                val estoqueInt = estoque.toIntOrNull()

                if (nome.isEmpty() || categoria.isEmpty() || preco.isEmpty() || estoque.isEmpty()) {
                    Toast.makeText(context, "Todos os campos são obrigatórios", Toast.LENGTH_LONG).show()
                } else if (precoDouble == null || precoDouble <= 0.0) {
                    Toast.makeText(context, "O preço deve ser um número maior que zero", Toast.LENGTH_LONG).show()
                } else if (estoqueInt == null || estoqueInt <= 0) {
                    Toast.makeText(context, "A quantidade em estoque deve ser um número maior que zero", Toast.LENGTH_LONG).show()
                } else {
                    Estoque.adicionarProduto(
                        Produto(nome, categoria, precoDouble, estoqueInt)
                    )
                    Toast.makeText(context, "Produto cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                    navController.navigate("telaLista")  // Navega de volta para a tela de lista após cadastrar
                }
            }) {
                Text("Cadastrar")
            }


        }
    }

    @Composable
    fun AppNavigation() {
        val navController = rememberNavController()

        // NavHost define as rotas e o ponto de entrada
        NavHost(navController = navController, startDestination = "Inicial") {
            composable("Inicial") { TelaInicial(navController) }
            composable("cadastroProduto") { CadastroProduto(navController) }
            composable("telaLista") { TelaLista(navController) }
            composable("DetalheProdutos/{produtoJson}") { backStackEntry ->
                val produtoJson = backStackEntry.arguments?.getString("produtoJson")
                val produto = Gson().fromJson(produtoJson, Produto::class.java)
                DetalheProdutos(navController, produto)
            }
            composable("LayoutEstatistica"){LayoutEstatistica(navController)}
        }
    }

        @Composable
        fun TelaLista(navController: NavHostController) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text(text = "LISTA DE PRODUTOS")

                LazyColumn {
                    items(Estoque.produtos) { produto ->
                        Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                            Text(text = "${produto.Nome} (${produto.Estoque} unidades)")
                            Button(onClick = {
                                val produtoJson = Gson().toJson(produto)
                                navController.navigate("DetalheProdutos/$produtoJson")
                            }) {
                                Text("Detalhes")
                            }
                        }
                    }
                }
            }
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom)
            {
                Button(onClick = {

                    navController.popBackStack()

                }) {
                    Text(text = "Voltar")
                }
                Button(onClick = {
                    navController.navigate("LayoutEstatistica")
                }) {
                    Text("Estatistica")
                }
            }
        }


@Composable
fun DetalheProdutos(navController: NavController, produto: Produto) {

    Column(
        Modifier
            .fillMaxSize()
            .padding(15.dp)) {

        Text(text = "DETALHES DO PRODUTO", fontSize = 22.sp,
            modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(15.dp))

        Text(text = "Nome: ${produto.Nome}\n" +
                "Categoria: ${produto.Categoria}\n" +
                "Preco: ${produto.Preco}\n" +
                "Estoque: ${produto.Estoque}" ,
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(15.dp))

        Button(onClick = {

            navController.popBackStack()

        }) {
            Text(text = "Voltar")
        }


    }

}

@Composable
fun LayoutEstatistica(navController: NavController) {

    Column(
        Modifier
            .fillMaxSize()
            .padding(15.dp)) {

        Text(text = "ESTATISTICA TOTAL", fontSize = 22.sp,
            modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(15.dp))

        Text(text = "Valor Estoque Total: ${Estoque.calcularValorTotalEstoque()}\n" +
                "Quantidade de Estoque Total: ${Estoque.calcularQuantidadeTotalEstoque()}\n",
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(15.dp))

        Button(onClick = {

            navController.popBackStack()

        }) {
            Text(text = "Voltar")
        }

    }

}

