package com.wedul.estest.domain.student.service;

import com.wedul.estest.domain.student.dto.StudentDto;
import org.apache.http.HttpHeaders;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class StudentServiceTest {

    private static final String GZIP_ENCODING = "gzip";
    @Autowired
    private StudentService studentService;

    @Test
    @DisplayName("bulk update response 확인")
    void bulk_update_response_check() throws IOException {
        // given
        List<StudentDto> studentDtoList = Arrays.asList(StudentDto.builder()
                .age(10)
                .id(1L)
                .name("wedul")
                .build(),
            StudentDto.builder()
                .age(20)
                .id(2L)
                .name("chul")
                .build()
        );

        // when
        BulkResponse bulkItemResponses = studentService.updateStudent(studentDtoList);

        // then
        Arrays.stream(bulkItemResponses.getItems()).forEach(item -> {
            System.out.println(item.getResponse());
        });
    }

    @Test
    @DisplayName("low level client 사용")
    void low_level_client_test() throws IOException {
        // given
        List<StudentDto> studentDtoList = Arrays.asList(StudentDto.builder()
                .age(10)
                .id(1L)
                .name("wedul")
                .build(),
            StudentDto.builder()
                .age(20)
                .id(2L)
                .name("chul")
                .build()
        );

        // when
        Response response = studentService.updateStudentUseLowLevelClient(studentDtoList);

        // then
        InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
        Stream<String> streamOfString= new BufferedReader(reader).lines();
        String streamToString = streamOfString.collect(Collectors.joining());
        System.out.println(streamToString);

        // gzip 확인
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals(GZIP_ENCODING, response.getHeader(HttpHeaders.CONTENT_ENCODING));
        assertThat(response.getEntity(), instanceOf(GzipDecompressingEntity.class));

        String body = EntityUtils.toString(response.getEntity());
        System.out.println(body);
    }

}
