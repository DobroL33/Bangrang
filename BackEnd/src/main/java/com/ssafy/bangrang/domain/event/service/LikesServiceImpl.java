package com.ssafy.bangrang.domain.event.service;


import com.ssafy.bangrang.domain.event.entity.Event;
import com.ssafy.bangrang.domain.event.entity.Likes;
import com.ssafy.bangrang.domain.event.repository.EventRepository;
import com.ssafy.bangrang.domain.event.repository.LikesRepository;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.repository.AppMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LikesServiceImpl implements LikesService {

    private final LikesRepository likesRepository;
    private final AppMemberRepository appMemberRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public void saveLikes(UserDetails userDetails, Long eventIdx) {
        AppMember appMember = appMemberRepository.findById(userDetails.getUsername()).orElseThrow();
        Event event = eventRepository.findById(eventIdx).orElseThrow();

        Likes likes = Likes.builder()
                .appMember(appMember)
                .event(event)
                .build();

        likesRepository.save(likes);

    }
}
