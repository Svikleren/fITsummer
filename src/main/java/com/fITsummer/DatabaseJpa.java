
package com.fITsummer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;

@Component
public class DatabaseJpa {
    @Autowired
    UserRepository userRepo;

    public boolean userExists(String username) {
        System.out.println(username);
        List<User> rs = userRepo.findByUsername(username);
        System.out.println(rs.size());
        if (!rs.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean userPwdCorrect(String username, String password) {
        List<User> rs = userRepo.findByUsername(username);
        if (!rs.isEmpty() && rs.get(rs.size() - 1).getPassword().equals(password)) {
            return true;
        } else {
            return false;
        }
    }

    public void registerNewUser(User user) {

        userRepo.save(user);
    }
}

