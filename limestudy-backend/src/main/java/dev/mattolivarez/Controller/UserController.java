package dev.mattolivarez.Controller;

import dev.mattolivarez.Constants;
import dev.mattolivarez.Model.UserModel;
import dev.mattolivarez.Service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/users")
public class UserController
{

    @Autowired
    UserService userService;


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, Object> userMap)
    {
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        UserModel userModel = userService.validateUser(email, password);
        return new ResponseEntity<>(generateJWTToken(userModel), HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Map<String, Object> userMap)
    {
        String firstName = (String) userMap.get("firstName");
        String lastName = (String) userMap.get("lastName");
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        UserModel userModel = userService.registerUser(firstName, lastName, email, password);
        return new ResponseEntity<>(generateJWTToken(userModel), HttpStatus.OK);
    }

    @PostMapping("/get-user")
    public ResponseEntity<UserModel> getUserDetails(@RequestBody Map<String, Object> userMap)
    {
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        UserModel userModel = userService.validateUser(email, password);
        return new ResponseEntity<>(userModel, HttpStatus.OK);
    }

    private Map<String, String> generateJWTToken(UserModel userModel)
    {
        long timestamp = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + Constants.TOKEN_VALIDITY))
                .claim("userId", userModel.getUserId())
                .claim("email", userModel.getEmail())
                .claim("firstName", userModel.getFirstName())
                .claim("lastName", userModel.getLastName())
                .compact();
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return map;
    }
}
