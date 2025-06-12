package com.station.admin.user;

import com.station.common.entity.Role;
import com.station.common.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static final int USER_PER_PAGE = 4;

    public List<User> users() {
        return userRepository.findAll();
    }

    public Page<User> listByPage(int pageNumber, String sortField, String sortDir, String keyword) {
        if (sortField == null || sortField.isEmpty()) {
            sortField = "id";
        }

        Sort sort = Sort.by(sortField);
        sort = sortDir.equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, USER_PER_PAGE, sort);

        if (keyword != null) {
            return userRepository.search(keyword, pageable);
        }
        return userRepository.findAll(pageable);
    }

    public List<Role> roles() {
        return roleRepository.findAll();
    }

    public User saveUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (user.getId() != null) {
            userRepository.findById(user.getId()).ifPresent(existingUser -> {
                if (user.getPassword() == null || user.getPassword().isEmpty()) {
                    user.setPassword(existingUser.getPassword());
                } else {
                    encodePassword(user);
                }
            });
        } else {
            encodePassword(user);
        }

        return userRepository.save(user);
    }

    public void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public boolean isEmailUnique(Integer id, String email) {
        User existingUser = userRepository.getUserByEmail(email);
        if (existingUser == null) {
            return true;
        }
        if (id == null) {
            return false;
        }
        return existingUser.getId().equals(id);
    }

    public User getUserById(Integer id) throws UserNotFoundException {

        try {
            return userRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new UserNotFoundException("Could not find user with id: " + id);
        }
    }

    public void delete(Integer id) throws UserNotFoundException {
        Long countById = userRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new UserNotFoundException("Could not find Category with id: " + id);

        }
        userRepository.deleteById(id);
    }

    public void updateUserStatus(Integer id, boolean enabled) {
        userRepository.updateEnabledStatus(id, enabled);
    }

    public void updateUserEnabledStatus(Integer id, boolean enabled) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));

        user.setEnabled(enabled);
        userRepository.save(user);
    }

    public User getByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public User updateAccount(User userInForm) {
        User userInDB = userRepository.findById(userInForm.getId()).get();

        if (!userInForm.getPassword().isEmpty()) {
            userInDB.setPassword(userInForm.getPassword());
            encodePassword(userInDB);
        }
        if (userInForm.getPhotos() != null) {
            userInDB.setPhotos(userInForm.getPhotos());
        }

        if (!userInForm.getFirstName().isEmpty()){
            userInDB.setFirstName(userInForm.getFirstName());
        }
        if (!userInForm.getLastName().isEmpty()){
            userInDB.setLastName(userInForm.getLastName());
        }


        return userRepository.save(userInDB);
    }
}
