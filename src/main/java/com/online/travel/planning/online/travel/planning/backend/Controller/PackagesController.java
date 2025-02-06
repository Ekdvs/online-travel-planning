package com.online.travel.planning.online.travel.planning.backend.Controller;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.online.travel.planning.online.travel.planning.backend.Model.Packages;
import com.online.travel.planning.online.travel.planning.backend.Service.PackagesService;

import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin("https://ceylon-travelernetlifyapp.vercel.app/")
@RequestMapping("/packages")
public class PackagesController {

     @Autowired
    private PackagesService packagesService;



    @GetMapping("/getAllPackages")
    public List<Packages> getAllPackages() {
        return packagesService.getAllPackages();
    }

    @GetMapping("/searchPackage")
    public ResponseEntity<List<Packages>> searchPackage(@RequestParam String name) {
        List<Packages> packages=packagesService.searchPackagesByName(name);
        return ResponseEntity.ok(packages);
    }

    @GetMapping("/getPackageById/{id}")
    public ResponseEntity<Packages> getPackageById(@PathVariable("id") String packageId) {
        Optional<Packages> packageOptional = packagesService.getPackageById(packageId);
        return packageOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/getPackageByType/{packageType}")
    public List<Packages> getPackageByType(@PathVariable("packageType") String packageType) {
        return packagesService.getPackagesByPackageType(packageType);
    }
    @PostMapping("/addPackage")
    public ResponseEntity<?> createPackage(
            @RequestPart("package") String packageJson,
            @RequestPart("imageFile") MultipartFile imageFile) throws IOException {
        try {
            // Deserialize the package JSON (no "imageFile" here)
            ObjectMapper objectMapper = new ObjectMapper();
            Packages packages = objectMapper.readValue(packageJson, Packages.class);

            // Handle package creation logic (process the image file)
            Packages createdPackage = packagesService.createPackage(packages, imageFile);

            return new ResponseEntity<>(createdPackage, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    @PutMapping("/updatePackage/{id}")
    public ResponseEntity<?> updatePackage(
            @PathVariable("id") String packageId,
            @RequestPart("package") String packageJson,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Packages updatedPackage = objectMapper.readValue(packageJson, Packages.class);
            Packages savedPackage = packagesService.updatePackage(packageId, updatedPackage, imageFile);
            return new ResponseEntity<>(savedPackage, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Package not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>("Error processing request: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/deletePackage/{id}")
    public String deletePackage(@PathVariable("id") String id) {
        packagesService.deletePackage(id);
        return "Package Deleted with ID: " + id;
    }








}
