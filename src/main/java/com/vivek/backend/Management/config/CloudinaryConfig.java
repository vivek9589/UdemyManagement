package com.vivek.backend.Management.config;


import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig{



    @Bean
    public Cloudinary getCloudinary()
    {
        Map config = new HashMap();
        config.put("cloud_name","dnpmn8agl");
        config.put("api_key","892143927645525");
        config.put("api_secret","k8MCi43-VwnGai_o-B_dlQzpTwM");
        config.put("secure",true);

        return new Cloudinary(config);
    }






}
