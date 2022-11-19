package dev.manuel.proyectomoviles.db

import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AppDatabase {

    val firestore = Firebase.firestore.apply {
        useEmulator("192.168.0.27", 8080)
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()
        firestoreSettings = settings
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