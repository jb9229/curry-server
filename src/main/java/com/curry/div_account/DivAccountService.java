package com.curry.div_account;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by test on 2016-01-31.
 */
@Service
@Transactional
@Slf4j
public class DivAccountService {

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private DivAccountRepository repository;



    public void deleteEstimate(Long id) {
        repository.delete(getEstimate(id));
    }


    public DivAccount getEstimate(Long id){


        DivAccount divAccount =   repository.findOne(id);

        if(divAccount == null)
        {
            throw new EstimateNotFoundException(id);
        }


        return divAccount;
    }


    public Page<DivAccount> getSpoons(Specification<DivAccount> spec, Pageable pageable){


        Page<DivAccount> page                =   repository.findAll(spec, pageable);


        return page;
    }


    public DivAccount createDivAccount(DivAccountDto.Create divAccDto) {

        DivAccount divAccount = this.modelMapper.map(divAccDto, DivAccount.class);


        return this.repository.save(divAccount);

    }
}
