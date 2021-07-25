package com.wedul.estest.domain.student.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
@NoArgsConstructor
public class StudentDto implements Serializable {

    private Long id;
    private String name;
    private int age;

    @Builder
    public StudentDto(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

}
