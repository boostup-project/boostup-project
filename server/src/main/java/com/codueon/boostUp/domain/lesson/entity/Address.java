package com.codueon.boostUp.domain.lesson.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Address {
    private List<LessonAddress> lessonAddresses = new ArrayList<>();
}
