package com.ssafy.bangrang.domain.event.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.bangrang.domain.event.api.request.CreateEventRequestDto;
import com.ssafy.bangrang.domain.event.api.request.EventUpdateDto;
import com.ssafy.bangrang.domain.event.api.request.UpdateEventRequestDto;
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
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
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
    private final GeometryFactory geometryFactory;

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

        List<Event> eventList = eventRepository.findAllByWebMember(user);

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

    /**
     * 이벤트 상세정보 조회
     * */
    @Override
    public GetEventDetailResponseDto getEventDetail(Long eventIdx, UserDetails userDetails) {

        if (webMemberRepository.findById(userDetails.getUsername()).isEmpty())
            throw new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1);

        Event foundEvent = eventRepository.findByIdx(eventIdx)
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 이벤트는 존재하지 않습니다.", 1));

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
                .likeCount((long) foundEvent.getLikes().size())
                .build();
    }

    /**
     * 이벤트 생성
     * */
    @Override
    public void createEvent(CreateEventRequestDto createEventRequestDto, MultipartFile image, MultipartFile subImage, UserDetails userDetails) throws Exception{
        WebMember user = webMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        JsonNode addresses = getLatiLong(createEventRequestDto.getAddress());

        if (!addresses.isArray() || addresses.size() <= 0)
            throw new Exception("위도, 경도 오류");

        JsonNode firstAddress = addresses.get(0);
        double latitude = Double.parseDouble(firstAddress.get("x").asText());
        double longitude = Double.parseDouble(firstAddress.get("y").asText());

        String img;
        String subImg;

        if (!image.isEmpty()) {
            String fileName =  s3Service.generateEventImageName(image, createEventRequestDto.getTitle());
            byte[] fileBytes = image.getBytes();
            img = s3Service.uploadToS3(fileName,fileBytes, image.getContentType());
        } else img = null;

        if (!subImage.isEmpty()) {
            String fileName =  s3Service.generateEventSubImageName(subImage, createEventRequestDto.getTitle());
            byte[] fileBytes = subImage.getBytes();
            subImg = s3Service.uploadToS3(fileName,fileBytes, subImage.getContentType());
        } else subImg = null;

        Event event = Event.builder()
                .title(createEventRequestDto.getTitle())
                .subTitle(createEventRequestDto.getSubTitle())
                .address(createEventRequestDto.getAddress())
                .content(createEventRequestDto.getContent())
                .eventUrl(createEventRequestDto.getEventUrl())
                .longitude(longitude)
                .latitude(latitude)
                .startDate(createEventRequestDto.getStartDate())
                .endDate(createEventRequestDto.getEndDate())
                .image(img)
                .subImage(subImg)
                .webMember(user)
                .build();

        eventRepository.save(event);
    }

    // 이벤트 수정하기
    @Override
    public void updateEvent(Long eventIdx, UpdateEventRequestDto updateEventRequestDto, MultipartFile image, MultipartFile subImage, UserDetails userDetails)
            throws Exception {
        WebMember user = webMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        Event event = eventRepository.findByIdx(eventIdx)
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 이벤트는 존재하지 않습니다.", 1));

        if (updateEventRequestDto.getAddress() != event.getAddress()) {
            JsonNode addresses = getLatiLong(updateEventRequestDto.getAddress());

            if (!addresses.isArray() || addresses.size() <= 0)
                throw new Exception("위도, 경도 오류");

            JsonNode firstAddress = addresses.get(0);
            double latitude = Double.parseDouble(firstAddress.get("x").asText());
            double longitude = Double.parseDouble(firstAddress.get("y").asText());

            event.updateEvent(updateEventRequestDto, latitude, longitude);
        } else {
            event.updateEvent(updateEventRequestDto, event.getLatitude(), event.getLongitude());
        }

        if (!image.isEmpty()) {
            if (!event.getImage().isEmpty()) {
                String[] parts = event.getImage().split("amazonaws.com/");
                s3Service.removeFile(parts[1]);
            }
            String fileName =  s3Service.generateEventImageName(image, updateEventRequestDto.getTitle());
            byte[] fileBytes = image.getBytes();
            event.updateEventImg(s3Service.uploadToS3(fileName,fileBytes, image.getContentType()));
        }

        if (!subImage.isEmpty()) {
            if (!event.getSubImage().isEmpty()) {
                String[] parts = event.getSubImage().split("amazonaws.com/");
                s3Service.removeFile(parts[1]);
            }
            String fileName =  s3Service.generateEventImageName(subImage, updateEventRequestDto.getTitle());
            byte[] fileBytes = subImage.getBytes();
            event.updateEventSubImg(s3Service.uploadToS3(fileName,fileBytes, subImage.getContentType()));
        }
        eventRepository.save(event);
    }

    @Override
    public String convertDateToString(LocalDateTime nowDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return nowDate.format(formatter);
    }

    //이벤트 삭제하기
    @Override
    public void deleteEvent(Long eventIdx, UserDetails userDetails) throws Exception {
        WebMember user = webMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        Event event = eventRepository.findByIdx(eventIdx)
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 이벤트는 존재하지 않습니다.", 1));

        if (event.getWebMember().equals(user))
            eventRepository.delete(event);
        else
            throw new Exception("이벤트 작성자가 당신이 아닙니다");
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
//                        .startDate(e.getStartDate())
//                        .endDate(e.getEndDate())
                        .address(e.getAddress())
                        .latitude(e.getLatitude())
                        .longitude(e.getLongitude())
                        .likeCount((long) e.getLikes().size())
                        .build())
                .collect(Collectors.toList());

        return eventList;
    }

    // 위도 경도 찾기
    public JsonNode getLatiLong (String address) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-NCP-APIGW-API-KEY-ID", client_id);
        headers.add("X-NCP-APIGW-API-KEY", client_secret);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        String query = address;
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

        return addresses;
    }
}
