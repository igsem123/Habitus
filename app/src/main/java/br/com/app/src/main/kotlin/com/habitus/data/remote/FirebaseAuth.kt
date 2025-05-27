package br.com.app.src.main.kotlin.com.habitus.data.remote

import com.google.firebase.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {

}

@Preview
@Composable
private fun LoginPreview() {
    Authenti
}

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

        awaitClose()
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

        awaitClose()
    }
}

interface AuthResponde {
    object Success: AuthResponde
    data class Error(val message: String): AuthResponde
}