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
			"message": "not authorized",
		})
	}
	if sess.Get(AUTH_KEY) == nil {
		return c.Status(fiber.StatusUnauthorized).JSON(fiber.Map {
			"message": "not authorized",
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
			"message": "server issue: " + err.Error(),
		})
	}
	password, bcErr := bcrypt.GenerateFromPassword([]byte(data.Password), 10)
	if bcErr != nil {
		return c.Status(fiber.StatusInternalServerError).JSON(fiber.Map {
			"message": "server issue: " + err.Error(),
		})
	}
	
	user := model.UserModel {
		FirstName: data.FirstName,
		LastName: data.LastName,
		Email: data.Email,
		Password: string(password),
	}
	err = model.CreateUser(&user)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).JSON(fiber.Map {
			"message": "server issue: " + err.Error(),
		})
	}
	return c.Status(fiber.StatusOK).JSON(fiber.Map {
		"message": "registered",
	})
}

func Login(c *fiber.Ctx) error {
	c.Accepts("application/json")
	var data UserAuth
	err := c.BodyParser(&data)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).JSON(fiber.Map {
			"message": "server issue: " + err.Error(),
		})
	}
	var user model.UserModel
	if !model.CheckEmail(data.Email, &user) {
		return c.Status(fiber.StatusUnauthorized).JSON(fiber.Map {
			"message": "unauthorized",
		})
	}
	err = bcrypt.CompareHashAndPassword([]byte(user.Password), []byte(data.Password))
	if err != nil {
		return c.Status(fiber.StatusUnauthorized).JSON(fiber.Map {
			"message": "unauthorized",
		})
	}
	sess, sessErr := store.Get(c)
	if sessErr != nil {
		return c.Status(fiber.StatusInternalServerError).JSON(fiber.Map {
			"message": "server issue: " + sessErr.Error(),
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
		"message": "logged in",
	})
}

func Logout(c *fiber.Ctx) error {
	c.Accepts("application/json")
	sess, err := store.Get(c)
	if err != nil {
		return c.Status(fiber.StatusOK).JSON(fiber.Map {
			"message": "logged out (no session found)",
		})
	}
	err = sess.Destroy()
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).JSON(fiber.Map {
			"message": "server issue: " + err.Error(),
		})
	}
	return c.Status(fiber.StatusOK).JSON(fiber.Map {
		"message": "logged out",
	})	
}

func HealthCheck(c *fiber.Ctx) error {
	c.Accepts("application/json")
	sess, err := store.Get(c)
	if err != nil {
		return c.Status(fiber.StatusUnauthorized).JSON(fiber.Map {
			"message": "unauthorized",
		})
	}
	auth := sess.Get(AUTH_KEY)
	if auth != nil {
		return c.Status(fiber.StatusOK).JSON(fiber.Map {
			"message": "authenticated",
		})
	} else {
		return c.Status(fiber.StatusUnauthorized).JSON(fiber.Map {
			"message": "unauthorized",
		})
	}
}

func GetUser(c *fiber.Ctx) error {
	c.Accepts("application/json")
	sess, err := store.Get(c)
	if err != nil {
		return c.Status(fiber.StatusUnauthorized).JSON(fiber.Map {
			"message": "unauthorized 1",
		})
	}
	if sess.Get(AUTH_KEY) == nil {
		return c.Status(fiber.StatusUnauthorized).JSON(fiber.Map {
			"message": "unauthorized 2",
		})
	}
	UserId := sess.Get(USER_ID)
	if UserId == nil {
		return c.Status(fiber.StatusUnauthorized).JSON(fiber.Map {
			"message": "unauthorized 3",
		})
	}
	var user model.UserModel
	user, err = model.GetUser(fmt.Sprint(UserId))
	if err != nil {
		return c.Status(fiber.StatusUnauthorized).JSON(fiber.Map {
			"message": "unauthorized 4",
		})
	}
	user.Password = ""
	return c.Status(fiber.StatusOK).JSON(user)

}