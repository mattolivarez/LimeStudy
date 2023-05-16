/*
Matthew Olivarez
Spring 2023
Senior Project
Limestudy Backend
User Repository Interface file
*/

package dev.mattolivarez.Repository;

import dev.mattolivarez.Exception.LSAuthException;
import dev.mattolivarez.Model.UserModel;

public interface UserRepository
{
    Integer create(String firstName, String lastName, String email, String password) throws LSAuthException;

    UserModel findByEmailAndPassword(String email, String password) throws LSAuthException;

    Integer getCountByEmail(String email);

    UserModel findById(Integer userId);

}
