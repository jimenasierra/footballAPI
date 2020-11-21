package com.footballAPI.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "player")
public class PlayerEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "position")
    private String position;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "country_of_birth")
    private String countryOfBirth;

    @Column(name = "nationality")
    private String nationality;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "team_fk", foreignKey = @ForeignKey(name = "id"))
    private TeamEntity team;
}
