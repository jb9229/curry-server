package com.curry.ori_account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by test on 2018-11-08.
 */
public interface OriAccountRepository extends JpaRepository<OriAccount, Long>, JpaSpecificationExecutor<OriAccount> {
    List<OriAccount> findByUserId(Long oriAccountId);
}
