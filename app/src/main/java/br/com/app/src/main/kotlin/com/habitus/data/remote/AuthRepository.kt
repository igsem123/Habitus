package br.com.app.src.main.kotlin.com.habitus.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    val auth: FirebaseAuth
) {

    fun createAccountWithEmail(email: String, password: String): Flow<AuthResponse> = callbackFlow {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(AuthResponse.Success(auth.currentUser))
                } else {
                    trySend(AuthResponse.Error(message = task.exception?.message ?: "" ))
                }
            }

        awaitClose()
    }

    fun loginWithEmail(email: String, password: String): Flow<AuthResponse> = callbackFlow {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(AuthResponse.Success(auth.currentUser))
                } else {
                    trySend(AuthResponse.Error(message = task.exception?.message ?: "" ))
                }
            }

        awaitClose()
    }

    fun addAuthStateListener(function: (FirebaseUser?) -> Unit) {
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            function(user)
        }
    }
}

interface AuthResponse {
    data class Success(val user: FirebaseUser?): AuthResponse
    data class Error(val message: String): AuthResponse
}