package com.project.gream.common.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class S3Config {

    private AmazonS3 s3Client;
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;
    @Value("${cloud.aws.region.static}")
    private String region;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    @PostConstruct
    public void s3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        s3Client =  AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }


    public <T> List<String> imgUpload(Class<T> dto, List<MultipartFile> multipartFiles) throws Exception {

        log.info("------------------------------- S3 이미지 업로드");

        if (multipartFiles.get(0).getOriginalFilename().equals("")) {
            return Collections.emptyList();
        }

        List<String> imgUrlList = new ArrayList<>();

        String imgDir = this.getImgDir(dto);

        for (MultipartFile file : multipartFiles) {
            String fileExtension = this.getFileExtension(Objects.requireNonNull(file.getOriginalFilename(), "이미지 파일이 존재하지 않습니다"));
            String fileName = this.createFileName(dto) + fileExtension;
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try (InputStream inputStream = file.getInputStream()) {
                s3Client.putObject(new PutObjectRequest(bucket + imgDir, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
                imgUrlList.add(s3Client.getUrl(bucket + imgDir, fileName).toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return imgUrlList;
    }
    // 이미지 파일명 중복 방지
    private <T> String createFileName(Class<T> dto) {

        log.info("---------------------------- 이미지 파일명 생성");

        String prefix = null;

        if (dto.getSimpleName().equals("ReviewDto")) {
            prefix = "review";
        } else if (dto.getSimpleName().equals("ItemRequestDto")) {
            prefix = "item";
        } else if (dto.getSimpleName().equals("PostRequestDto")) {
            prefix = "post";
        } else if (dto.getSimpleName().equals("QnaRequestDto")) {
            prefix = "qna";
        }
        return prefix + "_" + System.nanoTime();
    }

    private String getFileExtension(String fileName) throws Exception {

        log.info("---------------------------- 이미지 확장자 추출");

        if(fileName.length() == 0) {
            throw new Exception();
        }

        List<String> fileValidate = new ArrayList<>();

        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".JPG");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG");
        String idxFileName = fileName.substring(fileName.lastIndexOf("."));
        if (!fileValidate.contains(idxFileName)) {
            throw new Exception();
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private <T> String getImgDir(Class<T> dto) {

        log.info("---------------------------- 이미지 경로 생성");
        String imgDir = null;

        if (dto.getSimpleName().equals("ReviewDto")) {
            imgDir = "/review/images";
        } else if (dto.getSimpleName().equals("ItemRequestDto")) {
            imgDir = "/item/images";
        } else if (dto.getSimpleName().equals("PostRequestDto")) {
            imgDir = "/post/images";
        } else if (dto.getSimpleName().equals("QnaRequestDto")) {
            imgDir = "/post/qna/images";
        }
        return imgDir;
    }

}
