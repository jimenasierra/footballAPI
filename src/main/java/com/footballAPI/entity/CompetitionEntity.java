package com.footballAPI.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "competition")
public class CompetitionEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;


    @Column(name = "areaName")
    private String areaName;



}
