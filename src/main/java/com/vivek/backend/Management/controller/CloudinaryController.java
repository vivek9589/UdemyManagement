package com.vivek.backend.Management.controller;



import com.vivek.backend.Management.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/cloudinary/upload")
public class CloudinaryController {

    @Autowired
    private CloudinaryService cloudinaryService;



    @PostMapping("/image")
    public ResponseEntity<Map> uploadImage(@RequestParam("image")MultipartFile file)
    {
        Map data = this.cloudinaryService.uploadImage(file);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/video")
    public ResponseEntity<Map> uploadVideo(@RequestParam("video") MultipartFile file) {
        Map data = this.cloudinaryService.uploadVideo(file);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }


    /*

    Best Practices
✅ Store public_id, resource_type, and folder in your DB during upload

✅ Use backend-only access (never expose API secret to frontend)


     */

    @PostMapping("/file")
    public ResponseEntity<Map<String, Object>> uploadFile(
                                             @RequestParam("file") MultipartFile file,
                                             @RequestParam("folder") String folder)
    {
        try {
            Map<String, Object> result = cloudinaryService.uploadFile(file, folder);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = Map.of(
                    "message", "Upload failed",
                    "error", e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }





    // delete only image for now ,

    @DeleteMapping("/delete/{publicId:.+}")
    public ResponseEntity<Map<String, Object>> deleteFile(@PathVariable("publicId") String publicId) {
        try {
            Map<String, Object> result = cloudinaryService.deleteFile(publicId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = Map.of(
                    "message", "Deletion failed",
                    "error", e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

    }





}
