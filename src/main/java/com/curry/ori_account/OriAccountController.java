package com.curry.ori_account;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by test on 2018-11-08.
 */

@RestController
@RequestMapping("/api/v1/")
public class OriAccountController {

    @Autowired
    private OriAccountRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(value="/ori_account/{userId}", method = RequestMethod.GET)
    public ResponseEntity getDivAccount(@PathVariable Long userId){

        List<OriAccount> divAccountList       =      repository.findByUserId(userId);


        List<OriAccountDto.Response> content = divAccountList.parallelStream()
                .map(divAccounts -> modelMapper.map(divAccounts, OriAccountDto.Response.class))
                .collect(Collectors.toList());


        return new ResponseEntity<>(content, HttpStatus.OK);
    }
}
