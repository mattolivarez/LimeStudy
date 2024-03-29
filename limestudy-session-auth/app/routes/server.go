/*
Matthew Olivarez
Spring 2023
Senior Project
Limestudy Session Management Authentication/Authorization Middleware
Contains server setup including routes
*/

package routes

import (
	"time"

	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/cors"
	"github.com/gofiber/fiber/v2/middleware/session"
)

var (
	store *session.Store
	AUTH_KEY string = "authenticated"
	USER_ID  string = "user_id"
)

func Setup() {
	router := fiber.New()
	store = session.New(session.Config {
		CookieHTTPOnly: true,
		Expiration: time.Hour * 5,
		CookiePath: "/", 
		CookieSecure: true, // for https
		CookieSameSite: "None",
	})

	router.Use(NewMiddleware(), cors.New(cors.Config {
		AllowCredentials: true,
		AllowOrigins: "*",
		AllowHeaders: "Access-Control-Allow-Origin, Content-Type, Origin, Accept",
		AllowMethods: "*",
	}))

	router.Post("/auth/register", Register)
	router.Post("/auth/login", Login)
	router.Post("/auth/logout", Logout)
	router.Get("/auth/healthcheck", HealthCheck)
	router.Get("/user", GetUser)

	router.Listen("localhost:8085")
}