package com.online.travel.planning.online.travel.planning.backend.ServiceImplementation;

import com.online.travel.planning.online.travel.planning.backend.Model.Blog;
import com.online.travel.planning.online.travel.planning.backend.Model.User;
import com.online.travel.planning.online.travel.planning.backend.Repository.BlogRepository;
import com.online.travel.planning.online.travel.planning.backend.Repository.UserRepository;
import com.online.travel.planning.online.travel.planning.backend.Service.BlogService;
import com.online.travel.planning.online.travel.planning.backend.Service.Email_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BlogServiceImplementation implements BlogService {
    @Autowired
    private BlogRepository blogRepository;

    

    @Autowired
    private Email_Service emailService;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Blog> getAllBlogs() {
        List<Blog> blogs = blogRepository.findAll();

        for (Blog blog : blogs) {
            String imagePath = blog .getImagePath();
            if (imagePath != null && !imagePath.isEmpty()) {
                String fullPath = getAccessibleUrl("http://localhost:8080" + imagePath);
                blog .setImagePath(fullPath);
            }


        }

        return blogs;
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
    public Optional<Blog> getBlogById(String BlogId) {
        return blogRepository.findById(BlogId);
    }

    @Override
    public void deleteBlog(String blogId) {
        blogRepository.deleteById(blogId);
    }

    @Override
    public Blog updateBlog(String blogId, Blog blogDetails, MultipartFile imageFile) throws IOException {
        Optional<Blog> existingBlogOpt = blogRepository.findById(blogId);

        if (!existingBlogOpt.isPresent()) {
            throw new NoSuchElementException("Event with ID " + blogId + " not found.");
        }

        Blog blog = existingBlogOpt.get();

        // Update Blog details
        blog.setDescription(blogDetails.getDescription());
        blog.setTitle(blogDetails.getTitle());
        blog.setAuthor(blogDetails.getAuthor());



        // Add any additional attributes here.

        // Update image file if provided
        if (imageFile != null && !imageFile.isEmpty()) {
            blog.setImagePath(imageFile.getOriginalFilename());
            blog.setContentType(imageFile.getContentType());
            blog.setImageData(imageFile.getBytes());

        }

        // Save the updated event
        return blogRepository.save(blog);
    }

    @Override
    public Blog addBlog(Blog blog, MultipartFile imagefile)throws IOException {

        blog.setImagePath(imagefile.getOriginalFilename());
        blog.setContentType(imagefile.getContentType());
        blog.setImageData(imagefile.getBytes());
        Blog neweblog= blogRepository.save(blog);
        List<User> allUsers = userRepository.findAll();
// Prepare the email content
        String subject = "New Blog Added: " +blog.getTitle();
        String message =
                "<html>" +
                        "<head>" +
                        "<style>" +
                        "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f4f4f4; }" +
                        ".container { max-width: 800px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15); }" +
                        ".header { background-color: #135bf2; color: white; padding: 15px; border-radius: 8px 8px 0 0; text-align: center; }" +
                        ".content { padding: 20px; font-size: 16px; color: #333; }" +
                        ".content p { line-height: 1.6; }" +
                        ".footer { margin-top: 20px; padding-top: 15px; border-top: 1px solid #dddddd; text-align: center; font-size: 13px; color: #777; }" +
                        ".footer p { margin: 5px 0; }" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<div class='container'>" +
                        "<div class='header'>" +
                        "<h1 style='color: white;'>New Blog Published!</h1>" +
                        "</div>" +
                        "<div class='content'>" +
                        "<p>Hello,</p>" +
                        "<p>We are excited to share our latest blog post with you:</p>" +
                        "<p><strong>Title: " + blog.getTitle() + "</strong></p>" +
                        "<p><strong>Blog ID:</strong> " + blog.getBlogId() + "</p>" +
                        "<p>Description:</p>" +
                        "<p>" + blog.getDescription() + "</p>" +
                        "<p><strong>Author:</strong> " + blog.getAuthor() + "</p>" +
                        "<br>" +
                        "<p>Check out the full blog now and get inspired!</p>" +
                        "<p><strong>Your Travel Planning Team</strong></p>" +
                        "</div>" +
                        "<div class='footer'>" +
                        "<p>&copy; 2024 online-travel-planning LK. All rights reserved.</p>" +
                        "<p>Contact us at ceylontravelplanning@gmail.com</p>" +
                        "</div>" +
                        "</div>" +
                        "</body>" +
                        "</html>";

        allUsers.forEach(user -> {
            if (user.getUserRole().equals("user") || user.getUserRole().equals("GUIDE")) {
                emailService.sendEmail(user.getUserEmail(), subject, message);
            }
        });



        return neweblog;

    }



}
