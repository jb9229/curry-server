package com.curry.div_account;

import com.curry.common.ErrorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by test on 2016-01-31.
 */
@RestController
@RequestMapping("/api/v1/")
public class DivAccountController {

    @Autowired
    private EstimateService service;

    @Autowired
    private DivAccountRepository repository;

    @Autowired
    private ModelMapper modelMapper;



    @RequestMapping(value="/div_account", method = RequestMethod.POST)
    public ResponseEntity createDivAccount(@RequestBody @Valid DivAccountDto.Create create, BindingResult result){

        if(result.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        DivAccount divAccount = service.createEstimate(create);


        return new ResponseEntity<>(modelMapper.map(divAccount, DivAccountDto.Response.class), HttpStatus.OK);
    }

    @RequestMapping(value="/div_account/{oriAccountId}", method = GET)
    public ResponseEntity getDivAccount(@PathVariable  Long oriAccountId){

        List<DivAccount> divAccountList       =      repository.findByOriAccountId(oriAccountId);


        List<DivAccountDto.Response> content = divAccountList.parallelStream()
                .map(divAccounts -> modelMapper.map(divAccounts, DivAccountDto.Response.class))
                .collect(Collectors.toList());


        return new ResponseEntity<>(content, HttpStatus.OK);
    }

    @RequestMapping(value="/div_account/{id}/", method = GET)
    public ResponseEntity get(@PathVariable Long id){


        DivAccount divAccount =   service.getEstimate(id);

        DivAccountDto.Response response        =   modelMapper.map(divAccount, DivAccountDto.Response.class);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @RequestMapping(value="/spoon/{id}/", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Long id){
        service.deleteEstimate(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }




    @ExceptionHandler(EstimateNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerSpoonNotFoundException(EstimateNotFoundException e){
        ErrorResponse errorResponse  =   new ErrorResponse();
        errorResponse.setMessage("["+ e.getId()+"]에 해당하는 기부가 없습니다.");
        errorResponse.setCode("spoon.not.found.exception");

        return errorResponse;
    }
}
