package com.example.service.constants

class DataBaseConstants {
    /**
     * TABLEAS DISPONÍVEIS NO BANCO DE DADOS COM SUAS COLUNAS
     */
    object GUEST {
        const val TABLE_NAME = "Guest"

        // CRIO UM OBJETO DENTRO DO OBJETO TABELA, PORQUE O OBJETO COLUNAS FAZ PARTE DE TABLEAS
        // UM NÃO EXISTE SEM O OUTRO
        object COLUMNS {
            const val ID = "id"
            const val NAME = "name"
            const val PRESENCE = "presence"
        }
    }
}