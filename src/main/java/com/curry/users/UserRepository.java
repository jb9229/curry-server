package com.curry.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    Page<User> findAll(Specification<User> spec, Pageable pageable);

    @Query(value ="select a.adtsOrderNum from Account a where a.id = :accountID")
    Integer getOrderNumAdTS(@Param("accountID") Long accountID);

    @Modifying
    @Query(value ="update Account a set a.adtsOrderNum = :nextOrderNum where a.id = :accountID")
    void setNextOrderNumAdTS(@Param("accountID") Long accountID, @Param("nextOrderNum") Integer nextOrderNum);


    //    @Query( value="select a from User a left join DivAccount s on a.id = s.accountId where s.bowl.id = :bowlId")
    //    Page<User> findByBowl(@Param("bowlId") Long bowlId, Pageable pageable);
}
