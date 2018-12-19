package com.mingmin.practice

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

data class Demo(val id: Int, val title: String, val desc: String) {
    companion object {
        fun getDemoList(): ArrayList<Demo> {
            val mapper = jacksonObjectMapper()
            return mapper.readValue(demoJson)
        }

        fun getVersionList(): ArrayList<String> {
            return arrayListOf("Cupcake", "Donut", "Eclair", "Froyo", "Gingerbread", "Honeycomb",
                    "Icecream Sandwich", "Jelly Bean", "Kitkat", "Lollipop", "Marshmallow",
                    "Nougat", "Oreo", "Pie")
        }
    }
}

private val demoJson = """
    [
        {
            "id": 0,
            "title": "Bottom Sheet",
            "desc": "Bottom sheet state switch, FAB animation, Text Drawable"
        },
        {
            "id": 1,
            "title": "Transparent Toolbar",
            "desc": "Overlay transparent toolbar and statusbar"
        },
        {
            "id": 2,
            "title": "Rating Bar",
            "desc": "Custom Rating Bar icon, Chip single choice"
        },
        {
            "id": 3,
            "title": "Firestore",
            "desc": "Refactor Friendly Eats example of codelab with kotlin,<br>Firestore, FirebaseUI, DataBinding and Lifecycle-aware.<br>Solve filter dialog rotation issue.<br><b><font color='red'>User: demo@mail.com, password: 123456</font></b>"
        },
        {
            "id": 4,
            "title": "Retrofit",
            "desc": "Display wiki search count by retrofit<br>reference: Medium retrofit tutorial by Elye"
        }
    ]
"""