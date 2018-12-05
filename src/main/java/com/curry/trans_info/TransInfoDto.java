package com.curry.trans_info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

/**
 * Created by test on 2018-12-05.
 */
public class TransInfoDto {
    @Data
    public static class Response{
        private Long id;
        private Long divAccId;
        private String transDate;
        private String transTime;
        private String transAfterBalance;
    }

    @Data
    public static class Create {
        private Long id;

        @NotNull
        private Long divAccId;

        @NotBlank
        @JsonProperty("tran_date")
        private String transDate;
        @NotBlank
        @JsonProperty("tran_time")
        private String transTime;
        @NotBlank
        @JsonProperty("after_balance_amt")
        private String transAfterBalance;
    }
}
