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
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Domain model
data class Order(val id: Int, val userId: Int, val orderDate: LocalDateTime, val totalAmount: Double)

// DTO for serialization
@Serializable
data class OrderDTO(val id: Int, val userId: Int, val orderDate: String, val totalAmount: Double)

// Table definition
object Orders : IntIdTable() {
    val userId = integer("user_id").references(Users.id)
    val orderDate = datetime("order_date")
    val totalAmount = double("total_amount")
}

// Entity
class OrderEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OrderEntity>(Orders)
    var userId by Orders.userId
    var orderDate by Orders.orderDate
    var totalAmount by Orders.totalAmount

    fun toOrder() = Order(id.value, userId, orderDate, totalAmount)
}

// Mapper
object OrderMapper {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    fun toDTO(order: Order): OrderDTO = OrderDTO(
        order.id,
        order.userId,
        order.orderDate.format(formatter),
        order.totalAmount
    )
    fun toOrder(dto: OrderDTO): Order = Order(
        dto.id,
        dto.userId,
        LocalDateTime.parse(dto.orderDate, formatter),
        dto.totalAmount
    )
    fun toDTOs(orders: List<Order>): List<OrderDTO> = orders.map { toDTO(it) }
}

// Repository interface
interface OrderRepository {
    suspend fun create(userId: Int, orderDate: LocalDateTime, totalAmount: Double): Order
    suspend fun findById(id: Int): Order?
    suspend fun update(id: Int, userId: Int, orderDate: LocalDateTime, totalAmount: Double): Boolean
    suspend fun delete(id: Int): Boolean
    suspend fun findAll(): List<Order>
}

// Repository implementation
class OrderRepositoryImpl(private val db: DatabaseFactory) : OrderRepository {
    init {
        db.init()
    }
    override suspend fun create(userId: Int, orderDate: LocalDateTime, totalAmount: Double): Order = db.query {
        val entity = OrderEntity.new {
            this.userId = userId
            this.orderDate = orderDate
            this.totalAmount = totalAmount
        }
        entity.toOrder()
    }

    override suspend fun findById(id: Int): Order? = db.query {
        OrderEntity.findById(id)?.toOrder()
    }

    override suspend fun update(id: Int, userId: Int, orderDate: LocalDateTime, totalAmount: Double): Boolean = db.query {
        val entity = OrderEntity.findById(id)
        if (entity != null) {
            entity.userId = userId
            entity.orderDate = orderDate
            entity.totalAmount = totalAmount
            true
        } else {
            false
        }
    }

    override suspend fun delete(id: Int): Boolean = db.query {
        val entity = OrderEntity.findById(id)
        if (entity != null) {
            entity.delete()
            true
        } else {
            false
        }
    }

    override suspend fun findAll(): List<Order> = db.query {
        OrderEntity.all().map { it.toOrder() }
    }
}

// Service interface
interface OrderService {
    suspend fun create(dto: OrderDTO): OrderDTO
    suspend fun getById(id: Int): OrderDTO?
    suspend fun update(id: Int, dto: OrderDTO): Boolean
    suspend fun delete(id: Int): Boolean
    suspend fun getAll(): List<OrderDTO>
}

// Service implementation
class OrderServiceImpl(
    private val repository: OrderRepository
) : OrderService {
    override suspend fun create(dto: OrderDTO): OrderDTO {
        val order = repository.create(dto.userId, LocalDateTime.parse(dto.orderDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME), dto.totalAmount)
        return OrderMapper.toDTO(order)
    }

    override suspend fun getById(id: Int): OrderDTO? {
        return repository.findById(id)?.let { OrderMapper.toDTO(it) }
    }

    override suspend fun update(id: Int, dto: OrderDTO): Boolean {
        return repository.update(id, dto.userId, LocalDateTime.parse(dto.orderDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME), dto.totalAmount)
    }

    override suspend fun delete(id: Int): Boolean {
        return repository.delete(id)
    }

    override suspend fun getAll(): List<OrderDTO> {
        return OrderMapper.toDTOs(repository.findAll())
    }
}

// Controller
class OrderController(private val service: OrderService) {
    suspend fun getAll(ctx: ApplicationCall) {
        ctx.respond(service.getAll())
    }
    suspend fun getById(ctx: ApplicationCall) {
        val id = ctx.parameters["id"]?.toIntOrNull() ?: return ctx.respond(HttpStatusCode.BadRequest)
        val order = service.getById(id) ?: return ctx.respond(HttpStatusCode.NotFound)
        ctx.respond(order)
    }
    suspend fun create(ctx: ApplicationCall) {
        val dto = ctx.receive<OrderDTO>()
        ctx.respond(HttpStatusCode.Created, service.create(dto))
    }
    suspend fun update(ctx: ApplicationCall) {
        val id = ctx.parameters["id"]?.toIntOrNull() ?: return ctx.respond(HttpStatusCode.BadRequest)
        val dto = ctx.receive<OrderDTO>()
        if (service.update(id, dto)) ctx.respond(HttpStatusCode.OK) else ctx.respond(HttpStatusCode.NotFound)
    }
    suspend fun delete(ctx: ApplicationCall) {
        val id = ctx.parameters["id"]?.toIntOrNull() ?: return ctx.respond(HttpStatusCode.BadRequest)
        if (service.delete(id)) ctx.respond(HttpStatusCode.NoContent) else ctx.respond(HttpStatusCode.NotFound)
    }
}

// Routing
fun Route.orderRouting(controller: OrderController) {
    route("/orders") {
        get { controller.getAll(call) }
        get("/{id}") { controller.getById(call) }
        post { controller.create(call) }
        put("/{id}") { controller.update(call) }
        delete("/{id}") { controller.delete(call) }
    }
}