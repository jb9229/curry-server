package com.curry.div_account;

/**
 * Created by jeong on 2016-04-06.
 */
public class DivAccountNotFoundException extends RuntimeException {
    Long id;

    public DivAccountNotFoundException(Long id){
        this.id     =   id;
    }


    public Long getId() {
        return id;
    }

}