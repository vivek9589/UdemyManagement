package com.vivek.backend.Management.service.impl;

import com.vivek.backend.Management.entity.Session;
import com.vivek.backend.Management.entity.User;
import com.vivek.backend.Management.repository.SessionRepository;
import com.vivek.backend.Management.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(SessionServiceImpl.class);

    public void generateNewSession(User user, String refreshToken){

        logger.info("Generating New session for user : {}",user.getFirstName());
        List<Session> userSession = sessionRepository.findByUser(user);

        if(userSession.size() == SESSION_LIMIT){
            logger.info("User Session hit the maximum limit ");
            userSession.sort(Comparator.comparing(Session::getLastUsedAt));

            Session leastRecentlyUsedSession = userSession.getFirst();
            sessionRepository.delete(leastRecentlyUsedSession);  //delete first session
        }

        Session newSession = Session   //create new session
                .builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();
        logger.info("New Session Generated");
        sessionRepository.save(newSession);
    }

    public void validSession(String refreshToken){
        Session session = sessionRepository.findByRefreshToken(refreshToken).orElseThrow(()-> new SessionAuthenticationException("Sessoin not found for refresh token: "+ refreshToken));

        session.setLastUsedAt(LocalDateTime.now());

        sessionRepository.save(session);
    }
}
