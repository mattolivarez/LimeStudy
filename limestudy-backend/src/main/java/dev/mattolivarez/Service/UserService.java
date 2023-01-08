package dev.mattolivarez.Service;

import dev.mattolivarez.Exception.LSAuthException;
import dev.mattolivarez.Model.UserModel;

public interface UserService
{
    UserModel validateUser(String email, String password) throws LSAuthException;

    UserModel registerUser(String firsName, String lastName, String email, String password) throws LSAuthException;

}
