/*
Matthew Olivarez
Spring 2023
Senior Project
Limestudy Session Management Authentication/Authorization Middleware
Initializer for middleware
*/

package main

import (
	"go/sessionauth/model"
	"go/sessionauth/routes"
)

func main() {
	model.Setup()
	routes.Setup()
}