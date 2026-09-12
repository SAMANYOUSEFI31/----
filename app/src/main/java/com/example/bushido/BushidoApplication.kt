package com.example.bushido

import android.app.Application
import com.example.bushido.data.local.BushidoDatabase
import com.example.bushido.data.repository.BushidoRepository

class BushidoApplication : Application() {
    val database by lazy { BushidoDatabase.getDatabase(this) }
    val repository by lazy { BushidoRepository(database) }
}
