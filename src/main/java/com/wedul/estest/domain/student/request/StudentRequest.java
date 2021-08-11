package com.wedul.estest.domain.student.request;

import com.wedul.estest.domain.student.dto.StudentDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class StudentRequest {

    private List<StudentDto> students;

    @Builder
    public StudentRequest(List<StudentDto> students) {
        this.students = students;
    }
}
