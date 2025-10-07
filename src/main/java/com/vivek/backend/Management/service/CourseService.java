package com.vivek.backend.Management.service;


import com.vivek.backend.Management.dto.CourseRequestDto;
import com.vivek.backend.Management.dto.CourseResponseDto;
import com.vivek.backend.Management.vo.CourseVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CourseService {


    // CRUD OPERATION FOR Course

    // create course
    public CourseResponseDto createCourse(CourseRequestDto course,MultipartFile file, String folder);

    // Get courses

        // get All course
        public List<CourseResponseDto> getAllCourse();


         // get course by Id
        public CourseVO getCourseById(Long id);

    // delete course by id
    public String deleteCourseById(Long id);

    // update course by id


    // uploadImage content (video , pdf etc)

    // public void saveCourseContent(Long courseId, MultipartFile video, MultipartFile pdf)  throws IOException;;







}
