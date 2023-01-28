package com.codueon.boostUp.domain.member.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberImage {

    private String originFileName;
    private String fileName;
    private String filePath;
    private Long fileSize;

    @Builder
    public MemberImage(String originFileName,
                       String fileName,
                       String filePath,
                       Long fileSize) {
        this.originFileName = originFileName;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }
}
