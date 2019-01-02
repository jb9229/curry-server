package com.curry.trans_info;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by test on 2018-12-05.
 */
@RestController
@RequestMapping("/api/v1/")
public class TransInfoController {

    @Autowired
    private TransInfoService service;

    @Autowired
    private TransInfoRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(value="/transinfo", method = RequestMethod.POST)
    public ResponseEntity saveOrUpdate(@RequestBody @Valid List<TransInfoDto.Create> createList, BindingResult result){

        if(result.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<TransInfo> transInfoList = service.createTransInfo(createList);

        List<TransInfoDto.Response> content = transInfoList.parallelStream()
                .map(transInfo -> modelMapper.map(transInfo, TransInfoDto.Response.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(modelMapper.map(content, TransInfoDto.Response.class), HttpStatus.OK);
    }

    @RequestMapping(value="/transinfo/{divAccIds}", method = RequestMethod.GET)
    public ResponseEntity get(@PathVariable List<Long> divAccIds, @RequestParam String transFromDate, @RequestParam String transToDate){

        List<TransInfo> transInfoList       =      repository.findByDivAccIdInAndTransDateGreaterThanEqualAndTransDateLessThanEqualOrderByTransDateDescTransTimeDesc(divAccIds, transFromDate, transToDate); //, transFromDate, transToDate


        List<TransInfoDto.Response> content = transInfoList.parallelStream()
                .map(transInfo -> modelMapper.map(transInfo, TransInfoDto.Response.class))
                .collect(Collectors.toList());


        return new ResponseEntity<>(content, HttpStatus.OK);
    }

    @RequestMapping(value="/transinfo", method = RequestMethod.DELETE)
    public ResponseEntity delete(@RequestBody @Valid List<TransInfoDto.Delete> deleteList){
        service.delete(deleteList);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
