package com.pet.petmily.s3.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.pet.petmily.s3.component.S3Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Slf4j
@RequiredArgsConstructor
@Component
public class AWSS3UploadService implements UploadService {

    private final AmazonS3 amazonS3;
    private final S3Component component;

    @Override
    public void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String fileName) {
        log.info("uploadFile() 호출");
        log.info("fileName = " + fileName);
        amazonS3.putObject(new PutObjectRequest(component.getBucket(), fileName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
    }

    @Override
    public String getFileUrl(String fileName) {
        log.info("getFileUrl() 호출");
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
                component.getBucket(),
                fileName
        ).withMethod(HttpMethod.GET);

        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }
    @Override
    public byte[] downloadFile(String fileName) {
        log.info("Called downloadFile()");
        S3Object s3Object = amazonS3.getObject(component.getBucket(), fileName);
        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();

        try {
            return IOUtils.toByteArray(objectInputStream);
        } catch (IOException e) {
            throw new RuntimeException("파일 다운로드 실패: " + fileName, e);
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }
    }
}