package com.online.travel.planning.online.travel.planning.backend.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.travel.planning.online.travel.planning.backend.Model.User;
import com.online.travel.planning.online.travel.planning.backend.Repository.UserRepository;
import com.online.travel.planning.online.travel.planning.backend.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@CrossOrigin("https://ceylon-travelernetlifyapp.vercel.app/")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*// Endpoint to fetch new customers
    @GetMapping("/newCustomers")
    public ResponseEntity<List<User>> getNewCustomers() {
        List<User> newCustomers = userService.getNewCustomers();
        return ResponseEntity.ok(newCustomers);
    }*/
   /* @GetMapping("/usersOnline")
    public long getUsersOnline() {
        return userService.getOnlineUsersCount();  // Fetch and return the online user count
    }*/

    // Create a new user
    // Create a new user
    // Create a new user
    @PostMapping("/addUser")
    public ResponseEntity<?> createUser(@RequestPart("user")String userJson,@RequestPart("imageFile")MultipartFile imagefile) throws IOException {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(userJson, User.class);
            User userExist=userService.createUser(user,imagefile);
            return new ResponseEntity<>(userExist, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }



    // Get user by ID
    @GetMapping("/getUserById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") String userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }
    @GetMapping("/getUserNameByID/{id}")
    public String getUsernameById(@PathVariable("id") String userId) {
        User user = userService.getUserNameById(userId);
        String userName=user.getFirstName();
        return userName;
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestPart("user") String userJson,
            @RequestPart("imageFile") MultipartFile imageFile
    ) {
        try {
            // Parse the JSON string into a User object
            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(userJson, User.class);

            // Check if the email is already registered
            Optional<User> existingUser = userRepository.findByUserEmail(user.getUserEmail());
            if (existingUser.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User already registered with this email");
            }

            // Hash the user's password
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);

            userService.createUser(user,imageFile);

            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user data: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the image file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }




    @GetMapping("/getUserByEmail/{id}")
    public User getUserByEmail(@PathVariable("id") String userEmail) {
        //System.out.println(userEmail);
        return userService.getUserByUserEmail(userEmail);
    }

    // Get all users
    @GetMapping("/allUsers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Delete user by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User with ID " + userId + " has been deleted successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user, HttpSession session) {
        // Find user by email
        Optional<User> existingUser = userRepository.findByUserEmail(user.getUserEmail());

        if (existingUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        }

        // Check password
        if (!passwordEncoder.matches(user.getPassword(), existingUser.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        }

        // Store user in session
        session.setAttribute("user", existingUser);

        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("userEmail", existingUser.get().getUserEmail());
        response.put("role", existingUser.get().getUserRole());
        //System.out.println(existingUser.getUserRole());

        return ResponseEntity.ok(response);
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate(); // Destroy the session
        return ResponseEntity.ok("Logged out successfully");
    }


    @PostMapping("/sendotpcode")
    public String sendRecoveryCode(@RequestBody Map<String, String> payload) {
        String userEmail = payload.get("userEmail");
        if (userEmail == null || userEmail.isBlank()) {
            throw new RuntimeException("Email cannot be empty.");
        }
        return userService.sendRecoveryCode(userEmail);
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Map<String, Object>> verifyRecoveryCode(@RequestBody Map<String, String> payload) {
        String userEmail = payload.get("userEmail");
        String recoveryCode = payload.get("recoveryCode");

        Map<String, Object> response = new HashMap<>();

        // Validate that both userEmail and recoveryCode are provided
        if (userEmail == null || recoveryCode == null) {
            response.put("success", false);
            response.put("message", "Both userEmail and recoveryCode are required.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            // Verify if the recovery code matches the stored one
            boolean isCodeValid = userService.verifyRecoveryCode(userEmail, recoveryCode);

            if (isCodeValid) {
                response.put("success", true);
                response.put("message", "OTP verified successfully.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Invalid OTP. Please try again.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "An error occurred while verifying the OTP.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }




    @PostMapping("/update-password/{email}")
    public ResponseEntity<String> updatePassword(
            @PathVariable("email") String userEmail,
            @RequestBody Map<String, String> payload) {

        // Extract newPassword from the request body
        String newPassword = payload.get("Password");

        // Validate the newPassword input
        if (newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("New password must not be empty.");
        }

        try {
            // Call the service to update the password with password encoding
            String encodedPassword = passwordEncoder.encode(newPassword);
            User user = userService.updatePassword(userEmail, encodedPassword);

            // Check if the user was found and the password updated
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found or password could not be updated.");
            }

            // Return success message
            return ResponseEntity.ok("Password updated successfully.");
        } catch (RuntimeException e) {
            // Handle any exceptions thrown by the service
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the password: " + e.getMessage());
        }
    }




    @PutMapping("/{userEmail}")
    public ResponseEntity<?> updateUserProfile(@PathVariable("userEmail") String userEmail,
                                               @RequestPart("user") String userJson,
                                               @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            User userUpdate = objectMapper.readValue(userJson, User.class);
            User updatedUser = userService.updateUserProfile(userEmail, userUpdate, imageFile);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }









    // Get user profile by email
    @GetMapping("/{email}")
    public ResponseEntity<User> getUserProfile(@PathVariable String email) {
        User user = userService.getUserProfile(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(user);
    }
    @PutMapping("/travelgudie/{userId}")
    public ResponseEntity<String> promoteToGuide(@PathVariable String userId) {
        try {
            // Call service method to update the user's role
            User newuser = userService.promoteUserToGuide(userId);
            if (newuser!=null) {
                return ResponseEntity.ok("User successfully promoted to Travel Guide");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error promoting user to Travel Guide");
        }
    }

    //get all travel guides
    @GetMapping("/travel-guides")
    public List<User> getTravelGuides() {
        // Fetch and return travel guides from the service
        return userService.getTravelGuides();
    }


}



    /*@Value("${profile-pic.upload-dir}")
    private String uploadDir; // The directory to save the profile pictures

    @PutMapping("/{email}")
    public ResponseEntity<Map<String, String>> updateUserProfile(@PathVariable String email,
                                                                 @RequestParam Map<String, String> formData,
                                                                 @RequestParam(required = false) MultipartFile profilePicture) {
        Map<String, String> response = new HashMap<>();

        try {
            if (profilePicture != null && !profilePicture.isEmpty()) {
                String fileName = StringUtils.cleanPath(profilePicture.getOriginalFilename());
                Path uploadPath = Paths.get(uploadDir);

                // Create the directory if it does not exist
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Save the file
                Path targetLocation = uploadPath.resolve(fileName);
                Files.copy(profilePicture.getInputStream(), targetLocation);

                // Store the image URL in the database (e.g., the file path or URL)
                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/uploads/")
                        .path(fileName)
                        .toUriString();

                // You can save the fileDownloadUri in your database (User's profile picture URL)
                response.put("profilePicture", fileDownloadUri);
            }

            // Update other user information as needed
            response.put("message", "User profile updated successfully!");
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            response.put("message", "Could not upload the file. Please try again!");
            return ResponseEntity.status(500).body(response);
        }
    }*/





