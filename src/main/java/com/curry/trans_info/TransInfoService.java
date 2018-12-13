package com.curry.trans_info;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by test on 2018-12-05.
 */
@Service
public class TransInfoService {

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private TransInfoRepository repository;

    public List<TransInfo> createTransInfo(List<TransInfoDto.Create> createList) {
        List<TransInfo> transInfoList = createList.parallelStream()
                .map(create -> modelMapper.map(create, TransInfo.class))
                .collect(Collectors.toList());


        return this.repository.save(transInfoList);
    }

    public void delete(List<TransInfoDto.Delete> deleteList) {
        List<TransInfo> transInfoList = deleteList.parallelStream()
                .map(create -> modelMapper.map(create, TransInfo.class))
                .collect(Collectors.toList());

        repository.delete(transInfoList);
    }
}
