package com.ecom.admin.service;

import com.ecom.admin.exception.UserNotFoundException;
import com.ecom.admin.repository.user.RoleRepository;
import com.ecom.admin.repository.user.UserRepository;
import com.ecom.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {
    public static final int USER_PER_PAGE = 3;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public Page<User> getByPage(int page){
        Pageable pageable = PageRequest.of(page - 1, USER_PER_PAGE);
        return userRepository.findAll(pageable);
    }

    public User save(User user){
        boolean isUpdateUser = (user.getId() != null);

        if(isUpdateUser){
            if(user.getPassword().isEmpty()){
                User currentUser = userRepository.findById(user.getId()).get();
                user.setPassword(currentUser.getPassword());
            }else{
                encodePassword(user);
            }
        }else{
            encodePassword(user);
        }

        return userRepository.save(user);
    }

    public User updateAccount(User userInForm){
        User userInDB = userRepository.findById(userInForm.getId()).get();

        if(!userInForm.getPassword().isEmpty()){
            userInDB.setPassword(userInForm.getPassword());
            encodePassword(userInDB);
        }

        if(userInForm.getPhotos() != null){
            userInDB.setPhotos(userInForm.getPhotos());
        }

        userInDB.setFirstName(userInForm.getFirstName());
        userInDB.setLastName(userInForm.getLastName());

        return userRepository.save(userInDB);
    }

    public User getUserByEmail(String email){
        return userRepository.getUserByEmail(email);
    }

    public void encodePassword(User user){
        String passwordEncode = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordEncode);
    }

    public boolean checkEmailUnique(Integer id, String email){
        User user = userRepository.getUserByEmail(email);

        if(user == null) return true;

        boolean isCreateUser = (id == null);

        if(isCreateUser){
            if(user != null) return false;
        }else{
            if(user.getId() != id) return false;
        };

        return true;
    }

    public User get(Integer id) throws UserNotFoundException {
        try {
            return userRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new UserNotFoundException("Could not find any user with ID " + id);
        }
    }

    public void delete(Integer id) throws UserNotFoundException {
        Long counById = userRepository.countById(id);
        if(counById == 0 || counById == null){
            throw new UserNotFoundException("Could not find any user with ID ");
        }

        userRepository.deleteById(id);
    }

    public void updateEnabledStatus(Integer id, Boolean enable){
        userRepository.updateEnabledStatus(id, enable);
    }
}
