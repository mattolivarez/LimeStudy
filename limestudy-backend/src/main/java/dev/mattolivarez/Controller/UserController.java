package dev.mattolivarez.Controller;

import dev.mattolivarez.Constants;
import dev.mattolivarez.Model.UserModel;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/users")
public class UserController
{
/*
    @Autowired
    UserService userService;*/


    @PostMapping("/get-user-token")
    public ResponseEntity<Map<String, String>> getUserToken(@RequestBody Map<String, Object> userMap)
    {
        int userId = (Integer) userMap.get("userId");
        String email = (String) userMap.get("email");
        String firstName = (String) userMap.get("firstName");
        String lastName = (String) userMap.get("lastName");
        String account_created_on = (String) userMap.get("accountCreatedOn");
        UserModel userModel = new UserModel(userId, email, firstName, lastName, account_created_on);

        return new ResponseEntity<>(generateJWTToken(userModel), HttpStatus.OK);
    }
    /*@GetMapping("/test")
    public ResponseEntity<HttpResponse<String>> testingGoReq() throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = checkIfUserLoggedIn();
        return new ResponseEntity<HttpResponse<String>>(response, HttpStatus.OK);
    }*/
    @GetMapping("/test")
    public ResponseEntity<String> testingGoReq(HttpServletRequest httpServletRequest) throws URISyntaxException, IOException, InterruptedException {
        Cookie[] cookies = httpServletRequest.getCookies();
        System.out.println(cookies[0].getValue());
        checkIfUserLoggedIn(httpServletRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
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
    */
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

    private void checkIfUserLoggedIn(HttpServletRequest httpServletRequest) throws URISyntaxException, IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest golangReq = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8085/auth/healthcheck"))
                .headers("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(golangReq, HttpResponse.BodyHandlers.ofString());
        System.out.println(httpResponse);
        //return httpResponse;
    }

}
