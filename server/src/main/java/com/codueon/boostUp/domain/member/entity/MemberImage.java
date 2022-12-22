package com.codueon.boostUp.domain.member.entity;

import lombok.Getter;
import lombok.Builder;
import lombok.AccessLevel;
import javax.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberImage {
    @Id
    @Column(name = "MEMBER_IMAGE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String originFileName;
    private String fileName;
    private String filePath;
    private Long fileSize;

    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Builder
    public MemberImage(Long id,
                       String originFileName,
                       String fileName,
                       String filePath,
                       Long fileSize) {
        this.id = id;
        this.originFileName = originFileName;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
