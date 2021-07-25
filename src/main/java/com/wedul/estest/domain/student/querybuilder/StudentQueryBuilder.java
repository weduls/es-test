package com.wedul.estest.domain.student.querybuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedul.estest.domain.student.dto.StudentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class StudentQueryBuilder {

    private static final String ALIAS = "student";
    private final ObjectMapper objectMapper;

    public SearchRequest getStudentList() {
        return new SearchRequest(ALIAS).source(SearchSourceBuilder.searchSource()
            .query(QueryBuilders.matchAllQuery()));
    }

    public BulkRequest updateStudents(List<StudentDto> studentDtoList) {
        BulkRequest bulkRequest = new BulkRequest();

        studentDtoList.stream().map(studentDto -> {
            UpdateRequest updateRequest = new UpdateRequest();
            try {
                String doc = objectMapper.writeValueAsString(studentDto);
                updateRequest.index(ALIAS)
                    .id(studentDto.getId().toString())
                    .doc(doc, XContentType.JSON)
                    .docAsUpsert(true);
            } catch (JsonProcessingException e) {
                log.error("request create error", e);
            }
            return updateRequest;
        }).forEach(bulkRequest::add);
        return bulkRequest;
    }

    public String updateStudentJson(List<StudentDto> studentDtoList) {
        return studentDtoList.stream().map(studentDto -> {
            try {
                String update = "{ \"update\" : {\"_id\" : " + studentDto.getId() + ", \"_index\" : \"" + ALIAS + "\"} }";
                String body = "{ \"doc\" : " + objectMapper.writeValueAsString(studentDto) + ", \"doc_as_upsert\" : true }";
                return update + "\n" + body + "\n";
            } catch (JsonProcessingException e) {
                log.error("request create error", e);
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.joining());
    }

}
