package com.curry.account;

import com.curry.users.User;
import com.curry.users.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.curry.users.UserService;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * Created by jeong on 2015-10-20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class UserControllerTest {

    MockMvc mockMvc = null;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    UserService service;

    @Autowired
    private FilterChainProxy filterChainProxy;

    @Autowired
    ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(filterChainProxy)
                .build();
    }



    public static UserDto.Create accountCreateFixture(){
        UserDto.Create createDto = createAccountDto("JinbeomTest", "jinbeomjeongTest@gmail.com");


        return createDto;
    }

    private static UserDto.Create createAccountDto(String userName, String email) {
        UserDto.Create createDto = new UserDto.Create();
        createDto.setUsername(userName);
        createDto.setEmail(email);
        createDto.setPassword("123456");
        createDto.setFemale(false);
        createDto.setSingle(true);
        createDto.setBirth(1983);
        createDto.setResidence("충청북도 영동군");
        return createDto;
    }

    public static UserDto.Create obdonationAccountCreateFixture(){
        UserDto.Create createDto = createAccountDto("tenspoonTest", "obdonationTest@gmail.com");


        return createDto;
    }



    @Test
    public void createAccount() throws Exception {

        UserDto.Create createDto =   obdonationAccountCreateFixture();



        ResultActions result = mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));
        result.andExpect(jsonPath("$.username", is("tenspoon")));

        result.andDo(print());
        result.andExpect(status().isCreated());


        User newUser =   service.getAccount((long)1);

        Double authMailKey  =   newUser.getAuthMailkey();

        assertNotNull(authMailKey);

        ResultActions authMailResult = mockMvc.perform(get("/api/v1/auth/users/" + createDto.getEmail() + "/" + authMailKey));


        authMailResult.andDo(print());
        authMailResult.andExpect(status().isOk());

    }

    @Test
    public void createAccount_BedRequest() throws Exception {
        UserDto.Create createDto = new UserDto.Create();
        createDto.setUsername("   ");
        createDto.setPassword("1234");

        ResultActions result    =   mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        result.andDo(print());
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void createAccout_DuplicatedRequest() throws Exception {

        UserDto.Create createDto =  accountCreateFixture();

        ResultActions result = mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        result.andDo(print());
        result.andExpect(status().isCreated());

        result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));


        result.andDo(print());
        result.andExpect(status().isBadRequest());
    }



    @Test
    public void chekAuthEmailAccount() throws Exception {
        UserDto.Create   createDto   =   accountCreateFixture();
        createDto.setAuthMailKey(0.234);

        User user =   service.createAccount(createDto);

        ResultActions result            =   mockMvc.perform(get("/api/v1/users/"+ user.getId())
                .with(httpBasic(createDto.getEmail(), createDto.getPassword())));

        result.andDo(print());
        result.andExpect(status().isUnauthorized());
    }



    @Test
    public void getAccounts() throws Exception {
        UserDto.Create create        =   accountCreateFixture();

        User user =   service.createAccount(create);

        ResultActions result    =   mockMvc.perform(get("/api/v1/users")
                .param("email", "jinbeomjeong@google.com")
                .param("size", "2")
                .with(httpBasic(create.getEmail(), create.getPassword())));

        result.andDo(print());
        result.andExpect(status().isOk());
    }

    @Test
    public void getAccount() throws Exception {
        UserDto.Create   createDto   =   accountCreateFixture();
        createDto.setAuthMailKey(0.234);

        User user =   service.createAccount(createDto);

        ResultActions result            =   mockMvc.perform(get("/api/v1/users/" + user.getId())
                .with(httpBasic(createDto.getEmail(), createDto.getPassword())));

        result.andDo(print());
        result.andExpect(status().isOk());
    }


    @Test
    public void initPW() throws Exception {
        UserDto.Create   createDto   =   accountCreateFixture();
        createDto.setAuthMailKey(0.234);

        User user =   service.createAccount(createDto);

        ResultActions result            =   mockMvc.perform(put("/api/v1/users/password/")
                .param("email", "jb9229@gmail.com"));

        result.andDo(print());
        result.andExpect(status().isOk());
    }


    @Test
    public void updateAccount() throws Exception {
        UserDto.Create   createDto   =   accountCreateFixture();
        User user =   service.createAccount(createDto);

        UserDto.Update   updateDto   =   new UserDto.Update();
        updateDto.setPassword("changePassword");
        updateDto.setUsername("Jeong Jinbeom");

        ResultActions resultActions     =   mockMvc.perform(put("/api/v1/users/" + user.getId())
                .with(httpBasic(createDto.getEmail(), createDto.getPassword()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)));

        resultActions.andDo(print());
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.username", is("Jeong Jinbeom")));
    }



    @Test
    public void deleteAccount() throws Exception {
        UserDto.Create   createDto   =   accountCreateFixture();
        User user =   service.createAccount(createDto);


        ResultActions resultActions     =   mockMvc.perform(delete("/api/v1/users/" + user.getId())
        .with(httpBasic(createDto.getEmail(), createDto.getPassword())));


        resultActions.andDo(print());
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    public void deleteAccount_BedRequest() throws Exception {
        ResultActions resultActions     =   mockMvc.perform(delete("/api/v1/users/1"));


        resultActions.andDo(print());
        resultActions.andExpect(status().isBadRequest());
    }



    @Test
    public void loginAccount() throws Exception {
        UserDto.Create   createDto   =   accountCreateFixture();
        User user =   service.createAccount(createDto);


        ResultActions resultActions     =   mockMvc.perform(post("/api/v1/auth/login").param("email", "jinbeomjeong@google.com").param("password", "123456"));

        resultActions.andDo(print());
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void logoutAccount() throws Exception {
        UserDto.Create   createDto   =   accountCreateFixture();
        User user =   service.createAccount(createDto);

        ResultActions resultActions     =   mockMvc.perform(get("/api/v1/auth/logout"));

        resultActions.andDo(print());
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void loginOath2() throws Exception {
        UserDto.Create create        =   accountCreateFixture();

        User user =   service.createAccount(create);

        String accessToken = getAccessToken(create.getEmail(), create.getPassword());
        System.out.println(accessToken);

        // @formatter:off
        ResultActions resultActions     =   mockMvc.perform(get("/api/v1/users")
                .header("Authorization", "Bearer " + accessToken)
                .param("email", user.getEmail()));

        resultActions.andDo(print());
        resultActions.andExpect(status().isOk());
        // @formatter:on
    }


    private String getAccessToken(String email, String password) throws Exception {
        String authorization = "Basic "
                + new String(Base64Utils.encode("rest-client:rest-secret".getBytes()));
        String contentType = MediaType.APPLICATION_JSON + ";charset=UTF-8";

        // @formatter:off
        String content = mockMvc
                .perform(
                        post("/oauth/token")
                                .header("Authorization", authorization)
                                .contentType(
                                        MediaType.APPLICATION_FORM_URLENCODED)
                                .param("username", email)
                                .param("password", password)
                                .param("grant_type", "password")
                                .param("scope", "read write trust")
                                .param("client_id", "rest-client")
                                .param("client_secret", "rest-secret"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.access_token", is(notNullValue())))
                .andExpect(jsonPath("$.token_type", is(equalTo("bearer"))))
//                .andExpect(jsonPath("$.refresh_token", is(notNullValue())))
                .andExpect(jsonPath("$.expires_in", is(greaterThan(4000))))
                .andExpect(jsonPath("$.scope", is(equalTo("read trust write"))))
                .andReturn().getResponse().getContentAsString();

        // @formatter:on
        System.out.println(content);
        JSONObject obj = new JSONObject(content);


        String access_token = (String) obj.get("access_token");

        return access_token;
    }
}