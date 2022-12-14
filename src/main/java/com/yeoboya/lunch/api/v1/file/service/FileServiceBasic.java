package com.yeoboya.lunch.api.v1.file.service;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.yeoboya.lunch.api.v1.file.repository.FileRepository;
import com.yeoboya.lunch.api.v1.file.response.FileUploadResponse;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

@Service
@Slf4j
public class FileServiceBasic implements FileService {

    private final FileRepository fileRepository;

    @Value("${file.upload.root-path}")
    private String ROOT_PATH;
    @Value("${file.upload.ratio-wh}")
    private int RATIO_WH;


    public FileServiceBasic(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }


    public FileUploadResponse upload(MultipartFile multipartFile, String fileType) {

        long fileSize;
        byte[] bytes = null;
        String originalFileName = null;        //실제 업로드 파일 이름

        //fixme  MultipartFile / File / dataURL / ImageURL
        try {
            fileSize = multipartFile.getBytes().length;
            bytes = multipartFile.getBytes();
            originalFileName = multipartFile.getOriginalFilename();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ContentInfoUtil contentInfoUtil = new ContentInfoUtil();
        ContentInfo contentInfo = contentInfoUtil.findMatch(bytes);

        if (contentInfo.getContentType().toString().equals("EMPTY")) {
            log.error("contentInfo == null");
        }

        String filExtension = contentInfo.getContentType().getSimpleName();
        if (!this.validationExtension(filExtension)) {
            log.error("validationExtension");
        }

        String directory = this.makeNewDirectory(fileType);
        String fileName = this.makeNewFileName(filExtension);
        this.photoUpload(bytes, directory, filExtension, fileName);

        FileUploadResponse build = FileUploadResponse.builder()
                .originalFileName(originalFileName)
                .fileName(fileName)
                .filePath(directory)
                .extension(filExtension)
                .size(fileSize)
                .build();
        return build;
    }


    public void photoUpload(byte[] bytes, String directory, String fileExtension, String fileName) {
        File file = new File(directory + "/" + fileName);

        InputStream inputStream = new ByteArrayInputStream(bytes);
        try {
            BufferedImage originImage = ImageIO.read(inputStream);
            if (RATIO_WH < originImage.getHeight() || RATIO_WH < originImage.getWidth()) {
                BufferedImage resizedImage = Scalr.resize(originImage, Scalr.Method.QUALITY, 1280, 1280);
                ImageIO.write(resizedImage, fileExtension, file);
            } else {
                ImageIO.write(originImage, fileExtension, file);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error("파일 업로드 실패");
            }
        }
    }


    @Override
    public String makeNewDirectory(String subDirectory) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("/yyyy/MM/dd/HH");    //temp 디렉토리 경로 생성 (현재일시 기준 년/월/일/시)
        String newDir = dateFormat.format(new Date());
        newDir = newDir.substring(0, newDir.length() - 1);    // 00~09=0, 10~19=1, 20~24=2

        String pathname = ROOT_PATH + "/" + subDirectory + newDir;
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
}
