package com.codueon.boostUp.domain.member.entity;

import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @Column(name = "MEMBER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String name;
    private String address;
    private String company;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

    @Enumerated(value = EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.COMMON_MEMBER;

    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE)
    private MemberImage memberImage;

    @Builder
    public Member(Long id,
                  String email,
                  String password,
                  String name,
                  String address,
                  String company,
                  List<String> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.address = address;
        this.company = company;
        this.roles = roles;
    }

    public void setMemberStatus(MemberStatus memberStatus) {
        this.memberStatus = memberStatus;
    }

    public void addMemberImage(MemberImage memberImage) {
        if (memberImage.getMember() != this) memberImage.setMember(this);
        this.memberImage = memberImage;
    }
}
