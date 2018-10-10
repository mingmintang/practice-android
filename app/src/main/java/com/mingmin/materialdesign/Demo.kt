package com.mingmin.materialdesign

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
        }
    ]
"""

private val demoJsonBak = """
    [
        {
            "id": 0,
            "title": "FAB &amp; Toolbar Animation",
            "desc": "G+ like on scroll hide animation"
        },
        {
            "id": 1,
            "title": "Transparent Toolbar Test",
            "desc": "Overlaid transparent Toolbar"
        },
        {
            "id": 2,
            "title": "Navigation Drawer",
            "desc": "Material Design drawer with scrim"
        },
        {
            "id": 3,
            "title": "Toolbar Animation with Image",
            "desc": "Using the new Support Design Library"
        },
        {
            "id": 4,
            "title": "Material Style Tabs",
            "desc": "Tab strip with Design Support Lib"
        },
        {
            "id": 5,
            "title": "Card View Toolbar",
            "desc": "Nested Toolbar in card"
        },
        {
            "id": 6,
            "title": "Quick Return Pattern",
            "desc": "UI Pattern using Design Support Lib"
        },
        {
            "id": 7,
            "title": "Reveal Animation",
            "desc": "Circular Reveal for pre Lollipop"
        },
        {
            "id": 8,
            "title": "Gmail Style List with Bottom Sheet",
            "desc": "Colored list icons with Bottom Sheets Google Maps style"
        },
        {
            "id": 9,
            "title": "Introductory View Pager",
            "desc": "Google style product tour"
        },
        {
            "id": 10,
            "title": "Bottom Navigation Bar",
            "desc": "Using Design Support Library's BottomNavigationView"
        }
    ]
"""

