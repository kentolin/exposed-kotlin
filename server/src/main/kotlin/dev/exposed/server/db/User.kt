package dev.exposed.server.db

import dev.exposed.server.database.DatabaseFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

// Domain model
data class User(val id: Int, val name: String)

// DTO for serialization
@Serializable
data class UserDTO( val id: Int, val name: String)

// Table definition
object Users : IntIdTable() {
    val name = varchar("name", 255)
}

// Entity
class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(Users)
    var name by Users.name

    fun toUser() = User(id.value, name)
}

// Mapper
object UserMapper {
    fun toDTO(user: User): UserDTO = UserDTO(user.id, user.name)
    fun toUser(dto: UserDTO): User = User(dto.id, dto.name)
    fun toDTOs(users: List<User>): List<UserDTO> = users.map { toDTO(it) }
}

// Repository interface
interface UserRepository {
    suspend fun create(name: String): User
    suspend fun findById(id: Int): User?
    suspend fun update(id: Int, name: String): Boolean
    suspend fun delete(id: Int): Boolean
    suspend fun findAll(): List<User>
}

// Repository implementation
class UserRepositoryImpl(private val db: DatabaseFactory) : UserRepository {
    init {
        db.init()
    }
    override suspend fun create(name: String): User = db.query {
        val entity = UserEntity.new {
            this.name = name
        }
        entity.toUser()
    }

    override suspend fun findById(id: Int): User? = db.query {
        UserEntity.findById(id)?.toUser()
    }

    override suspend fun update(id: Int, name: String): Boolean = db.query {
        val entity = UserEntity.findById(id)
        if (entity != null) {
            entity.name = name
            true
        } else {
            false
        }
    }

    override suspend fun delete(id: Int): Boolean = db.query {
        val entity = UserEntity.findById(id)
        if (entity != null) {
            entity.delete()
            true
        } else {
            false
        }
    }

    override suspend fun findAll(): List<User> = db.query {
        UserEntity.all().map { it.toUser() }
    }
}

// Service interface
interface UserService {
    suspend fun create(dto: UserDTO): UserDTO
    suspend fun getById(id: Int): UserDTO?
    suspend fun update(id: Int, dto: UserDTO): Boolean
    suspend fun delete(id: Int): Boolean
    suspend fun getAll(): List<UserDTO>
}

// Service implementation
class UserServiceImpl(
    private val repository: UserRepository
) : UserService {
    override suspend fun create(dto: UserDTO): UserDTO {
        val user = repository.create(dto.name)
        return UserMapper.toDTO(user)
    }

    override suspend fun getById(id: Int): UserDTO? {
        return repository.findById(id)?.let { UserMapper.toDTO(it) }
    }

    override suspend fun update(id: Int, dto: UserDTO): Boolean {
        return repository.update(id, dto.name)
    }

    override suspend fun delete(id: Int): Boolean {
        return repository.delete(id)
    }

    override suspend fun getAll(): List<UserDTO> {
        return UserMapper.toDTOs(repository.findAll())
    }
}

// Controller
class UserController(private val service: UserService) {
    suspend fun getAll(ctx: ApplicationCall) {
        ctx.respond(service.getAll())
    }
    suspend fun getById(ctx: ApplicationCall) {
        val id = ctx.parameters["id"]?.toIntOrNull() ?: return ctx.respond(HttpStatusCode.BadRequest)
        val user = service.getById(id) ?: return ctx.respond(HttpStatusCode.NotFound)
        ctx.respond(user)
    }
    suspend fun create(ctx: ApplicationCall) {
        val dto = ctx.receive<UserDTO>()
        ctx.respond(HttpStatusCode.Created, service.create(dto))
    }
    suspend fun update(ctx: ApplicationCall) {
        val id = ctx.parameters["id"]?.toIntOrNull() ?: return ctx.respond(HttpStatusCode.BadRequest)
        val dto = ctx.receive<UserDTO>()
        if (service.update(id, dto)) ctx.respond(HttpStatusCode.OK) else ctx.respond(HttpStatusCode.NotFound)
    }
    suspend fun delete(ctx: ApplicationCall) {
        val id = ctx.parameters["id"]?.toIntOrNull() ?: return ctx.respond(HttpStatusCode.BadRequest)
        if (service.delete(id)) ctx.respond(HttpStatusCode.NoContent) else ctx.respond(HttpStatusCode.NotFound)
    }
}

// Routing
fun Route.userRouting(controller: UserController) {
    route("/users") {
        get { controller.getAll(call) }
        get("/{id}") { controller.getById(call) }
        post { controller.create(call) }
        put("/{id}") { controller.update(call) }
        delete("/{id}") { controller.delete(call) }
    }
}
