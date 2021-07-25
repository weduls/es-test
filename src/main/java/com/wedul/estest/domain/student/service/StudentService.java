package com.wedul.estest.domain.student.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedul.estest.domain.student.dto.StudentDto;
import com.wedul.estest.domain.student.querybuilder.StudentQueryBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final RestHighLevelClient restHighLevelClient;
    private final StudentQueryBuilder studentQueryBuilder;
    private final ObjectMapper objectMapper;

    public List<StudentDto> getStudent() throws IOException {
        SearchResponse searchResponse = restHighLevelClient.search(studentQueryBuilder.getStudentList(), RequestOptions.DEFAULT);
        return Arrays.stream(searchResponse.getHits().getHits())
            .map(document -> {
                try {
                    return objectMapper.readValue(document.getSourceAsString(), StudentDto.class);
                } catch (JsonProcessingException e) {
                    return null;
                }
            }).filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    public BulkResponse updateStudent(List<StudentDto> studentDtos) throws IOException {
         return restHighLevelClient.bulk(studentQueryBuilder.updateStudents(studentDtos), RequestOptions.DEFAULT);
    }

    public Response updateStudentUseLowLevelClient(List<StudentDto> studentDtos) throws IOException {
        Request request = new Request("POST", "_bulk");

        request.addParameter("filter_path", "items.*.error");
        request.setJsonEntity(studentQueryBuilder.updateStudentJson(studentDtos));

        return restHighLevelClient.getLowLevelClient().performRequest(request);
    }

}
