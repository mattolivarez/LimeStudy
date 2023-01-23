package dev.mattolivarez.Service;

import dev.mattolivarez.Exception.LSAuthException;
import dev.mattolivarez.Model.UserModel;
import dev.mattolivarez.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@Transactional
public class UserServiceImpl //implements UserService
{
/*
    @Autowired
    UserRepository userRepository;

    @Override
    public UserModel validateUser(String email, String password) throws LSAuthException {
        if (email != null)
        {
            email = email.toLowerCase();
        }
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public UserModel registerUser(String firsName, String lastName, String email, String password) throws LSAuthException {
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        if (email != null)
        {
            email = email.toLowerCase();
        }
        if (!pattern.matcher(email).matches())
        {
            throw new LSAuthException("Invalid email format");
        }
        Integer count = userRepository.getCountByEmail(email);
        if (count > 0)
        {
            throw new LSAuthException("Email already in use");
        }
        Integer userId = userRepository.create(firsName, lastName, email, password);
        return userRepository.findById(userId);
    }*/
}
