package com.codueon.boostUp.global.file;

import com.amazonaws.services.s3.model.ObjectMetadata;
import net.bytebuddy.asm.Advice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.google.common.io.Files.getFileExtension;

@Component
public class FileHandler {
    public List<UploadFile> parseUploadFileInfo(List<MultipartFile> multipartFiles) throws Exception {
        //변환 파일 리스트
        List<UploadFile> fileList = new ArrayList<>();
        if (multipartFiles.isEmpty()) {
            return fileList;
        }

        //전달된 파일이 존재하는 경우
        if (!CollectionUtils.isEmpty(multipartFiles)) {
            //파일명을 업로드 한 날짜에 저장
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String current_date = now.format(dateTimeFormatter);

            //프로젝트 디렉토리 내 저장을 위한 절대 경로
            //경로 구분자 File.separator 사용
            String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;
            String path = "images";
            File file = new File(path);

            //디렉터리가 존재하지 않을 경우
            if (!file.exists()) {
                file.mkdir();
            }

            //다중 파일 처리
            for (MultipartFile multipartFile : multipartFiles) {

                //파일의 확장자 추출
                String originalFileExtension;
                String contentType = multipartFile.getContentType();

                //확장자명이 존재하지 않을 경우 처리 X
                if (ObjectUtils.isEmpty(contentType)) {
                    break;
                }
                //확장자가 jpeg, png, gif 인 파일들만 받아서 처리
                else {
                    if (contentType.contains("image/jpeg"))
                        originalFileExtension = ".jpg";
                    else if (contentType.contains("image/png"))
                        originalFileExtension = ".png";
                    else if (contentType.contains("image/gif"))
                        originalFileExtension = ".gif";
                    else
                        break;
                }

                //파일명 중복을 피하고자 나노초까지 얻어와 지정
                String new_file_name = Long.toString(System.nanoTime()) + originalFileExtension;
                String originFileName = multipartFile.getOriginalFilename();
                String extension = originFileName.substring(originFileName.lastIndexOf("."));
                String uuid = UUID.randomUUID().toString();
                new_file_name = uuid + extension;

                //파일 DTO 생성
                UploadFile uploadFile = UploadFile.builder()
                        .originFileName(multipartFile.getOriginalFilename())
                        .fileName(new_file_name)
                        .filePath(path + File.separator + new_file_name)
                        .fileSize(multipartFile.getSize())
                        .build();

                //생성 후 리스트에 추가
                fileList.add(uploadFile);

                //업로드 한 파일 데이터를 지정한 파일에 저장
                file = new File(absolutePath + path + File.separator + new_file_name);
                multipartFile.transferTo(file);

                //파일 권한 설정(쓰기, 읽기)
                file.setWritable(true);
                file.setReadable(true);
                }
            }
        return fileList;
    }
    public UploadFile uploadFile(MultipartFile multipartFile) throws  IOException {
        String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;
        String path = "images";
        File file = new File(path);

        //디렉터리가 존재하지 않을 경우
        if (!file.exists()) {
            file.mkdir();
        }
        //파일 원래 이름
        String originFileName = multipartFile.getOriginalFilename();
        // 파일 이름으로 쓸 uuid 생성
        String uuid = UUID.randomUUID().toString();
        // 확장자 추출(ex : .png)
        String extension = originFileName.substring(originFileName.lastIndexOf("."));
        // uuid와 확장자 결함
        String savedName = uuid + extension;

        UploadFile uploadFile = UploadFile.builder()
                .originFileName(multipartFile.getOriginalFilename())
                .fileName(savedName)
                .filePath(path + File.separator + savedName)
                .fileSize(multipartFile.getSize())
                .build();

        file = new File(absolutePath + path + File.separator + savedName);
        multipartFile.transferTo(file);

        file.setWritable(true);
        file.setReadable(true);
        return uploadFile;
    }
    public void delete(String absolutePath) {
        File file = new File(absolutePath);
        file.delete();
    }
}
