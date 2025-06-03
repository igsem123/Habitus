package br.com.app.src.main.kotlin.com.habitus.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirebaseAuth @Inject constructor(
    val auth: FirebaseAuth
) {

    fun createAccountWithEmail(email: String, password: String): Flow<AuthResponde> = callbackFlow {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(AuthResponde.Success(auth.currentUser))
                } else {
                    trySend(AuthResponde.Error(message = task.exception?.message ?: "" ))
                }
            }

        awaitClose()
    }

    fun loginWithEmail(email: String, password: String): Flow<AuthResponde> = callbackFlow {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(AuthResponde.Success(auth.currentUser))
                } else {
                    trySend(AuthResponde.Error(message = task.exception?.message ?: "" ))
                }
            }

        awaitClose()
    }
}

interface AuthResponde {
    data class Success(val user: FirebaseUser?): AuthResponde
    data class Error(val message: String): AuthResponde
}