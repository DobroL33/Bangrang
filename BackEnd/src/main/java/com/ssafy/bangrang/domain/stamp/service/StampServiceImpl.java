package com.ssafy.bangrang.domain.stamp.service;

import com.ssafy.bangrang.domain.event.entity.Event;
import com.ssafy.bangrang.domain.event.repository.EventRepository;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.repository.AppMemberRepository;
import com.ssafy.bangrang.domain.stamp.api.response.GetStampListResponseDto;
import com.ssafy.bangrang.domain.stamp.api.response.StampDetailDto;
import com.ssafy.bangrang.domain.stamp.entity.AppMemberStamp;
import com.ssafy.bangrang.domain.stamp.repository.AppMemberStampRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StampServiceImpl implements StampService{

    private final AppMemberRepository appMemberRepository;

    private final EventRepository eventRepository;

    private final AppMemberStampRepository appMemberStampRepository;
//    @Override
//    public StampResponseDto findStampsById(String id){
//
//        // id로 member를 찾음
//        AppMember appMember = appMemberRepository.findById(id).orElseThrow();
//
//        // member의 stamp를 불러옴
//        List<StampDetailDto> stampDetailDtos = appMember.getAppMemberStamps()
//                .stream()
//                .map(appMemberStamp -> StampDetailDto
//                        .builder()
//                        .stampName(appMemberStamp.getStamp().getName())
//                        .stampEvent(appMemberStamp.getStamp().getEvent().getIdx())
//                        .stampLocation(appMemberStamp.getStamp().getEvent().getAddress())
//                        .stampTime(appMemberStamp.getCreatedAt().format(dateTimeFormatter))
//                        .build())
//                .collect(Collectors.toList());
//
//        // distinct 행사를 불러옴
//        Set<Long> distinctEvent = stampDetailDtos.stream()
//                .map(appMemberStamp -> appMemberStamp.getStampEvent())
//                .collect(Collectors.toSet());
//
//        StampResponseDto stampResponse = StampResponseDto.builder()
//                .totalNum((long) stampDetailDtos.size())
//                .totalType((long) distinctEvent.size())
//                .stamps(stampDetailDtos)
//                .build();
//
//        return stampResponse;
//    }
    /**
     * 스탬프 찍기
     * */
    @Override
    @Transactional
    public void sealStamp(Long eventIdx, UserDetails userDetails) {
        AppMember appMember = appMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        Event event = eventRepository.findByIdx(eventIdx)
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 이벤트는 존재하지 않습니다.", 1));

        AppMemberStamp appMemberStamp = AppMemberStamp.builder()
                .appMember(appMember)
                .stamp(event.getStamp())
                .build();

        // 스탬프 찍기
        appMemberStampRepository.save(appMemberStamp);
    }

    /**
     * 내 스탬프 리스트
     * */
    @Override
    public GetStampListResponseDto getStampList(UserDetails userDetails) {
        AppMember appMember = appMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        List<AppMemberStamp> appMemberStamps = appMemberStampRepository.findAllByAppMember(appMember);

        List<StampDetailDto> stampDetailList = new ArrayList<>();

        for (AppMemberStamp appMemberStamp : appMemberStamps) {
            StampDetailDto stampDetailDto = StampDetailDto.builder()
                    .stampEvent(appMemberStamp.getStamp().getEvent().getIdx())
                    .stampTime(appMemberStamp.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")))
                    .stampLocation(appMemberStamp.getStamp().getEvent().getAddress())
                    .stampName(appMemberStamp.getStamp().getName())
                    .build();
            stampDetailList.add(stampDetailDto);
        }

        GetStampListResponseDto getStampListResponseDto = GetStampListResponseDto.builder()
                .stamps(stampDetailList)
                .totalType((long) 0)
                .totalNum((long) stampDetailList.size())
                .build();

        return getStampListResponseDto;
    }
}
