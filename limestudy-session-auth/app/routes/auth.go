package routes

import (
	"go/sessionauth/model"
	"strings"
	"fmt"
	"github.com/gofiber/fiber/v2"
	"golang.org/x/crypto/bcrypt"
)

type UserAuth struct {
	//user_id uint64 `json: "user_id"`
	FirstName string `json:"first_name"`
	LastName string `json:"last_name"`
	Email string `json:"email"`
	Password string `json:"password"`
	AccountCreatedOn string `json:"account_created_on"`
}


func NewMiddleware() fiber.Handler {
	return AuthMiddleware
}

func AuthMiddleware(c *fiber.Ctx) error {
	sess, err := store.Get(c)
	if strings.Split(c.Path(), "/")[1] == "auth" {
		return c.Next()
	}
	if err != nil {
		return c.Status(fiber.StatusUnauthorized).JSON(fiber.Map {
			"message": "Not Authorized due to " + err.Error(),
		})
	}
	if sess.Get(AUTH_KEY) == nil {
		return c.Status(fiber.StatusUnauthorized).JSON(fiber.Map {
			"message": "Not Authorized due to AUTH_KEY == nil",
		})
	}
	return c.Next()
}

func Register(c *fiber.Ctx) error {
	c.Accepts("application/json")
	var data UserAuth
	err := c.BodyParser(&data)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).JSON(fiber.Map {
			"message": "Server Issue: " + err.Error(),
		})
	}
	password, bcErr := bcrypt.GenerateFromPassword([]byte(data.Password), 10)
	if bcErr != nil {
		return c.Status(fiber.StatusInternalServerError).JSON(fiber.Map {
			"message": "Server Issue: " + bcErr.Error(),
		})
	}
	
	user := model.UserModel {
		FirstName: data.FirstName,
		LastName: data.LastName,
		Email: data.Email,
		Password: string(password),
		AccountCreatedOn: data.AccountCreatedOn,
	}
	err = model.CreateUser(&user)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).JSON(fiber.Map {
			"message": "Server Issue: " + err.Error(),
		})
	}
	return c.Status(fiber.StatusOK).JSON(fiber.Map {
		"message": "User Registered",
	})
}

func Login(c *fiber.Ctx) error {
	c.Accepts("application/json")
	var data UserAuth
	err := c.BodyParser(&data)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).JSON(fiber.Map {
			"message": "Server Issue: " + err.Error(),
		})
	}
	var user model.UserModel
	if !model.CheckEmail(data.Email, &user) {
		return c.Status(fiber.StatusUnauthorized).JSON(fiber.Map {
			"message": "Email not recognized",
		})
	}
	err = bcrypt.CompareHashAndPassword([]byte(user.Password), []byte(data.Password))
	if err != nil {
		return c.Status(fiber.StatusUnauthorized).JSON(fiber.Map {
			"message": "Incorrect password",
		})
	}
	sess, sessErr := store.Get(c)
	if sessErr != nil {
		return c.Status(fiber.StatusInternalServerError).JSON(fiber.Map {
			"message": "Server Issue: " + sessErr.Error(),
		})
	}
	sess.Set(AUTH_KEY, true)
	sess.Set(USER_ID, user.UserId)
	sessErr = sess.Save()
	if sessErr != nil {
		return c.Status(fiber.StatusInternalServerError).JSON(fiber.Map {
			"message": "server issue: " + sessErr.Error(),
		})
	}
	return c.Status(fiber.StatusOK).JSON(fiber.Map {
		"message": "User logged in",
	})
}

func Logout(c *fiber.Ctx) error {
	c.Accepts("application/json")
	sess, err := store.Get(c)
	if err != nil {
		return c.Status(fiber.StatusOK).JSON(fiber.Map {
			"message": "Logged out (No session found)",
		})
	}
	err = sess.Destroy()
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).JSON(fiber.Map {
			"message": "Server Issue: " + err.Error(),
		})
	}
	return c.Status(fiber.StatusOK).JSON(fiber.Map {
		"message": "User logged out",
	})	
}

func HealthCheck(c *fiber.Ctx) error {
	c.Accepts("application/json")
	sess, err := store.Get(c)
	if err != nil {
		return c.Status(fiber.StatusUnauthorized).JSON(fiber.Map {
			"message": "Not authorized due to " + err.Error(),
		})
	}
	auth := sess.Get(AUTH_KEY)
	if auth != nil {
		return c.Status(fiber.StatusOK).JSON(fiber.Map {
			"message": "Authenticated",
		})
	} else {
		return c.Status(fiber.StatusUnauthorized).JSON(fiber.Map {
			"message": "Not authorized",
		})
	}
}

func GetUser(c *fiber.Ctx) error {
	c.Accepts("application/json")
	sess, err := store.Get(c)
	if err != nil {
		return c.Status(fiber.StatusUnauthorized).JSON(fiber.Map {
			"message": "Not authorized due to " + err.Error(),
		})
	}
	if sess.Get(AUTH_KEY) == nil {
		return c.Status(fiber.StatusUnauthorized).JSON(fiber.Map {
			"message": "Not authorized: Couldn't verify authorization key",
		})
	}
	UserId := sess.Get(USER_ID)
	if UserId == nil {
		return c.Status(fiber.StatusUnauthorized).JSON(fiber.Map {
			"message": "Not authorized: Couldn't verify user id",
		})
	}
	var user model.UserModel
	user, err = model.GetUser(fmt.Sprint(UserId))
	if err != nil {
		return c.Status(fiber.StatusUnauthorized).JSON(fiber.Map {
			"message": "Not authorized due to " + err.Error(),
		})
	}
	user.Password = ""
	return c.Status(fiber.StatusOK).JSON(user)

}