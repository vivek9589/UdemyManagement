package com.vivek.backend.Management.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CloudinaryService {


    // image uploadImage
    public Map uploadImage(MultipartFile file);
    public Map uploadVideo(MultipartFile file);


    //generic method to uploadI all type of content (vid, img,and pdf)
    public Map<String, Object> uploadFile(MultipartFile multipartFile, String folderName);


    // destroy content using public id or assest id
    // cloudinary.uploader().destroy(String public_id, Map options);

    public Map<String, Object> deleteFile(String publicId);

    // Get all content from the particular folder

}
