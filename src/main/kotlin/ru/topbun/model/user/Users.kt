package ru.topbun.model.user

import io.ktor.http.*
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.utills.AppException
import ru.topbun.utills.Error

object Users: IntIdTable("users") {
    private val username = text("username")
    private val email = varchar("email", 255).uniqueIndex()
    private val password = text("password")
    private val isAdmin = bool("isAdmin").default(false)

    fun getUser(email: String): UserDTO? = transaction { selectAll().where { Users.email eq email }.firstOrNull()?.toUser() }
    fun containsUser(email: String): Boolean = getUser(email) != null
    fun getUserOrThrow(email: String): UserDTO = getUser(email) ?: throw AppException(HttpStatusCode.NotFound, Error.USER_NOT_FOUND)
    fun insertUser(username: String, email: String, password: String){
        transaction { insert {
            it[Users.username] = username
            it[Users.email] = email
            it[Users.password] = password
        } }
    }

    fun addUser(user: UserDTO){
        transaction { insert {
            it[Users.id] = user.id
            it[Users.username] = user.username
            it[Users.email] = user.email
            it[Users.password] = user.password
            it[Users.isAdmin] = user.isAdmin
        } }
    }

    fun ResultRow.toUser(): UserDTO {
        return UserDTO(
            id = this[Users.id].value,
            username = this[username],
            email = this[email],
            password = this[password],
            isAdmin = this[isAdmin],
        )
    }

}