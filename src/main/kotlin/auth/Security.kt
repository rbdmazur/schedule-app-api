import com.example.auth.UserSession
import com.example.repository.repositories.UsersRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import kotlinx.html.InputType

fun Application.configureSecurity(usersRepository: UsersRepository) {
    val authConfig = environment.config.config("ktor.auth.form")

    install(Authentication) {
        form("auth-form") {
            userParamName = "email"
            passwordParamName = "password"
            validate { credentials ->
                val user = usersRepository.findUserByEmail(credentials.name)
                if (user != null && credentials.password == user.password) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }

            challenge {
                call.respondRedirect("/login")
            }
        }

        session<UserSession>("auth-session") {
            validate { session ->
                val user = usersRepository.findUserByEmail(session.name)
                if (user != null) {
                    session
                } else {
                    null
                }
            }

            challenge { call.respondRedirect("/login") }
        }
    }


}

