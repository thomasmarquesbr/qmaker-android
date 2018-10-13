package com.qmakercorp.qmaker.data.model

class Quiz {

    var id: String? = null
    var name: String? = null
    var code: String? = null
    var questions = mutableListOf<Question>()

    constructor(id: String, name: String, code: String) {
        this.id = id
        this.name = name
        this.code = code
    }

    constructor()

}