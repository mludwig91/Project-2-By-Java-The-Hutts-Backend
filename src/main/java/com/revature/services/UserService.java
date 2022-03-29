package com.revature.services;

import com.revature.controllers.UserController;
import com.revature.models.Users;
import com.revature.models.loginDTO;
import com.revature.repo.UsersDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.security.NoSuchAlgorithmException;

@Service
public class UserService {

    private final UsersDAO usersDAO;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final Logger userServiceLog = LoggerFactory.getLogger(UserService.class);



    @Autowired
    public UserService(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public Users findById(int id){return usersDAO.findById(id);}

    public Users findByUsername(String username) {
        return usersDAO.findByUsername(username);
    }

    public boolean loginUser(loginDTO user){
        Users secure = usersDAO.findByUsername(user.getUsername());
        if (secure != null){
            String passcheck = encoder.encode(user.getPassword());
            String securepass = secure.getPassword();
            return securepass.equals(passcheck);
        }
        return false;
    }

    public boolean createUser(Users user){
        //User Validaiton before saving
        user.setPassword(encoder.encode(user.getPassword()));
        try{
            usersDAO.save(user);
            userServiceLog.info(user.getUsername() +" was successfully registered!!");
        }catch(Exception e){
            userServiceLog.debug(user.getUsername() +" could not be registered!!");
            e.printStackTrace();
        }
        return true;
    }

    public boolean updateUser(Users user){
        Users user2 = usersDAO.findByUsername(user.getUsername());
        user.setUserId(user2.getUserId());
            try{
                usersDAO.save(user);
            }catch(Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
    }
}
