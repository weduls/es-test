package com.wedul.estest.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@ConfigurationProperties("spring.elasticsearch")
@Validated
public class ElasticsearchConfiguration {
    @NotNull
    private String host;

    @NotNull
    private int port;
}