package com.revature.controllers;

import com.revature.models.Events;
import com.revature.models.Users;
import com.revature.models.loginDTO;

import com.revature.services.EventService;
import com.revature.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;


@RestController
@RequestMapping(value = "users")
public class UserController {

    private final UserService userService;
    private final Logger userControllerLog = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/registration")
    public ResponseEntity<Users> registration(@RequestBody Users user){

        if(userService.createUser(user)){


                userControllerLog.info("User: " + user.getUsername()
                        + "has successfully created an account!");

                return ResponseEntity.status(201).body(user);
            }

            return ResponseEntity.status(400).build();
        }


    @PostMapping("/login")
    public ResponseEntity<Users> login(@RequestHeader loginDTO login, HttpSession session){

        if(userService.loginUser(login)){


            session.setAttribute("username", login.getUsername());
            Users user = userService.findByUsername(login.getUsername());
            userControllerLog.info("User: " + user.getUsername()
                    + "has successfully logged in!");

            return ResponseEntity.status(200).body(user);
        }
        return ResponseEntity.status(401).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Users> logout(HttpSession session){
        if(session != null) {
            userControllerLog.info("User: " + session.getAttribute("username")
                    + "has successfully logged out!");
            session.invalidate();
            return ResponseEntity.status(200).build();
        }
        return ResponseEntity.status(401).build();
    }

    @GetMapping("/profile")
    @ResponseBody
    public ResponseEntity<Users> getEvents(@RequestBody loginDTO login, HttpSession session){


        Users thisuser = userService.findByUsername(session.getAttribute("username").toString());
        if (thisuser != null) {
            return ResponseEntity.status(200).body(thisuser);
        }else{
            return ResponseEntity.status(400).build();
            }
    }

    @GetMapping("/{username}")
    @ResponseBody
    public ResponseEntity<Users> getUser(@PathVariable("username") String username){
        Users loggedInUser = userService.findByUsername(username);
        if (loggedInUser != null) {
            return ResponseEntity.status(200).body(loggedInUser);
        } else {
            return ResponseEntity.status(204).build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Users> update(@RequestBody Users user){
        if(userService.updateUser(user)){
            userControllerLog.info("User: " + user.getUsername() +
                    "has successfully updated their information!");
            return ResponseEntity.status(201).body(user);
        }
        return ResponseEntity.status(400).build();
    }

    @Autowired
    UserController(UserService userService){
        super();
        this.userService = userService;
    }

}
