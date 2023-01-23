package model


type UserModel struct {
	UserId uint64 `json:"user_id"`
	FirstName string `json:"first_name"`
	LastName string `json:"last_name"`
	Email string `json:"email"`
	Password string `json:"password"`
}

func CreateUser(user *UserModel) error {
	statement := `insert into "USER"(user_id, first_name, last_name, email, password) values(NEXTVAL('USER_SEQ'), $1, $2, $3, $4);`
	_, err := db.Exec(statement, user.FirstName, user.LastName, user.Email, user.Password)
	return err
}

func GetUser(UserId string) (UserModel, error) {
	var user UserModel
	statement := `select * from "USER" where user_id=$1;`
	rows, err := db.Query(statement, UserId)
	if err != nil {
		return UserModel{}, err
	}
	for rows.Next() {
		err = rows.Scan(&user.UserId, &user.FirstName, &user.LastName, &user.Email, &user.Password)
		if err != nil {
			return UserModel{}, err
		}
	}
	return user, nil
}

func CheckEmail(email string, user *UserModel) bool {
	statement := `select user_id, first_name, last_name, email, password from "USER" where email=$1 limit 1;`
	rows, err := db.Query(statement, email)
	if err != nil {
		return false
	}
	for rows.Next() {
		err = rows.Scan(&user.UserId, &user.FirstName, &user.LastName, &user.Email, &user.Password)
		if err != nil {
			return false
		}
	}
	return true
}