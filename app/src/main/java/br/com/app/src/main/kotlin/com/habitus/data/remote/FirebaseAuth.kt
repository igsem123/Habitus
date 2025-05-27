package br.com.app.src.main.kotlin.com.habitus.data.remote

import kotlinx.coroutines.flow.callbackFlow

class FirebaseAuth {

    private val auth = Firebase.auth

    fun createAccountWithEmail(email: String, password: String): Flow<AuthResponde> = callbackFlow {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSucccessful) {
                    trySend(AuthResponde.Success)
                } else {
                    trySend(AuthResponde.Error(message = task.exception?.message ?: "" ))
                }
            }
    }

    fun loginWithEmail(email: String, password: String): Flow<AuthResponde> = callbackFlow {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSucccessful) {
                    trySend(AuthResponde.Success)
                } else {
                    trySend(AuthResponde.Error(message = task.exception?.message ?: "" ))
                }
            }
    }
}

interface AuthResponde {
    object Success: AuthResponde
    data class Error(val message: String): AuthResponde
}