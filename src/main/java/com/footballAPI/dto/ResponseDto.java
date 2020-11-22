package com.footballAPI.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDto {
    private HttpStatus httpStatus;
    private int httpCode;
    private String message;
    private long total;

}
