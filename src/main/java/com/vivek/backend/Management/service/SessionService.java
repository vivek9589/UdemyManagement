package com.vivek.backend.Management.service;

import com.vivek.backend.Management.entity.User;

public interface SessionService {

    public void generateNewSession(User user, String refreshToken);
    public void validSession(String refreshToken);
}
