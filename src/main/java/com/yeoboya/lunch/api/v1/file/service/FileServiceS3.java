package com.yeoboya.lunch.api.v1.file.service;

import com.yeoboya.lunch.api.v1.file.response.FileUploadResponse;
import com.yeoboya.lunch.config.aws.AwsSecretsManagerClient;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FileServiceS3 implements FileService{

    private final S3Client s3Client;
    final String S3_BUCKET_NAME = "yeoboya-lunch-s3";

    public FileServiceS3() {
        JSONObject secret = AwsSecretsManagerClient.getSecret("prod/lunch");
        String accessKey = secret.getString("accessKey");
        String secretKey = secret.getString("secretKey");

        this.s3Client = S3Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();

    }

    @Override
    public FileUploadResponse upload(MultipartFile multipartFile, String subDirectory) {
        // Validation
        String originalFileName = multipartFile.getOriginalFilename();
        String extension = Objects.requireNonNull(originalFileName).substring(originalFileName.lastIndexOf('.') + 1);
        if (!validationExtension(extension)) {
            try {
                throw new IOException("Invalid file extension");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Directory and BoardFile Name Generation
        String directory = makeNewDirectory(subDirectory);
        String fileName = makeNewFileName(extension);

        try {
            Path tempFile = Files.createTempFile(fileName, "");
            Files.write(tempFile, multipartFile.getBytes(), StandardOpenOption.WRITE);

            String objectKey = directory + "/" + fileName;

            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(S3_BUCKET_NAME)
                            .key(objectKey)
                            .build(),
                    RequestBody.fromFile(tempFile));
            Files.delete(tempFile); // 임시 파일 삭제

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(S3_BUCKET_NAME)
                    .key(objectKey)
                    .build();

            // Get metadata
            GetObjectResponse metadata = s3Client.getObject(getObjectRequest).response();
            Long size = metadata.contentLength();
            String contentType = metadata.contentType();
            String externalForm = s3Client.utilities().getUrl(builder -> builder.bucket(S3_BUCKET_NAME).key(originalFileName)).toExternalForm();

            return  FileUploadResponse.builder()
                    .originalFileName(originalFileName)
                    .fileName(fileName)
                    .filePath(directory)
                    .extension(contentType)
                    .externalForm(externalForm)
                    .size(size)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String makeNewDirectory(String subDirectory) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("/yyyy/MM/dd/HH");    //temp 디렉토리 경로 생성 (현재일시 기준 년/월/일/시)
        String newDir = dateFormat.format(new Date());
        newDir = newDir.substring(0, newDir.length() - 1);    // 00~09=0, 10~19=1, 20~24=2

        String pathname = subDirectory + newDir;
        File directory = new File(pathname);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return pathname;
    }

    @Override
    public String makeNewFileName(String fileExtension) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String dataFormat = sdf.format(new Date());
        Random random = new Random();
        return dataFormat + String.format("%03d.", random.nextInt(999) + 1) + fileExtension;
    }

    @Override
    public boolean validationExtension(String filenameExtension) {
        String[] filenameExtensionArray = {"png", "jpg", "jpeg", "gif"};
        ArrayList<String> AllowedExtension = new ArrayList<>(Arrays.asList(filenameExtensionArray));
        return AllowedExtension.contains(filenameExtension);
    }

    //todo file read
    public ResponseInputStream<GetObjectResponse> read() {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(S3_BUCKET_NAME)
                .key("profile/2024/03/27/1/20240327162325842891.jpg")
                .build();
        ResponseInputStream<GetObjectResponse> object = s3Client.getObject(getObjectRequest);
        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(getObjectRequest);
        return object;

    }
}
