package br.com.app.src.main.kotlin.com.habitus.data.repository

import br.com.app.src.main.kotlin.com.habitus.data.database.HabitusDatabase
import br.com.app.src.main.kotlin.com.habitus.data.entity.UserEntity
import br.com.app.src.main.kotlin.com.habitus.data.remote.AuthRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserRepositoryImpl @Inject constructor(
    private val db: HabitusDatabase,
    auth: AuthRepository
) : UserRepository {

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    override val currentUser: StateFlow<UserEntity?> = _currentUser

    init {
        // Supondo que o FirebaseAuth tenha um listener para mudanças de autenticação
        auth.addAuthStateListener { firebaseUser ->
            if (firebaseUser != null) {
                // Busque o usuário no banco de dados usando o uid do Firebase
                CoroutineScope(Dispatchers.IO).launch {
                    val user = db.userDao().getUserById(firebaseUser.uid)
                    _currentUser.value = user
                }
            } else {
                _currentUser.value = null
            }
        }
    }

    override suspend fun insertUser(user: UserEntity) {
        db.userDao().insertUser(user)
    }

    override suspend fun getUserById(userId: String): UserEntity? {
        return db.userDao().getUserById(userId)
    }

    override suspend fun updateUser(user: UserEntity) {
        db.userDao().updateUser(user)
    }

    override suspend fun deleteUser(user: UserEntity) {
        db.userDao().deleteUser(user)
    }
}