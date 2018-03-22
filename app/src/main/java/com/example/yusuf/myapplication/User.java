package com.example.yusuf.myapplication;

import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class User {

    public String nickname;
    public String username;
    public String email;
    public String usersurname;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email,String nickname ,String surname) {
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.usersurname =surname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getUsersurname() {
        return usersurname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsersurname(String usersurname) {
        this.usersurname = usersurname;
    }
}

