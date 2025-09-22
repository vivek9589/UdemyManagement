package com.vivek.backend.Management.enums;


import lombok.Getter;

import java.util.Set;


@Getter
public enum Role
{

    ADMIN(Set.of(Permissions.UDEMY_READ,Permissions.UDEMY_WRITE,Permissions.UDEMY_DELETE)),
    USER(Set.of(Permissions.UDEMY_READ)),
    FACULTY(Set.of(Permissions.UDEMY_READ,Permissions.UDEMY_WRITE));

    //role :- Faculty (give read and write permission to courses)


    private final Set<Permissions> permissions;



    //constructor
    Role(Set<Permissions> permissions)
    {
        this.permissions = permissions;
    }


}
