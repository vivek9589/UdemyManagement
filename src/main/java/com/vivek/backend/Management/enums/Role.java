package com.vivek.backend.Management.enums;


import lombok.Getter;

import java.util.Set;


public enum Role{

    ADMIN(Set.of(
            Permission.AUTH_MANAGE_USERS,
            Permission.AUTH_VIEW_ROLES,

            Permission.USER_CREATE,
            Permission.USER_READ,
            Permission.USER_UPDATE,
            Permission.USER_DELETE,

            Permission.COURSE_CREATE,
            Permission.COURSE_READ,
            Permission.COURSE_UPDATE,
            Permission.COURSE_DELETE,

            Permission.MEDIA_UPLOAD,
            Permission.MEDIA_DELETE,

            Permission.ENROLL_USER,
            Permission.VIEW_ENROLLMENTS,
            Permission.CANCEL_ENROLLMENT,

            Permission.FACULTY_ASSIGN,
            Permission.FACULTY_VIEW,

            Permission.PLAN_CREATE,
            Permission.PLAN_READ,
            Permission.PLAN_UPDATE,
            Permission.PLAN_DELETE,

            Permission.QUESTION_CREATE,
            Permission.QUESTION_READ,
            Permission.QUESTION_UPDATE,
            Permission.QUESTION_DELETE,

            Permission.QUIZ_CREATE,
            Permission.QUIZ_READ,
            Permission.QUIZ_UPDATE,
            Permission.QUIZ_DELETE,
            Permission.QUIZ_ATTEMPT
    )),

    FACULTY(Set.of(
            Permission.COURSE_CREATE,
            Permission.COURSE_READ,
            Permission.COURSE_UPDATE,

            Permission.MEDIA_UPLOAD,
            Permission.MEDIA_DELETE,

            Permission.VIEW_ENROLLMENTS,
            Permission.FACULTY_VIEW,

            Permission.PLAN_READ,

            Permission.QUESTION_CREATE,
            Permission.QUESTION_READ,
            Permission.QUESTION_UPDATE,
            Permission.QUIZ_CREATE,
            Permission.QUIZ_READ,
            Permission.QUIZ_UPDATE
    )),

    USER(Set.of(
            Permission.USER_CREATE,
            Permission.USER_READ,

            Permission.COURSE_READ,

            Permission.ENROLL_USER,

            Permission.VIEW_ENROLLMENTS,
            Permission.CANCEL_ENROLLMENT,

            Permission.PLAN_READ,

            Permission.QUESTION_READ,

            Permission.QUIZ_READ,
            Permission.QUIZ_ATTEMPT
    ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}