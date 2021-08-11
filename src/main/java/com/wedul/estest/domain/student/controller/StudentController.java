package com.wedul.estest.domain.student.controller;

import com.wedul.estest.domain.student.dto.StudentDto;
import com.wedul.estest.domain.student.request.StudentRequest;
import com.wedul.estest.domain.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    @PostMapping("")
    public void upsertStudent(@RequestBody StudentRequest studentRequest) throws IOException {
        studentService.updateStudent(studentRequest.getStudents());
    }

    @GetMapping("")
    public List<StudentDto> all() throws IOException {
        return studentService.getStudentUseLowLevelClient();
    }

}
