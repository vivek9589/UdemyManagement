package com.vivek.backend.Management.controller;




import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vivek.backend.Management.dto.CourseRequestDto;
import com.vivek.backend.Management.dto.CourseResponseDto;
import com.vivek.backend.Management.service.CourseService;
import com.vivek.backend.Management.vo.CourseVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;

    CourseController(CourseService courseService)
    {
        this.courseService = courseService;
    }



        // create course with upload its content

    @PreAuthorize("hasAuthority('COURSE_CREATE')")
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public CourseResponseDto createCourse(
            @RequestPart("course") String course,
            @RequestPart("file") MultipartFile file,
            @RequestPart("folder") String folder
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        // Convert JSON string to your desired Java object
        CourseRequestDto dataObject = objectMapper.readValue(course, CourseRequestDto.class);

        return  courseService.createCourse(dataObject, file, folder);

    }



    // get All course

    @PreAuthorize("hasAuthority('COURSE_READ')")
    @GetMapping("/all")
    public List<CourseResponseDto> getAllCourse()
    {
        return courseService.getAllCourse();
    }


    // get course by Id

    @PreAuthorize("hasAuthority('COURSE_READ')")
    @GetMapping("/content/{id}")
    public CourseVO getCourseById(@PathVariable Long id)
    {
        return courseService.getCourseById(id);
    }



    // delete course by id

    @PreAuthorize("hasAuthority('COURSE_DELETE')")
    @DeleteMapping("/delete/{id}")
    public String deleteCourseById(@PathVariable Long id)
    {
        return courseService.deleteCourseById(id);
    }


    // update course by id
    // work on it later



    // uploadImage Course Content
/*
    @PostMapping(value = "/course/{id}/content", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadCourseContent  throws IOException(
            @PathVariable Long id,
            @RequestParam("video") MultipartFile video,
            @RequestParam("pdf") MultipartFile pdf

    ) {
        courseService.saveCourseContent(id, video, pdf);
        return ResponseEntity.ok("Course content uploaded successfully");
    }



 */



}



