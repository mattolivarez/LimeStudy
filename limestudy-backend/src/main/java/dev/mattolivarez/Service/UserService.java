package dev.mattolivarez.Service;

import dev.mattolivarez.Exception.LSAuthException;
import dev.mattolivarez.Model.User;

public interface UserService
{
    User validateUser(String email, String password) throws LSAuthException;

    User registerUser(String firsName, String lastName, String email, String password) throws LSAuthException;

}
