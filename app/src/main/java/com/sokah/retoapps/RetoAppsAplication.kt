package com.sokah.retoapps

import android.app.Application

class RetoAppsAplication :Application() {

    companion object{

        lateinit var prefs :SharedPreferences
    }
    override fun onCreate() {
        super.onCreate()

        prefs=SharedPreferences(applicationContext)
        prefs.createUser()
    }
}