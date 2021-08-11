package com.wedul.estest.domain.student.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedul.estest.domain.student.dto.StudentDto;
import com.wedul.estest.domain.student.querybuilder.StudentQueryBuilder;
import lombok.RequiredArgsConstructor;
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
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        builder.addHeader("Accept-Encoding'", "gzip");
        builder.addHeader("Content-Encoding", "gzip");

        SearchResponse searchResponse = restHighLevelClient.search(studentQueryBuilder.getStudentList(), builder.build());
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

    public List<StudentDto> getStudentUseLowLevelClient() throws IOException {
        Request request = new Request("POST", "_search");

        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        builder.addHeader("Accept-Encoding'", "gzip");
        builder.addHeader("Content-Encoding", "gzip");

        request.setOptions(builder);

        Response response = restHighLevelClient.getLowLevelClient().performRequest(request);

        return null;
    }

    public BulkResponse updateStudent(List<StudentDto> studentDtos) throws IOException {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        builder.addHeader("Accept-Encoding'", "gzip");
        builder.addHeader("Content-Encoding", "gzip");

        return restHighLevelClient.bulk(studentQueryBuilder.updateStudents(studentDtos), builder.build());
    }

    public Response updateStudentUseLowLevelClient(List<StudentDto> studentDtos) throws IOException {
        Request request = new Request("POST", "_bulk");

        request.addParameter("filter_path", "items.*.error");
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        builder.addHeader("Accept-Encoding'", "gzip");
        builder.addHeader("Content-Encoding", "gzip");
        request.setOptions(builder);
        request.setJsonEntity(studentQueryBuilder.updateStudentJson(studentDtos));

        return restHighLevelClient.getLowLevelClient().performRequest(request);
    }

}
