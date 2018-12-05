package com.curry.div_account;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * Created by test on 2016-02-13.
 */
public class DivAccountDto {

    @Data
    public static class Response{
        private Long id;
        private Long oriAccountId;
        private String description;
        private int balance;
    }

    @Data
    public static class Create {
        private Long id;

        private Long oriAccountId;

        @NotBlank
        @Size(max=45)
        private String description;
        private int balance;
    }
}
