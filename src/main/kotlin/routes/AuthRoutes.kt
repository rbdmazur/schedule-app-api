package com.example.routes

import com.example.auth.hashing.HashingService
import com.example.auth.token.TokenClaim
import com.example.auth.token.TokenConfig
import com.example.auth.token.TokenService
import com.example.routes.requests.AuthRequest
import com.example.routes.responses.AuthResponse
import com.example.service.UserService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*

fun Route.signIn(
    tokenConfig: TokenConfig,
    userService: UserService,
    hashingService: HashingService,
    tokenService: TokenService
) {

    post("/signin") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = userService.findUserByEmail(request.email)
        if (user == null) {
            call.respond(HttpStatusCode.Conflict, "Incorrect email or password")
            return@post
        }

        val isValidPassword = hashingService.verify(
            request.password,
            user.password
        )

        if (!isValidPassword) {
            call.respond(HttpStatusCode.Conflict, "Incorrect email or password")
            return@post
        }

        val token = tokenService.generate(
            tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id.toString()
            )
        )
        call.respond(HttpStatusCode.OK, AuthResponse(userId = user.id.toString(), token = token))
    }
    authenticate {
        get("/auth") {
            call.respond(HttpStatusCode.OK)
        }
    }
    get("/signin") {
        call.respondText(
            """
                <!DOCTYPE html>
                <html lang="ru">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Авторизация</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            height: 100vh;
                            background-color: #f4f4f4;
                        }
                        .login-container {
                            background: white;
                            padding: 20px;
                            border-radius: 8px;
                            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                            width: 300px;
                            text-align: center;
                        }
                        input {
                            width: 100%;
                            padding: 10px;
                            margin: 10px 0;
                            border: 1px solid #ccc;
                            border-radius: 5px;
                        }
                        button {
                            width: 100%;
                            padding: 10px;
                            background: #28a745;
                            color: white;
                            border: none;
                            border-radius: 5px;
                            cursor: pointer;
                        }
                        button:hover {
                            background: #218838;
                        }
                    </style>
                </head>
                <body>
                    <div class="login-container">
                        <h2>Вход</h2>
                        <input type="email" id="email" placeholder="Email" required>
                        <input type="password" id="password" placeholder="Пароль" required>
                        <button onclick="login()">Войти</button>
                    </div>

                    <script>
                        function login() {
                            const email = document.getElementById('email').value;
                            const password = document.getElementById('password').value;

                            const data = { email, password };

                            fetch('http://127.0.0.1:8080/signin', {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/json'
                                },
                                body: JSON.stringify(data)
                            })
                            .then(response => response.json())
                            .then(result => {
                                if (result.token) {
                                    localStorage.setItem('authToken', result.token);
                                    alert('Успешный вход');
                                } else {
                                    alert(result.message);
                                }
                            })
                            .catch(error => {
                                console.error('Ошибка:', error);
                                alert('Ошибка при авторизации');
                            });
                        }
                    </script>
                </body>
                </html>
            """.trimIndent(), contentType = ContentType.Text.Html
        )
    }
}