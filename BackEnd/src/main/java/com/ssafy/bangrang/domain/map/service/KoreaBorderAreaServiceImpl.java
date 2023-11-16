package com.ssafy.bangrang.domain.map.service;

import com.ssafy.bangrang.domain.map.repository.KoreaBorderAreaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class KoreaBorderAreaServiceImpl implements KoreaBorderAreaService{

    private final KoreaBorderAreaRepository koreaBorderAreaRepository;


}
