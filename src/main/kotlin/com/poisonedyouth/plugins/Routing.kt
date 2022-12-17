package com.poisonedyouth.plugins

import com.poisonedyouth.api.ApiResult
import com.poisonedyouth.api.ErrorCode
import com.poisonedyouth.api.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*

fun Application.configureRouting() {

    routing {
        route("/api/user") {
            get {
                val name = call.request.queryParameters["name"]
                when (val result = UserService.findBy(name)) {
                    is ApiResult.Success -> call.respond(status = HttpStatusCode.OK, message = result.value)
                    is ApiResult.Failure -> handleFailure(result)
                }
            }
            post {
                when (val result = UserService.add(call.receive())) {
                    is ApiResult.Success -> call.respond(status = HttpStatusCode.Created, message = result.value)
                    is ApiResult.Failure -> handleFailure(result)
                }
            }
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleFailure(result: ApiResult.Failure<ErrorCode>) {
    val status = when (result.errorCode) {
        ErrorCode.GENERAL_ERROR -> HttpStatusCode.InternalServerError
        ErrorCode.INVALID_INPUT,
        ErrorCode.USER_NOT_FOUND -> HttpStatusCode.BadRequest
    }
    call.respond(status = status, message = result.message)
}
