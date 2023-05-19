package com.pet.petmily.s3.controller;

import com.pet.petmily.s3.service.FileUploadService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DownloadController {

    private final FileUploadService fileUploadService;

    @ApiOperation(value = "이미지 다운로드", notes = "이미지를 다운로드합니다")
    @GetMapping("image/download")
    public ResponseEntity<byte[]> downloadImage(String fileName) {
        log.info("Call downloadImage()");
        byte[] imageData = fileUploadService.downloadImage(fileName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // Adjust content type if necessary
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentType(MediaType.IMAGE_GIF);;

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(imageData.length)
                .body(imageData);
    }
}