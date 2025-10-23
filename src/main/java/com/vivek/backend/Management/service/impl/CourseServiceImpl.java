package com.vivek.backend.Management.service.impl;



import com.vivek.backend.Management.config.CloudinaryConfig;
import com.vivek.backend.Management.dto.CourseRequestDto;
import com.vivek.backend.Management.dto.CourseResponseDto;
import com.vivek.backend.Management.entity.Category;
import com.vivek.backend.Management.entity.Course;
import com.vivek.backend.Management.entity.Faculty;
import com.vivek.backend.Management.exception.CategoryNotFoundException;
import com.vivek.backend.Management.exception.CourseNotFoundException;
import com.vivek.backend.Management.exception.FacultyNotFoundException;
import com.vivek.backend.Management.repository.CategoryRepository;
import com.vivek.backend.Management.repository.CourseRepository;
import com.vivek.backend.Management.repository.FacultyRepository;
import com.vivek.backend.Management.repository.UserRepository;
import com.vivek.backend.Management.service.CloudinaryService;
import com.vivek.backend.Management.service.CourseService;
import com.vivek.backend.Management.vo.CourseVO;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
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
    public CourseServiceImpl(CourseRepository courseRepository,
                             UserRepository userRepository,
                             FacultyRepository facultyRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.facultyRepository = facultyRepository;
    }

    @Autowired CloudinaryService cloudinaryService;

    @Autowired private CategoryRepository categoryRepository;

    // Cloudinary cloudinary = new Cloudinary();

    @Autowired CloudinaryConfig cloudinary;

    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

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
        logger.info(" starting Course creation / uploading files");

        Map<String, Object> result = cloudinaryService.uploadFile(file, folder);

        // Fetch category from DB
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() ->
                {
                    logger.warn("Course category not found with ID: {}", dto.getCategoryId());
                    throw new CategoryNotFoundException("Course category not found with ID: " + dto.getCategoryId());
                });


        // Fetch faculty from DB
        Faculty faculty = facultyRepository.findById(dto.getFacultyId())
                .orElseThrow(() ->
                {
                    logger.warn("Faculty not found with ID: {}", dto.getFacultyId());
                    throw new FacultyNotFoundException("Faculty not found with ID: " + dto.getFacultyId());

                });

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

        logger.info("Course Created with ID: {}",savedCourse.getCourseId());
        return mapToResponseDto(savedCourse);
    }

    @Override
    public List<CourseResponseDto> getAllCourse() {
        logger.info("Fetching all course ");
        List<Course> courses = courseRepository.findAll();

        if(courses.isEmpty()) logger.warn("No courses found in the database");



        logger.info("Total courses fetched :{}",courses.size());
        return courses.stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    // work on get course content using cloudinary

    @Override
    public CourseVO getCourseById(Long id) {

        logger.info("Fetching course by ID : {}",id);
        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                {
                    logger.debug("Course not exit with this ID: {}",id);
                    throw new CourseNotFoundException("Course not found with ID: " + id);

                });


        //String url = cloudinary.getCloudinary().url().generate(course.getPublicId() + ".jpg");
        //System.out.println("this is the url "+ url);


        logger.info("Successfully Fetched Course with this ID: {}",id);

        // return mapToResponseDto(course);
        return CourseVO.builder()
                .courseId(course.getCourseId())
                .courseTitle(course.getCourseName())
                .instructorName(course.getFaculty().getFacultyName())
                .category(course.getCategory() != null ? course.getCategory().getCategoryName() : null)
                .duration(course.getDuration())
                .build();
    }


    @Override
    public String deleteCourseById(Long id) {

        logger.info("Deleting course by ID:{}",id);



        if(courseRepository.existsById(id))
        {
            // get the full course
            Course course = courseRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Course not found for deletion with ID: {}", id);
                        throw new CourseNotFoundException("Course not found with ID: " + id);
                    });


            cloudinaryService.deleteFile(course.getPublicId());
            courseRepository.deleteById(id);

            logger.info("Course deleted  with  ID: {}",id);
            return "Successfully deleted course with this id "+ course.getPublicId();
        }


        logger.debug("Course not exist with this ID:",id);
        throw new CourseNotFoundException("Invalid Id for course ,not deleted");


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
