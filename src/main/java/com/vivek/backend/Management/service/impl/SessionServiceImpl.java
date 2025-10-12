package com.vivek.backend.Management.service.impl;

import com.vivek.backend.Management.entity.Session;
import com.vivek.backend.Management.entity.User;
import com.vivek.backend.Management.repository.SessionRepository;
import com.vivek.backend.Management.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final int SESSION_LIMIT = 5;

    public void generateNewSession(User user, String refreshToken){

        List<Session> userSession = sessionRepository.findByUser(user);

        if(userSession.size() == SESSION_LIMIT){
            userSession.sort(Comparator.comparing(Session::getLastUsedAt));

            Session leastRecentlyUsedSession = userSession.getFirst();
            sessionRepository.delete(leastRecentlyUsedSession);  //delete first session
        }

        Session newSession = Session   //create new session
                .builder()
                .user(user)
                .refreshToken(refreshToken)




                .build();
        sessionRepository.save(newSession);
    }

    public void validSession(String refreshToken){
        Session session = sessionRepository.findByRefreshToken(refreshToken).orElseThrow(()-> new SessionAuthenticationException("Sessoin not found for refresh token: "+ refreshToken));

        session.setLastUsedAt(LocalDateTime.now());

        sessionRepository.save(session);
    }
}
