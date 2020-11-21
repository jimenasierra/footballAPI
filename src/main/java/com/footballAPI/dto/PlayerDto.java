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
    // java.util o java.sql?? O String directamente?
    private Date dateOfBirth;
    private String countryOfBirth;
    private String nationality;
    // aca sería Dto no? Se pondría en el mapper no?
    private TeamDto team;
}
