package com.ssafy.bangrang.domain.event.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.bangrang.domain.event.api.request.EventSignUpDto;
import com.ssafy.bangrang.domain.event.api.request.EventUpdateDto;
import com.ssafy.bangrang.domain.event.api.response.GetEventAllResponseDto;
import com.ssafy.bangrang.domain.event.api.response.GetEventDetailResponseDto;
import com.ssafy.bangrang.domain.event.api.response.GetEventListResponseDto;
import com.ssafy.bangrang.domain.event.entity.Event;
import com.ssafy.bangrang.domain.event.repository.EventRepository;
import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.domain.member.repository.WebMemberRepository;
import com.ssafy.bangrang.global.s3service.S3ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class EventWebServiceImpl implements EventWebService{

    private final EventRepository eventRepository;
    private final WebMemberRepository webMemberRepository;
    private final ObjectMapper objectMapper;
    private final AmazonS3Client amazonS3Client;
    private final S3ServiceImpl s3Service;

    @Value("${cloud.naver.client_id}")
    String client_id;

    @Value("${cloud.naver.client_secret}")
    String client_secret;


    /**
     * 내가 등록한 이벤트 리스트 조회
     * */
    @Override
    public List<GetEventListResponseDto> getEventList(UserDetails userDetails) throws Exception {
        WebMember user = webMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        List<Event> eventList = eventRepository.findByWebMember(user.getIdx());

        List<GetEventListResponseDto> result = new ArrayList<>();

        for (Event event : eventList) {
            GetEventListResponseDto getEventListResponseDto = GetEventListResponseDto.builder()
                    .eventIdx(event.getIdx())
                    .title(event.getTitle())
                    .subTitle(event.getSubTitle())
                    .eventImg(event.getImage())
                    .startDate(event.getStartDate())
                    .endDate(event.getEndDate())
                    .address(event.getAddress())
                    .eventUrl(event.getEventUrl())
                    .build();

            result.add(getEventListResponseDto);
        }
        return result;
    }

    @Override
    public String convertDateToString(LocalDateTime nowDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return nowDate.format(formatter);
    }

    @Override
    @Transactional
    public void saveEvent(EventSignUpDto eventSignUpDto, MultipartFile eventUrl, UserDetails userDetails) throws IOException {
        log.info("이벤트 저장 시작");

        WebMember webMember = webMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(()->new IllegalArgumentException("찾을수 없어요!!"));

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-NCP-APIGW-API-KEY-ID", client_id);
        headers.add("X-NCP-APIGW-API-KEY", client_secret);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        String query = eventSignUpDto.getAddress();
        String url = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query="+query;

        log.info("네이버 Geocoding api요청 시작");
        ResponseEntity<String> response =
                restTemplate.exchange(url,
                        HttpMethod.GET,
                        new HttpEntity<>(null, headers),
                        String.class);
        String body = response.getBody();
        log.info("네이버 Geocoding api요청 완료");


        JsonNode root = objectMapper.readTree(body);
        JsonNode addresses = root.get("addresses");




        if (addresses.isArray() && addresses.size() > 0) {

            if (eventUrl!=null) {
                String fileName =  s3Service.generateAuthFileName(eventUrl, userDetails.getUsername());
                byte[] fileBytes = eventUrl.getBytes();

                String eventPath = s3Service.uploadToS3(fileName,fileBytes, eventUrl.getContentType());

                JsonNode firstAddress = addresses.get(0);
                double latitude = Double.parseDouble(firstAddress.get("x").asText());
                double longitude = Double.parseDouble(firstAddress.get("y").asText());

                Event event = Event.builder()
                        .title(eventSignUpDto.getTitle())
                        .subTitle(eventSignUpDto.getSubTitle())
                        .address(eventSignUpDto.getAddress())
                        .content(eventSignUpDto.getContent())
                        .eventUrl(eventPath)
                        .longitude(longitude)
                        .latitude(latitude)
                        .startDate(LocalDateTime.parse(eventSignUpDto.getStartDate())) // 수정된 부분
                        .endDate(LocalDateTime.parse(eventSignUpDto.getEndDate())) // 수정된 부분
                        .webMember(webMember)
                        .build();

                event = eventRepository.save(event);
            } else  {
                JsonNode firstAddress = addresses.get(0);
                double latitude = Double.parseDouble(firstAddress.get("x").asText());
                double longitude = Double.parseDouble(firstAddress.get("y").asText());

                Event event = Event.builder()
                        .title(eventSignUpDto.getTitle())
                        .subTitle(eventSignUpDto.getSubTitle())
                        .address(eventSignUpDto.getAddress())
                        .content(eventSignUpDto.getContent())
                        .longitude(longitude)
                        .latitude(latitude)
                        .eventUrl(null)
                        .startDate(LocalDateTime.parse(eventSignUpDto.getStartDate())) // 수정된 부분
                        .endDate(LocalDateTime.parse(eventSignUpDto.getEndDate())) // 수정된 부분
                        .webMember(webMember)
                        .build();

                event = eventRepository.save(event);
            }
        }
    }

    // 이벤트 수정하기
    @Override
    @Transactional
    public void updateEvent(Long eventIdx, MultipartFile eventUrl, EventUpdateDto eventUpdateDto, UserDetails userDetails) throws IOException {

        log.info("수정하기 시작");
        Event event = eventRepository.findByIdx(eventIdx)
                .orElseThrow(()->new IllegalArgumentException("이벤트를 찾을 수 없다"));

        WebMember webMember = webMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을수 없다"));

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-NCP-APIGW-API-KEY-ID", client_id);
        headers.add("X-NCP-APIGW-API-KEY", client_secret);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        String query = eventUpdateDto.getAddress();
        String url = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query="+query;

        log.info("네이버 Geocoding api요청 시작");
        ResponseEntity<String> response =
                restTemplate.exchange(url,
                        HttpMethod.GET,
                        new HttpEntity<>(null, headers),
                        String.class);
        String body = response.getBody();
        log.info("네이버 Geocoding api요청 완료");


        JsonNode root = objectMapper.readTree(body);
        JsonNode addresses = root.get("addresses");




        if (addresses.isArray() && addresses.size() > 0) {

            if (eventUrl != null) {
                String fileName = s3Service.generateAuthFileName(eventUrl, userDetails.getUsername());
                byte[] fileBytes = eventUrl.getBytes();

                String eventPath = s3Service.uploadToS3(fileName, fileBytes, eventUrl.getContentType());

                JsonNode firstAddress = addresses.get(0);
                double latitude = Double.parseDouble(firstAddress.get("x").asText());
                double longitude = Double.parseDouble(firstAddress.get("y").asText());


                event.update(eventUpdateDto,eventPath);
//                Event updateEvent = Event.builder()
//                        .title(eventUpdateDto.getTitle())
//                        .subTitle(eventUpdateDto.getSubTitle())
//                        .address(eventUpdateDto.getAddress())
//                        .content(eventUpdateDto.getContent())
//                        .eventUrl(eventPath)
//                        .longitude(longitude)
//                        .latitude(latitude)
//                        .startDate(LocalDateTime.parse(eventUpdateDto.getStartDate())) // 수정된 부분
//                        .endDate(LocalDateTime.parse(eventUpdateDto.getEndDate())) // 수정된 부분
//                        .webMember(webMember)
//                        .build();
//
//                event = eventRepository.save(updateEvent);
            } else {
                JsonNode firstAddress = addresses.get(0);
                double latitude = Double.parseDouble(firstAddress.get("x").asText());
                double longitude = Double.parseDouble(firstAddress.get("y").asText());
                String eventPath = null;
                event.update(eventUpdateDto, eventPath);
            }
        }
    }

    //이벤트 삭제하기
    @Override
    public void deleteEvent(Long eventIdx, UserDetails userDetails){
        Optional<Event> event = eventRepository.findByIdx(eventIdx);
        if (event.isPresent()) {
            eventRepository.delete(event.get());
        } else {
            throw new IllegalArgumentException("Comment not found with ID: " + eventIdx);
        }
    }

    //웹멤버의 모든 이벤트 가져오기
    @Override
    public List<Event> getWebMemberAllEvents(Long webMemberIdx, UserDetails userDetails) {
        log.info("웹멤버 모든 이벤트 가져오기");
        WebMember webMember = webMemberRepository.findByIdx(webMemberIdx)
                .orElseThrow(()->new IllegalArgumentException("찾을수없다"));

        List<Event> eventList = eventRepository.findAllByWebMember(webMember);


        return eventList;
    }


    // 모든 축제 조회하기
    @Override
    public List<GetEventAllResponseDto> findAll(){
        List<GetEventAllResponseDto> eventList = eventRepository.findAll()
                .stream()
                .map(e -> GetEventAllResponseDto.builder()
                        .eventIdx(e.getIdx())
                        .image(e.getImage())
                        .title(e.getTitle())
                        .subtitle(e.getSubTitle())
                        .startDate(e.getStartDate())
                        .endDate(e.getEndDate())
                        .address(e.getAddress())
                        .latitude(e.getLatitude())
                        .longitude(e.getLongitude())
                        .likeCount((long) e.getLikes().size())
                        .build())
                .collect(Collectors.toList());

        return eventList;
    }

    /**
     * 이벤트 상세정보 조회
     * */
    @Override
    public GetEventDetailResponseDto getEventDetail(Long eventIdx, UserDetails userDetails) {

        if (webMemberRepository.findById(userDetails.getUsername()).isEmpty())
            throw new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1);

        Event foundEvent = eventRepository.findByIdx(eventIdx)
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 이벤트는 존재하지 않습니다.", 1));

        int likeCount = foundEvent.getLikes().size();

        return GetEventDetailResponseDto.builder()
                .title(foundEvent.getTitle())
                .subtitle(foundEvent.getSubTitle())
                .content(foundEvent.getContent())
                .image(foundEvent.getImage())
                .subImage(foundEvent.getSubImage())
                .startDate(foundEvent.getStartDate())
                .endDate(foundEvent.getEndDate())
                .address(foundEvent.getAddress())
                .longitude(foundEvent.getLongitude())
                .latitude(foundEvent.getLatitude())
                .eventUrl(foundEvent.getEventUrl())
                .likeCount(likeCount)
                .build();
    }
}
