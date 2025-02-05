package com.online.travel.planning.online.travel.planning.backend.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.travel.planning.online.travel.planning.backend.Model.Blog;
import com.online.travel.planning.online.travel.planning.backend.Service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;

    

    @GetMapping("/getAllBlogs")
    public List<Blog> getAllBlogs() {
        return blogService.getAllBlogs();
    }

    @GetMapping("/getBogById/{id}")
    public Optional<Blog> getBlogById(@PathVariable("id") String blogId) {
        return blogService.getBlogById(blogId);
    }

    @PostMapping("/addBlog")
    public ResponseEntity<?> addBlog(@RequestPart("blog")String blogJson, @RequestPart("imageFile") MultipartFile imagefile) throws IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Blog blog = objectMapper.readValue(blogJson, Blog.class);
            Blog blogExist = blogService.addBlog(blog,imagefile);
            return new ResponseEntity<>(blogExist, HttpStatus.CREATED);


        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/updateBlog/{id}")
    public ResponseEntity<?> updateEvent(
            @PathVariable("id") String blogId,
            @RequestPart("blog") String blogJson,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Blog updatedblog = objectMapper.readValue(blogJson, Blog.class);
            Blog blog= blogService.updateBlog(blogId, updatedblog, imageFile);
            return new ResponseEntity<>(blog, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/deleteBlog/{id}")
    public String deleteBlog(@PathVariable("id") String blogId) {
        blogService.deleteBlog(blogId);
        return "Blog deleted with id " + blogId;
    }
}
