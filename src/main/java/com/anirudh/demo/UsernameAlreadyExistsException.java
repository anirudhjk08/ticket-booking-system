package com.anirudh.demo;

public class UsernameAlreadyExistsException extends RuntimeException{
    public UsernameAlreadyExistsException(String username){
        super("Username already taken: "+ username);
    }
}

