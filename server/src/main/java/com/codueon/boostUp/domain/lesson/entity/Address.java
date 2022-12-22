package com.codueon.boostUp.domain.lesson.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADDRESS_ID")
    private Long id;
    private String address;
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<LessonAddress> lessonAddresses = new ArrayList<>();
    @Builder
    public Address(Long id, String address) {
        this.id = id;
        this.address = address;
    }
}
