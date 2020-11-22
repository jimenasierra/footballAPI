package com.footballAPI.dto;

import lombok.*;

import java.util.Date;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerDto {
    private Long id;
    private String name;
    private String position;
    private Date dateOfBirth;
    private String countryOfBirth;
    private String nationality;
    private TeamDto team;
}
