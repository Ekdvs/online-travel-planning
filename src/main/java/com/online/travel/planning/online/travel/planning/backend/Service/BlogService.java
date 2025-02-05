package com.online.travel.planning.online.travel.planning.backend.Service;

import com.online.travel.planning.online.travel.planning.backend.Model.Blog;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface BlogService {
    List<Blog> getAllBlogs();
    Optional<Blog> getBlogById(String blogId);
    Blog addBlog(Blog blog, MultipartFile imagefile)throws IOException;
    Blog updateBlog(String  blogId,Blog blog, MultipartFile imagefile)throws IOException;
    void deleteBlog(String blogId);
}
