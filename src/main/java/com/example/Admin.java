
package com.example;

public class Admin extends User {

    public Admin(String username) {
        super(username);
    }

    @Override
    public String getRole() {
        return "Admin";
    }
}
