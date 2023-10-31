package com.ssafy.bangrang.domain.member.service;

import com.ssafy.bangrang.domain.member.repository.StampRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StampServiceImpl implements StampService{

    private final StampRepository stampRepository;



}
