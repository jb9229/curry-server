package com.curry.trans_info;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by test on 2018-12-05.
 */
@Entity
@Data
public class TransInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long divAccId;

    private String transDate;
    private String transTime;
    private String inoutType;
    private Integer tranAmt;
    private Integer transAfterBalance;
    private String branchName;
}
