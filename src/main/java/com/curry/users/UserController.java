package com.curry.users;

import com.curry.common.EmailSender;
import com.curry.common.Email;
import com.curry.common.ErrorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by test on 2015-10-18.
 */
@RestController
@RequestMapping("/api/v1/")
public class UserController {
    public static final int INIT_PASSWORD_DIGIT     =   8;

    public static final String REGIST_MAIL_TITLE        =   "[십시일반]가입을 축하 드립니다, 이메일 인증을 해 주세요";
    public static final String INIT_MAIL_TITLE          =   "[십시일반] 비밀번호 초기화 했습니다";


    @Autowired
    private MessageSource messageSource;


    @Autowired
    private UserService service;

    @Autowired
    private UserRepository repository;

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private EmailSender emailSender;



    @RequestMapping(value="/users", method = RequestMethod.POST)
    public ResponseEntity createAccount(@RequestBody @Valid UserDto.Create create, BindingResult result) throws MessagingException {

        if(result.hasErrors())
        {
            ErrorResponse errorResponse =   new ErrorResponse();

            errorResponse.setMessage(result.toString());
            errorResponse.setCode("bed.request");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        double authMailKey  =   Math.random();
        create.setAuthMailKey(authMailKey);

        User newUser =   service.createAccount(create);



        Email authenMail    =   new Email();
        authenMail.setSubject(REGIST_MAIL_TITLE);
        authenMail.setReceiver(newUser.getEmail());
        authenMail.setContent("<p> 십시일반 가입을 축하 드립니다, 하기 링크로 이메일 인증을 완료 해 주세요.</p> <a href='http://tenspoon.elasticbeanstalk.com/api/v1/users/auth/" + newUser.getEmail() + "/" + authMailKey + "'>이메일 인증하러 가기</a>");

        emailSender.sendMail(authenMail);


        return new ResponseEntity<>(modelMapper.map(newUser, UserDto.Response.class), HttpStatus.CREATED);
    }


    @RequestMapping(value = "/users/auth/{email}/{key}", method = RequestMethod.GET)
    public ResponseEntity authAccountMail(@PathVariable String email, @PathVariable Double key){
        User user =   service.getAccount(email);

        user.setAuthMailkey(null);

        User updateUser =   repository.save(user);

        return new ResponseEntity<>(modelMapper.map(updateUser, UserDto.Response.class),
                HttpStatus.OK);

    }

    @RequestMapping(value="/users/bowl/{bowlId}", method = GET)
    public ResponseEntity getBowlAccounts(@PathVariable Long bowlId, Pageable pageable){


        //TODO findByBow 문제 해결
//        Page<User> page              =      repository.findByBowl(bowlId, pageable);

        Page<User> page              = null;

        List<UserDto.Response> content = page.getContent().parallelStream()
                .map(newAccount -> modelMapper.map(newAccount, UserDto.Response.class))
                .collect(Collectors.toList());


        PageImpl<UserDto.Response> result    =   new PageImpl<>(content, pageable, page.getTotalElements());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @RequestMapping(value="/users", method = GET)
    public ResponseEntity getAccounts(User user, Pageable pageable){

//        Specification<User> spec     =   Specifications.where(UserSpecs.emailEqual(user.getEmail()));//spec  =   spec.and()



//        Page<User> page              =      repository.findAll(spec, pageable);


//        List<UserDto.Response> content = page.getContent().parallelStream()
//                .map(newAccount -> modelMapper.map(newAccount, UserDto.Response.class))
//                .collect(Collectors.toList());


//        PageImpl<UserDto.Response> result    =   new PageImpl<>(content, pageable, page.getTotalElements());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


    @RequestMapping(value="/users/{id}", method = GET )
    public ResponseEntity getAccount(@PathVariable Long id) {
        User user =   service.getAccount(id);

        UserDto.Response response        =   modelMapper.map(user, UserDto.Response.class);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @RequestMapping(value="/users/password/", method = PUT)
    public ResponseEntity initPassword(@RequestParam("email") String emailAdd) throws Exception{

        User user =   service.getAccount(emailAdd);


        String randomPW             =   service.calRandomPW(INIT_PASSWORD_DIGIT);

        UserDto.Update update    =   new UserDto.Update();
        update.setPassword(randomPW);
        update.setUsername(user.getUsername());


        System.out.println(">>>>>>"+emailAdd);

        Email email     =   new Email();
        email.setSubject(INIT_MAIL_TITLE);
        email.setContent("<div>안녕하세요. 십시일반 입니다." +
                "<br><br>" +
                "고객님의 비밀번호를 초기화 했습니다." +
                "<br><br>" +
                "<ul><li><b>초기화 비번: " + randomPW + "<b></li></ul>" +
                "<br><br>" +
                "<p>감사합니다.</p>" +
                "</div>");
        email.setReceiver(emailAdd);

        emailSender.sendMail(email);


        User updateUser = service.updateAccount(user, update);


        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping(value="/users/{id}", method = PUT)
    public ResponseEntity updateAccount(@PathVariable Long id, @RequestBody @Valid UserDto.Update updateDto, BindingResult result){

        if(result.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user =   repository.findOne(id);

        if(user ==  null)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User updateUser =   service.updateAccount(user, updateDto);

        return new ResponseEntity<>(modelMapper.map(updateUser, UserDto.Response.class),
                HttpStatus.OK);
    }


    @RequestMapping(value="/users/{id}", method = DELETE)
    public ResponseEntity deleteAccount(@PathVariable Long id){
        service.deleteAccount(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



    //Exception Handler Method
    @ExceptionHandler(UserDuplicatedException.class)@ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerUserDuplicatedException(UserDuplicatedException e){
        ErrorResponse   errorResponse   =   new ErrorResponse();
        errorResponse.setMessage("["+ e.getUsername() + "] 중복된 username 입니다.");
        errorResponse.setCode("duplicated.username.exception");

        return errorResponse;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerAccountNotFoundException(UserNotFoundException e){
        ErrorResponse errorResponse  =   new ErrorResponse();
        errorResponse.setMessage("["+ e.getId()+"]에 해당하는 계정이 없습니다.");
        errorResponse.setCode("account.not.found.exception");

        return errorResponse;
    }

    @ExceptionHandler(MessagingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerMessagingException(MessagingException e){
        ErrorResponse errorResponse  =   new ErrorResponse();
        errorResponse.setMessage("메일 전송에 실패 했습니다.");
        errorResponse.setCode("mail.send.fail.exception");

        return errorResponse;
    }

}
