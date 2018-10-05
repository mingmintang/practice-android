package com.mingmin.materialdesign

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

data class Demo(val title: String, val desc: String) {
    companion object {
        fun getDemoList(): ArrayList<Demo> {
            val mapper = jacksonObjectMapper()
            return mapper.readValue(demoJson)
        }
    }
}

private val demoJson = """
    [
        {
            "title": "FAB &amp; Toolbar Animation",
            "desc": "G+ like on scroll hide animation"
        },
        {
            "title": "Transparent Toolbar Test",
            "desc": "Overlaid transparent Toolbar"
        },
        {
            "title": "Navigation Drawer",
            "desc": "Material Design drawer with scrim"
        },
        {
            "title": "Toolbar Animation with Image",
            "desc": "Using the new Support Design Library"
        },
        {
            "title": "Material Style Tabs",
            "desc": "Tab strip with Design Support Lib"
        },
        {
            "title": "Card View Toolbar",
            "desc": "Nested Toolbar in card"
        },
        {
            "title": "Quick Return Pattern",
            "desc": "UI Pattern using Design Support Lib"
        },
        {
            "title": "Reveal Animation",
            "desc": "Circular Reveal for pre Lollipop"
        },
        {
            "title": "Gmail Style List with Bottom Sheet",
            "desc": "Colored list icons with Bottom Sheets Google Maps style"
        },
        {
            "title": "Introductory View Pager",
            "desc": "Google style product tour"
        },
        {
            "title": "Bottom Navigation Bar",
            "desc": "Using Design Support Library's BottomNavigationView"
        }
    ]
"""

