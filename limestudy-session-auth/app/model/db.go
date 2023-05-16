/*
Matthew Olivarez
Spring 2023
Senior Project
Limestudy Session Management Authentication/Authorization Middleware
Contains setup for database communication
*/

package model

import (
	"fmt"
	"database/sql"
	_ "github.com/lib/pq"
)

var print = fmt.Println

var db *sql.DB

func Setup() {
	dsn := "host=localhost port=5432 user=postgres password=postgres dbname=postgres sslmode=disable"
	var err error
	db, err = sql.Open("postgres", dsn)
	if err != nil {
		panic(err)
	}

	err = db.Ping()
	if err != nil {
		print(err)
	}
}
