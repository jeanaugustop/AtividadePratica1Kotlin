package com.example.atividadeavaliativa

class Estoque {
    companion object {
        val produtos = mutableListOf<Produto>()

        fun adicionarProduto(produto: Produto) {
            produtos.add(produto)
        }

        fun calcularValorTotalEstoque():Double{

            var valorEstoqueTotal = 0.0

            produtos.forEach{
                it -> valorEstoqueTotal += it.Preco * it.Estoque
            }

            return valorEstoqueTotal

        }

        fun calcularQuantidadeTotalEstoque():Int{

            var valorQuantidadeTotal = 0

            produtos.forEach{
                    it -> valorQuantidadeTotal += it.Estoque
            }

            return valorQuantidadeTotal

        }

    }
}
