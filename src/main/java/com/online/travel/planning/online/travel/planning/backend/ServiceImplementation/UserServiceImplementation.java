package com.online.travel.planning.online.travel.planning.backend.ServiceImplementation;


import com.online.travel.planning.online.travel.planning.backend.Model.User;
import com.online.travel.planning.online.travel.planning.backend.Repository.UserRepository;
import com.online.travel.planning.online.travel.planning.backend.Service.Email_Service;
import com.online.travel.planning.online.travel.planning.backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    private UserRepository userRepository;



    @Autowired
    private Email_Service emailService;

    private final Map<String,String> recoveryCodes = new HashMap<>();

    @Override
    public User createUser(User user, MultipartFile imagefile)throws IOException {
        System.out.println("welcome");
        if (user == null || user.getUserEmail() == null || user.getUserEmail().isEmpty()) {
            throw new IllegalArgumentException("Invalid user data. Email is required.");
        }
        //System.out.println(imagefile.getOriginalFilename());
        user.setProfileImagePath(imagefile.getOriginalFilename());
        user.setContentType(imagefile.getContentType());
        user.setImageData(imagefile.getBytes());

        String userEmail = user.getUserEmail();


        String subject = "Welcome to Travel Planning Website!!!";
        String message = "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f4f4f4; }" +
                ".container { max-width: 800px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15); }" +
                ".header { background-color: #4CAF50; color: white; padding: 15px; border-radius: 8px 8px 0 0; text-align: center; }" +
                ".content { padding: 20px; font-size: 16px; color: #333; }" +
                ".content p { line-height: 1.6; }" +
                ".welcome { font-weight: bold; color: #135bf2; font-size: 18px; }" +
                ".footer { margin-top: 20px; padding-top: 15px; border-top: 1px solid #dddddd; text-align: center; font-size: 13px; color: #777; }" +
                ".footer p { margin: 5px 0; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>Welcome to Travel Planning Website!</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Dear <strong>" + user.getFirstName() + " " + user.getLastName() + "</strong>,</p>" +
                "<p>Thank you for registering with us. We are excited to have you on board.</p>" +
                "<p>Enjoy planning your trips and feel free to explore our travel services!</p>" +
                "<br>" +
                "<p>Best Regards,</p>" +
                "<p><strong>Travel Planning Team</strong></p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>&copy; 2024 online-travel-planning LK. All rights reserved.</p>" +
                "<p>If you have any questions, please contact us at <a href='mailto:ceylontravelplanning@gmail.com'>ceylontravelplanning@gmail.com</a></p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        // Send the welcome email
        try {
            emailService.sendEmail(userEmail, subject, message); // Ensure this method supports HTML content
            System.out.println("Email sent successfully to: " + userEmail);
        } catch (Exception e) {
            System.err.println("Failed to send email to: " + userEmail);
            e.printStackTrace();
        }

        return userRepository.save(user);
    }




    @Override
    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }
    @Override
    public User getUserNameById(String userId) {
        return userRepository.findUsernameByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }
    @Override
    public User getUserByUserEmail(String userEmail) {
        Optional<User> optionalUser = userRepository.findByUserEmail(userEmail);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found with email: " + userEmail);
        }

        User user = optionalUser.get();
        String imagePath = user.getProfileImagePath();



        if (imagePath != null && !imagePath.isEmpty()) {
            String fullPath = getAccessibleUrl("http://localhost:8080" + imagePath);
            user.setProfileImagePath(fullPath);
        }

        return user;
    }


    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
          String imagePath=user.getProfileImagePath();
          if(imagePath!=null && !imagePath.isEmpty()) {
              String fullPath=getAccessibleUrl("http://localhost:8080" +imagePath);
              user.setProfileImagePath(fullPath);
          }
        }
        return userRepository.findAll();
    }


    @Override
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public String sendRecoveryCode(String userEmail) {
        // Check if the email is valid and user exists
        Optional<User> optionalUser = userRepository.findByUserEmail(userEmail);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("No user found with email: " + userEmail);
        }

        User user = optionalUser.get();

        // Generate a 6-digit recovery code
        String recoveryCode = String.format("%06d", new Random().nextInt(999999));

        // Store the recovery code temporarily in the map
        recoveryCodes.put(userEmail, recoveryCode);

        // Log for debugging
        //System.out.println("Stored recovery code for email " + userEmail + ": " + recoveryCode);

        // Prepare the email content
        String subject = "Password Recovery Code!";
        String message =
                "<html>" +
                        "<head>" +
                        "<style>" +
                        "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f4f4f4; }" +
                        ".container { max-width: 600px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15); }" +
                        ".header { background-color: #00ff99; color: white; padding: 15px; text-align: center; border-radius: 8px 8px 0 0; }" +
                        ".content { font-size: 16px; color: #333; }" +
                        ".recovery-code { font-weight: bold; font-size: 20px; color: #135bf2; }" +
                        ".footer { margin-top: 20px; text-align: center; font-size: 12px; color: #777; }" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<div class='container'>" +
                        "<div class='header'><h2>Password Recovery</h2></div>" +
                        "<div class='content'>" +
                        "<p>Dear " + user.getFirstName() + " " + user.getLastName() + ",</p>" +
                        "<p>We received a request to reset the password for your account. Use the recovery code below to proceed:</p>" +
                        "<p class='recovery-code'>" + recoveryCode + "</p>" +
                        "<p>If you did not request this, please contact our support team immediately.</p>" +
                        "</div>" +
                        "<div class='footer'>" +
                        "<p>&copy; 2024 online-travel-planning LK. All rights reserved.</p>" +
                        "<p>For support, email us at <a href='mailto:ceylontravelplanning@gmail.com'>ceylontravelplanning@gmail.com</a></p>" +
                        "</div>" +
                        "</div>" +
                        "</body>" +
                        "</html>";

        // Send the recovery email
        emailService.sendEmail(userEmail, subject, message);

        return recoveryCode; // Optionally return for testing purposes
    }


    @Override
    public boolean verifyRecoveryCode(String userEmail, String recoveryCode) {
        // Retrieve the stored recovery code
        String storedCode = recoveryCodes.get(userEmail);

        // Log for debugging
        //System.out.println("UserEmail: " + userEmail + ", Entered RecoveryCode: " + recoveryCode + ", StoredCode: " + storedCode);

        // Check if the entered code matches the stored code
        if (storedCode != null && storedCode.trim().equals(recoveryCode.trim())) {
             // Remove the code after successful verification
            return true;
        }

        return false;
    }



    @Override
    public User updatePassword(String userEmail, String newPassword) {
        Optional<User> optionalUser = userRepository.findByUserEmail(userEmail);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("No user found with email: " + userEmail);
        }

        User user = optionalUser.get();
        user.setPassword(newPassword); // Update password

        //email message
        String subject = "Your Account Password Changed!";
        String message ="<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f4f4f4; }" +
                ".image { width: 50px; height: 50px; display: block; margin: 0 auto; }" +
                ".container { max-width: 800px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15); }" +
                ".header { background-color: #00ff99; color: white; padding: 15px; border-radius: 8px 8px 0 0; text-align: center; }" +
                ".content { padding: 20px; font-size: 16px; color: #333; }" +
                ".content p { line-height: 1.6; }" +
                ".recovery-code { font-weight: bold; color: #135bf2; font-size: 18px; }" +
                ".footer { margin-top: 20px; padding-top: 15px; border-top: 1px solid #dddddd; text-align: center; font-size: 13px; color: #777; }" +
                ".footer p { margin: 5px 0; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'><h2>Password Change</h2></div>" +
                "<div class='content'>" +
                "<p>Dear " + optionalUser.get().getFirstName() + " " + optionalUser.get().getLastName()+ ",</p>" +
                "<p>you change to  the password for your account. Succesfull!!</p>" +
                "<p>If you did not request this change, please contact our support team immediately.</p>" +
                "<p>Warm regards,</p>" +
                "<p><strong>online-travel-planning Support Team</strong></p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>&copy; 2024 online-travel-planning LK. All rights reserved.</p>" +
                "<p>If you have any questions, please contact us at ceylontravelplanning@gmail.com</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        // Send the welcome email
        emailService.sendEmail(userEmail, subject, message);

        return userRepository.save(user);
    }

    @Override
    public User getUserProfile(String email) {
        Optional<User> getuser = userRepository.findByUserEmail(email);
        String imagePath = getuser.get().getProfileImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            String fullPath = getAccessibleUrl("http://localhost:8080" + imagePath);
            getuser.get().setProfileImagePath(fullPath);
        }


        return getuser.orElse(null);
    }

    private String getAccessibleUrl(String... urls) {
        for (String url : urls) {
            if (isUrlAccessible(url)) {
                return url;
            }
        }
        return null; // or handle it if neither URL is accessible
    }

    private boolean isUrlAccessible(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return (responseCode == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public User updateUserProfile(String userEmail, User userDetails, MultipartFile imageFile) throws IOException {
        System.out.println("Updating user profile with email: " + userEmail);

        // Use orElseThrow to handle the case when the user is not found
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("No user found with email: " + userEmail));

        // Update user details with new data
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        user.setTitle(userDetails.getTitle());
        user.setGender(userDetails.getGender());
        user.setCountry(userDetails.getCountry());

        // Update profile image if provided
        if (imageFile != null && !imageFile.isEmpty()) {
            // Assuming you have a method to save the file and get the file name
            user.setProfileImagePath(imageFile.getOriginalFilename());
            user.setContentType(imageFile.getContentType());
            user.setImageData(imageFile.getBytes());  // Storing image as byte array, but might be better to store only file path
        }

        // Save and return updated user
        return userRepository.save(user);
    }



   /* @Override
    public long getOnlineUsersCount() {
        return userRepository.
    }*/

   /* @Override
    public List<User> getNewCustomers() {
        // Get today's date
        LocalDate today = LocalDate.now();
        // Fetch users based on their registration date (latest first)
        return userRepository.findTop5ByDateRegisteredOrderByDateRegisteredDesc(today);
    }*/

    @Override
    public User promoteUserToGuide(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUserRole("travelGuide"); // Update the role
           return userRepository.save(user); // Save the updated user

        }
        else {
            return null;
        }

    }
    @Override
    public List<User> getTravelGuides() {
        // Fetch users with the "travelGuide" role
        List<User> users = userRepository.findByUserRole("travelGuide");



        return users;
    }





}
