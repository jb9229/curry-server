package com.curry.ori_account;

import lombok.Data;

/**
 * Created by test on 2018-11-08.
 */
public class OriAccountDto {
    @Data
    public static class Response{
        private Long id;

        private String description;
        private String pintecUseNum;
        private String divAccountDescription;
    }
}
