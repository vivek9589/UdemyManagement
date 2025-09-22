package com.vivek.backend.Management.service.impl;



import com.vivek.backend.Management.config.CloudinaryConfig;
import com.vivek.backend.Management.dto.CourseRequestDto;
import com.vivek.backend.Management.dto.CourseResponseDto;
import com.vivek.backend.Management.entity.Category;
import com.vivek.backend.Management.entity.Course;
import com.vivek.backend.Management.entity.Faculty;
import com.vivek.backend.Management.repository.CategoryRepository;
import com.vivek.backend.Management.repository.CourseRepository;
import com.vivek.backend.Management.repository.FacultyRepository;
import com.vivek.backend.Management.repository.UserRepository;
import com.vivek.backend.Management.service.CloudinaryService;
import com.vivek.backend.Management.service.CourseService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final FacultyRepository facultyRepository;

    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    private CategoryRepository categoryRepository;




    // Cloudinary cloudinary = new Cloudinary();

    @Autowired
    CloudinaryConfig cloudinary;




    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository,
                             UserRepository userRepository,
                             FacultyRepository facultyRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.facultyRepository = facultyRepository;
    }

/*

    public CourseResponseDto createCourse(CourseRequestDto dto,MultipartFile file, String folder) {


        Map<String, Object> result = cloudinaryService.uploadFile(file,folder);
        System.out.print("this is the course service --> "+result.get("public_id"));

        Course course = Course.builder()
                .courseName(dto.getCourseName())
                .description(dto.getDescription())
                .duration(dto.getDuration())
                .fees(dto.getFees())
                .publicId((String) result.get("public_id"))
                .url((String) result.get("url"))
                .build();

        Course savedCourse = courseRepository.save(course);

        return mapToResponseDto(savedCourse);
    }

 */

    @Override
    public CourseResponseDto createCourse(CourseRequestDto dto, MultipartFile file, String folder) {
        Map<String, Object> result = cloudinaryService.uploadFile(file, folder);

        // Fetch category from DB
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + dto.getCategoryId()));


        // Fetch faculty from DB
        Faculty faculty = facultyRepository.findById(dto.getFacultyId())
                .orElseThrow(() -> new RuntimeException("Faculty not found with ID: " + dto.getFacultyId()));

        Course course = Course.builder()
                .courseName(dto.getCourseName())
                .description(dto.getDescription())
                .duration(dto.getDuration())
                .fees(dto.getFees())
                .publicId((String) result.get("public_id"))
                .url((String) result.get("url"))
                .category(category) // Link category here
                .faculty(faculty)
                .build();

        Course savedCourse = courseRepository.save(course);
        return mapToResponseDto(savedCourse);
    }


    private CourseResponseDto mapToResponseDto(Course course) {
        return CourseResponseDto.builder()
                .courseId(course.getCourseId())
                .courseName(course.getCourseName())
                .description(course.getDescription())
                .duration(course.getDuration())
                .fees(course.getFees())
                .publicId(course.getPublicId())
                .url(course.getUrl())
                //.facultyName(course.getFaculty().getName())
                //.instructorEmail(course.getFaculty().getEmail())
                //.createdBy(course.getUser().getFirstName() + " " + course.getUser().getLastName())
                .build();
    }





    @Override
    public List<Course> getAllCourse() {

        List<Course> courses = courseRepository.findAll();
        return courses;
    }

    // work on get course content using cloudinary

    @Override
    public CourseResponseDto getCourseById(Long id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + id));


        //String url = cloudinary.getCloudinary().url().generate(course.getPublicId() + ".jpg");
        //System.out.println("this is the url "+ url);


        return mapToResponseDto(course);
    }


    @Override
    public String deleteCourseById(Long id) {

        if(courseRepository.existsById(id))
        {

            // get the full course
            CourseResponseDto course = getCourseById(id);

            cloudinaryService.deleteFile(course.getPublicId());
            courseRepository.deleteById(id);
            return "Successfully deleted course with this id "+ course.getPublicId();
        }

        return "Invalid Id for course ,not deleted";

    }



    /*
    @Override
    public void saveCourseContent(Long courseId, MultipartFile video, MultipartFile pdf) throws IOException {
        String basePath = "uploads/course_" + courseId + "/";
        Files.createDirectories(Paths.get(basePath));

        Files.copy(video.getInputStream(), Paths.get(basePath + video.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(pdf.getInputStream(), Paths.get(basePath + pdf.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);

        // Optionally update course entity with file paths
    }


     */


    // save/uploadImage file


}
