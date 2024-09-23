package com.example.mainactivity

class Estoque {
        companion object {
            var produtos: MutableList<Produto> = mutableListOf()
            fun adicionarProduto(produto: Produto) {
                produtos.add(produto)
            }
        }
    }
