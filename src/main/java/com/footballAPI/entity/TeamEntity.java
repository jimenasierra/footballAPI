package com.footballAPI.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "team")
public class TeamEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "tla")
    private String tla;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "area_name")
    private String areaName;

    @Column(name = "email")
    private String email;

    @Column
    @OneToMany()
    @JoinColumn(name = "squad_fk")
    private List<PlayerEntity> squad;
}
