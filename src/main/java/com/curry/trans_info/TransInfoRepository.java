package com.curry.trans_info;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by test on 2018-12-05.
 */
public interface TransInfoRepository extends JpaRepository<TransInfo, Long>, JpaSpecificationExecutor<TransInfo> {

    List<TransInfo> findByDivAccIdInAndTransDateGreaterThanEqualAndTransDateLessThanEqual(List<Long> divAccId, String transFromDate, String transToDate);
}
