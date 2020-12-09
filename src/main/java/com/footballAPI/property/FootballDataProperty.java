package com.footballAPI.property;

import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@ConfigurationProperties("app.footballdata")
@EnableConfigurationProperties
@Data
@EnableAutoConfiguration
@ComponentScan
public final class FootballDataProperty {
    private String url = "http://api.football-data.org/v2/";
    private String token = "e34185592a4546899c8a7372c6a5ee42";
}
