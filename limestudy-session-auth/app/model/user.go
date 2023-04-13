package model



type UserModel struct {
	UserId uint64 `json:"user_id"`
	FirstName string `json:"first_name"`
	LastName string `json:"last_name"`
	Email string `json:"email"`
	Password string `json:"password"`
	AccountCreatedOn string `json:"account_created_on"`
	FlashcardDelaySetting uint64 `json:"flashcard_delay_setting"`
}

func CreateUser(user *UserModel) error {
	statement := `insert into "USER"(user_id, first_name, last_name, email, password, account_created_on, flashcard_delay_setting) values(NEXTVAL('USER_SEQ'), $1, $2, $3, $4, $5, $6);`
	_, err := db.Exec(statement, user.FirstName, user.LastName, user.Email, user.Password, user.AccountCreatedOn, user.FlashcardDelaySetting)
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
		err = rows.Scan(&user.UserId, &user.FirstName, &user.LastName, &user.Email, &user.Password, &user.AccountCreatedOn, &user.FlashcardDelaySetting)
		if err != nil {
			return UserModel{}, err
		}
	}
	return user, nil
}

func CheckEmail(email string, user *UserModel) bool {
	statement := `select user_id, first_name, last_name, email, password, account_created_on, flashcard_delay_setting from "USER" where email=$1 limit 1;`
	rows, err := db.Query(statement, email)
	if err != nil {
		return false
	}
	for rows.Next() {
		err = rows.Scan(&user.UserId, &user.FirstName, &user.LastName, &user.Email, &user.Password, &user.AccountCreatedOn, &user.FlashcardDelaySetting)
		if err != nil {
			return false
		}
	}
	return true
}