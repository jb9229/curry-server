package com.curry.users;

public class UserNotFoundException extends RuntimeException {
    Long id;
    String email;


    //Constuctor
    public UserNotFoundException(Long id){this.id = id;}

    public UserNotFoundException(String email)
    {
        this.email = email;
    }


    //Method
    public Long getId()
    {
        return this.id;
    }

    public String getEmail()
    {
        return this.email;
    }
}
