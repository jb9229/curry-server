package com.curry.div_account;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by test on 2016-01-31.
 */
public interface DivAccountRepository extends JpaRepository<DivAccount, Long>, JpaSpecificationExecutor<DivAccount> {

    List<DivAccount> findByOriAccountId(Long oriAccountId);
    Page<DivAccount> findAll(Specification<DivAccount> spec, Pageable pageable);
}
