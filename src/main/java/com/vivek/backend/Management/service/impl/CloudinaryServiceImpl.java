package com.vivek.backend.Management.service.impl;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.vivek.backend.Management.service.CloudinaryService;
import com.vivek.backend.Management.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;  /*What Is the Cloudinary Class?
                                    The Cloudinary class is part of the Cloudinary Java SDK. It wraps the API calls and configuration needed to communicate
                                    with Cloudinary’s cloud services. You typically instantiate it with your credentials (cloud name, API key, and API secret)
                                    either directly or via an environment variable.
                                    */





    @Override
    public Map uploadImage(MultipartFile file)
    {
        try
        {
            Map data = this.cloudinary.uploader().upload(file.getBytes(),Map.of());
            System.out.println("public id-- >   " + data.get("public_id"));

            return data;
        } catch (IOException e) {
            throw new RuntimeException("Image/file uploading fail ");

        }
    }


    public Map uploadVideo(MultipartFile file) {
        try {
            return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "resource_type", "video",
                    "public_id", "course_video_" + UUID.randomUUID(),
                    "folder", "course_videos"
            ));
        } catch (IOException e) {
            throw new RuntimeException("Video uploadImage failed", e);
        }
    }



    // generic method to upload all type of content (vid, img,and pdf)

    public Map<String, Object> uploadFile(MultipartFile multipartFile, String folderName) {
        try {
            Map<String, Object> options = ObjectUtils.asMap(
                    "resource_type", "auto",      // auto-detect file type
                    "folder", folderName          // dynamic folder name
            );


            Map<String , Object> result = cloudinary.uploader().upload(multipartFile.getBytes(), options);

            // courseService.createCourse().setPublicId(result.get("public_id"));
            System.out.println(result.get("public_id"));

            return  result;
        } catch (IOException e) {
            throw new RuntimeException("Cloudinary upload failed: " + e.getMessage());
        }
    }




    public Map<String,Object> deleteFile(String publicId) {
        try {
            Map<String, Object> options = ObjectUtils.emptyMap();

            return cloudinary.uploader().destroy(publicId, options);
        } catch (IOException e) {
            throw new RuntimeException("Cloudinary deletion failed: " + e.getMessage());
        }
    }


}




