package com.online.travel.planning.online.travel.planning.backend.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.online.travel.planning.online.travel.planning.backend.Model.Packages;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PackagesRepository extends MongoRepository<Packages, String> {
    @Query("{ 'PackageType' : ?0 }")
    List<Packages> findByPackageType(String PackageType);
    List<Packages> findByPackageNameContainingIgnoreCase(String name);
}
