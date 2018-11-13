package com.curry.common;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by test on 2018-07-21.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class CommonController {
    private final S3Uploader s3Uploader;


    @RequestMapping(value="/common/image/upload", method = RequestMethod.POST )
    public String uploadProfilePhoto(@RequestParam("img")MultipartFile img) throws IOException {


        return s3Uploader.upload(img, "isa/img");



//        File newFile    =   null;
//        if(!img.isEmpty()){
//            try{
//                System.out.println(assetPath + imgName);
//                newFile     =   new File(assetPath + imgName);
//                if(newFile.createNewFile()){}
//
//                BufferedOutputStream stream     =   new BufferedOutputStream(new FileOutputStream(newFile));
//
//
//                FileCopyUtils.copy(img.getInputStream(), stream);
//
//
//                stream.close();
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//        }


//        return assetUri + "img/profile/" + newFile.getName();
    }
}
