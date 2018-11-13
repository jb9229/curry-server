package com.curry.users;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public static final int ORDERNUMBER_ADTS_RETURN  =   1;

    @Autowired
    private UserRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public User createAccount(UserDto.Create dto)
    {
        User user = this.modelMapper.map(dto, User.class);

        String username = dto.getUsername();
        if (this.repository.findByUsername(username) != null) {
            throw new UserDuplicatedException(username);
        }
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        Date date = new Date();
        user.setJoined(date);
        user.setUpdated(date);

        return this.repository.save(user);
    }

    public User getAccount(Long id)
    {
        User user = (User)this.repository.findOne(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return user;
    }

    public User getAccount(String email)
    {
        User user = this.repository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException(email);
        }
        return user;
    }

    public User updateAccount(User user, UserDto.Update updateDto)
    {
        user.setUsername(updateDto.getUsername());

        return (User)this.repository.save(user);
    }

    public User updateRiceAccount(User user, UserDto.RiceUpdate updateDto)
    {
        user.setRiceTol(updateDto.getRiceTol());
        user.setRiceMonth(updateDto.getRiceMonth());
        user.setRiceYear(updateDto.getRiceYear());
        user.setRiceTemp(updateDto.getRiceTemp());

        return (User)this.repository.save(user);
    }

    public void deleteAccount(Long id)
    {
        this.repository.delete(getAccount(id));
    }

    public int addRice(User user, UserDto.RiceUpdate riceUpdate, int rice)
    {
        riceUpdate.setRiceTol(user.getRiceTol() + rice);
        riceUpdate.setRiceMonth(user.getRiceMonth() + rice);
        riceUpdate.setRiceYear(user.getRiceYear() + rice);

        riceUpdate.setRiceTemp(user.getRiceTemp() + rice);


        int donationRice    =   0;
        if (riceUpdate.getRiceTemp() > 100)
        {
            double donationRiceOri = riceUpdate.getRiceTemp() / 100;

            donationRice = (int)donationRiceOri;

            donationRice *= 100;



            riceUpdate.setRiceTemp(user.getRiceTemp() - donationRice);
        }
        else
        {
            donationRice = 0;
        }
        return donationRice;
    }

    public String calRandomPW(int length)
    {
        int index = 0;

        char[] charSet = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };




        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++)
        {
            index = (int)(charSet.length * Math.random());
            sb.append(charSet[index]);
        }
        return sb.toString();
    }

}
