package com.online.travel.planning.online.travel.planning.backend.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.online.travel.planning.online.travel.planning.backend.Model.Packages;
import org.springframework.web.multipart.MultipartFile;

public interface PackagesService {
    List<Packages> getAllPackages();
    Packages createPackage(Packages packages, MultipartFile imagefile)throws IOException;
    Packages updatePackage(String packageId, Packages packages, MultipartFile imagefile)throws IOException;
    void deletePackage(String packageId);
    Optional <Packages> getPackageById(String packageId);
    List<Packages> searchPackagesByName(String packageName);
    List<Packages> getPackagesByPackageType(String PackageType);
    
    
    
    
    

}
