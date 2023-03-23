enum class Nivel { BASICO, INTERMEDIARIO, DIFICIL }

class Usuario {
    val formacoesConcluidas = mutableListOf<Formacao>()

    fun inscreverEmFormacao(formacao: Formacao) {
        formacao.matricular(this)
    }

    fun marcarConteudoComoConcluido(formacao: Formacao, conteudo: ConteudoEducacional) {
        formacao.marcarConteudoConcluido(this, conteudo)
    }

    fun acessarHistoricoFormacoes(): List<Formacao> {
        return formacoesConcluidas.toList()
    }
}

data class ConteudoEducacional(var nome: String, val duracao: Int = 60, var concluido: Boolean = false)

data class Formacao(val nome: String, var conteudos: List<ConteudoEducacional>, val nivel: Nivel) {
    val inscritos = mutableListOf<Usuario>()
    val conteudosConcluidos = mutableMapOf<Usuario, MutableList<ConteudoEducacional>>()

    fun matricular(usuario: Usuario) {
        inscritos.add(usuario)
        conteudosConcluidos[usuario] = mutableListOf()
    }

    fun marcarConteudoConcluido(usuario: Usuario, conteudo: ConteudoEducacional) {
        conteudosConcluidos[usuario]?.add(conteudo)
        conteudo.concluido = true

        if (usuario.conteudosConcluidos(this) == conteudos.size) {
            usuario.formacoesConcluidas.add(this)
        }
    }

    fun acessarProgresso(usuario: Usuario): Map<ConteudoEducacional, Boolean> {
        val progresso = mutableMapOf<ConteudoEducacional, Boolean>()
        conteudos.forEach { conteudo ->
            progresso[conteudo] = conteudo in conteudosConcluidos[usuario] ?: emptyList()
        }
        return progresso
    }
    
    fun acessarInscritos(): List<Usuario> {
        return inscritos.toList()
    }
}

fun Usuario.conteudosConcluidos(formacao: Formacao): Int {
    return formacao.conteudosConcluidos[this]?.size ?: 0
}
