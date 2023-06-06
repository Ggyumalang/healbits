package com.zerobase.healbits.awss3;

import static com.zerobase.healbits.common.type.ErrorCode.FAILED_UPLOAD_FILE;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.zerobase.healbits.common.exception.HealBitsException;
import com.zerobase.healbits.challenge.type.ChallengeCategory;
import com.zerobase.healbits.common.util.FileUtil;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwsS3Api {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;

    public String uploadImage(
        MultipartFile multipartFile
        , ChallengeCategory challengeCategory
        , String challengeName
        , String email
    ) {
        log.info("uploadImage started " + LocalDateTime.now());
        String filePath = FileUtil.createFilePath(challengeCategory,
            challengeName, email);
        String fileName = FileUtil.createFileName(
            multipartFile.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(
                new PutObjectRequest(bucket, filePath + fileName, inputStream,
                    objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new HealBitsException(FAILED_UPLOAD_FILE);
        }
        log.info("uploadImage finished " + LocalDateTime.now() + " ImagePath : "
            + filePath + fileName);
        return filePath + fileName;
    }

    public String getImageUrl(String ImagePath) {
        return amazonS3Client.getUrl(bucket, ImagePath).toString();
    }


}
