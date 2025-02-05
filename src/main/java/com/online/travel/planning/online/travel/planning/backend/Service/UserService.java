package com.online.travel.planning.online.travel.planning.backend.Service;

import com.online.travel.planning.online.travel.planning.backend.Model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {


    User createUser(User user,MultipartFile imagefile)throws IOException;

    User getUserById(String userId);
    User getUserNameById(String userId);
    User getUserByUserEmail(String userEmail);
    List<User> getAllUsers();
    void deleteUser(String userId);
    String sendRecoveryCode(String userEmail);
    boolean verifyRecoveryCode(String userEmail, String recoveryCode);
    User updatePassword(String userEmail, String newPassword);

    User getUserProfile(String email);

    User updateUserProfile(String email, User updatedUser,MultipartFile imagefile)throws IOException;

    //long getOnlineUsersCount();
    List<User> getTravelGuides();
    User promoteUserToGuide(String userId);
    //List<User> getNewCustomers();
}