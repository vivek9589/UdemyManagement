package com.vivek.backend.Management.enums;




public enum Permission {


    // USER
    USER_CREATE,
    USER_READ,
    USER_UPDATE,
    USER_DELETE,



    // Auth
    AUTH_MANAGE_USERS,
    AUTH_VIEW_ROLES,

    // Course
    COURSE_CREATE,
    COURSE_READ,
    COURSE_UPDATE,
    COURSE_DELETE,

    // Cloudinary
    MEDIA_UPLOAD,
    MEDIA_DELETE,

    // Enrollment
    ENROLL_USER,
    VIEW_ENROLLMENTS,
    CANCEL_ENROLLMENT,

    // Faculty
    FACULTY_ASSIGN,
    FACULTY_VIEW,

    // Plan
    PLAN_CREATE,
    PLAN_READ,
    PLAN_UPDATE,
    PLAN_DELETE,

    // Question
    QUESTION_CREATE,
    QUESTION_READ,
    QUESTION_UPDATE,
    QUESTION_DELETE,

    // Quiz
    QUIZ_CREATE,
    QUIZ_READ,
    QUIZ_UPDATE,
    QUIZ_DELETE,
    QUIZ_ATTEMPT
}