package dev.mattolivarez.Repository;

import dev.mattolivarez.Exception.LSAuthException;
import dev.mattolivarez.Model.User;

public interface UserRepository
{
    Integer create(String firstName, String lastName, String email, String password) throws LSAuthException;

    User findByEmailAndPassword(String email, String password) throws LSAuthException;

    Integer getCountByEmail(String email);

    User findById(Integer userId);

}
