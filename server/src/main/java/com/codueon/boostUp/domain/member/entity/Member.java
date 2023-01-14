package com.codueon.boostUp.domain.member.entity;

import com.codueon.boostUp.global.utils.Auditable;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends Auditable {
    @Id
    @Column(name = "MEMBER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;

    @Setter
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

    @Enumerated(value = EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.COMMON_MEMBER;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private MemberImage memberImage;

    @Builder
    public Member(Long id,
                  String email,
                  String password,
                  String name,
                  AccountStatus accountStatus,
                  List<String> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.accountStatus = accountStatus;
        this.roles = roles;
    }

    public void setMemberStatus(MemberStatus memberStatus) {
        this.memberStatus = memberStatus;
    }

    public void addMemberImage(MemberImage memberImage) {
        if (memberImage.getMember() != this) memberImage.setMember(this);
        this.memberImage = memberImage;
    }

    public void editNewPassword(String password) {
        this.password = password;
    }
}
