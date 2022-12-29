package com.codueon.boostUp.global.file;

import lombok.*;
import net.bytebuddy.implementation.bind.annotation.BindingPriority;

@Getter
@Setter
@NoArgsConstructor
public class UploadFile {
    private String originFileName;
    private String fileName;
    private String filePath;
    private Long fileSize;

    @Builder
    public UploadFile(String originFileName,
                      String fileName,
                      String filePath,
                      Long fileSize) {
        this.originFileName = originFileName;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }
}
