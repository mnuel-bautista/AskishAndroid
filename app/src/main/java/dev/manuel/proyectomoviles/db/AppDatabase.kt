package dev.manuel.proyectomoviles.db

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dev.manuel.proyectomoviles.BuildConfig

class AppDatabase {

    val firestore = Firebase.firestore.apply {
        val host = BuildConfig.HOST
        val port = BuildConfig.PORT

        if(host != "" && port != 0) {
            useEmulator(host, port)
            val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build()
            firestoreSettings = settings
        }
    }

    val auth = Firebase.auth.apply {
        val host = BuildConfig.HOST
        if(host != "") {
            FirebaseAuth.getInstance().useEmulator(host, 9099)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = AppDatabase()
                    }
                }
            }
            return INSTANCE
        }
    }
}