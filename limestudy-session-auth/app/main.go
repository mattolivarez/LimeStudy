package main

import (
	"go/sessionauth/model"
	"go/sessionauth/routes"
)

func main() {
	model.Setup()
	routes.Setup()
}