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
        @JsonProperty("tran_date")
        private String transDate;
        @JsonProperty("tran_time")
        private String transTime;
        @JsonProperty("inout_type")
        private String inoutType;
        @JsonProperty("tran_amt")
        private Integer tranAmt;
        @JsonProperty("after_balance_amt")
        private Integer transAfterBalance;
        @JsonProperty("branch_name")
        private String branchName;
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
        @JsonProperty("inout_type")
        private String inoutType;
        @NotBlank
        @JsonProperty("tran_amt")
        private Integer tranAmt;
        @NotBlank
        @JsonProperty("after_balance_amt")
        private Integer transAfterBalance;
        @NotBlank
        @JsonProperty("branch_name")
        private String branchName;
    }

    @Data
    public static class Delete {
        private Long id;
    }
}
