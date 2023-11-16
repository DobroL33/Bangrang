package com.ssafy.bangrang.global.s3service;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    String uploadToS3(String fileName, byte[] fileBytes, String contentType);
    String generateAuthFileName(MultipartFile multipartFile, String userNickname);
    String generateEventImageName(MultipartFile multipartFile, String eventTitle);
    String generateEventSubImageName(MultipartFile multipartFile, String eventTitle);
    String generateImgFileName(MultipartFile multipartFile, String userNickname);
    String getFileExtension(String fileName);
    void removeFile(String fileName);
}
