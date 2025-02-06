package com.online.travel.planning.online.travel.planning.backend.ServiceImplementation;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.online.travel.planning.online.travel.planning.backend.Model.User;
import com.online.travel.planning.online.travel.planning.backend.Repository.UserRepository;
import com.online.travel.planning.online.travel.planning.backend.Service.Email_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.online.travel.planning.online.travel.planning.backend.Model.Packages;
import com.online.travel.planning.online.travel.planning.backend.Repository.PackagesRepository;
import com.online.travel.planning.online.travel.planning.backend.Service.PackagesService;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PackagesServiceImplementation implements PackagesService {

    @Autowired
    private PackagesRepository packagesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Email_Service emailService;

    @Override
    public List<Packages> getAllPackages() {
        List<Packages> packagesList = packagesRepository.findAll();

        for (Packages packages : packagesList) {
            String imagePath=packages.getPackageImagePath();
            if(imagePath!=null&&!imagePath.isEmpty()){
                String fullpath=getAccessibleUrl("https://online-travel-planning-production.up.railway.app" + imagePath);
                packages.setPackageImagePath(fullpath);
            }
        }
        return packagesList;
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
    public Optional<Packages> getPackageById(String id) {
        return packagesRepository.findById(id);
    }

    @Override
    public List<Packages> getPackagesByPackageType(String packageType) {
        List<Packages> packagesList = packagesRepository.findByPackageType(packageType);

        for (Packages packages : packagesList) {
            String imagePath=packages.getPackageImagePath();
            if(imagePath!=null&&!imagePath.isEmpty()){
                String fullpath=getAccessibleUrl("https://online-travel-planning-production.up.railway.app" + imagePath);
                packages.setPackageImagePath(fullpath);
            }

    }
    return packagesList;
    }

    @Override
    public Packages createPackage(Packages packages, MultipartFile imagefile)throws IOException {

        packages.setPackageImagePath(imagefile.getOriginalFilename());
        packages.setContentType(imagefile.getContentType());
        packages.setImageData(imagefile.getBytes());
        Packages newPackage = packagesRepository.save(packages);

        List<User>allUsers=userRepository.findAll();

        //send message email
        String subject="New Package Added :" +packages.getPackageName();
        String message="";

        allUsers.forEach(user -> {
            if (user.getUserRole().equals("user") || user.getUserRole().equals("GUIDE")) {
                emailService.sendEmail(user.getUserEmail(), subject, message);
            }
        });
        return newPackage;

    }

    @Override
    public Packages updatePackage(String packageId, Packages updatedPackage, MultipartFile imageFile) throws IOException {
        Optional<Packages> existingPackageOpt = packagesRepository.findById(packageId);

        if (!existingPackageOpt.isPresent()) {
            throw new NoSuchElementException("Package with ID " + packageId + " does not exist");
        }

        Packages existingPackage = existingPackageOpt.get();

        // Update package details
        existingPackage.setPackageName(updatedPackage.getPackageName());
        existingPackage.setPackageType(updatedPackage.getPackageType());
        existingPackage.setDescription(updatedPackage.getDescription());
        existingPackage.setOnePersonPrice(updatedPackage.getOnePersonPrice());
        existingPackage.setDuration(updatedPackage.getDuration());
        existingPackage.setLocation(updatedPackage.getLocation());

        // Update image if provided
        if (imageFile != null && !imageFile.isEmpty()) {
            existingPackage.setPackageImagePath(imageFile.getOriginalFilename());
            existingPackage.setContentType(imageFile.getContentType());
            existingPackage.setImageData(imageFile.getBytes());
        }

        return packagesRepository.save(existingPackage);
    }


    @Override
    public void deletePackage(String packageId) {
        packagesRepository.deleteById(packageId);
    }

    @Override
    public List<Packages> searchPackagesByName(String name) {
        return packagesRepository.findByPackageNameContainingIgnoreCase(name);
    }






}
