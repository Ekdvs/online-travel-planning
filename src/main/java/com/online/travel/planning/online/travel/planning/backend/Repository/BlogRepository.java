package com.online.travel.planning.online.travel.planning.backend.Repository;

import com.online.travel.planning.online.travel.planning.backend.Model.Blog;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface BlogRepository extends MongoRepository<Blog,String> {
}
