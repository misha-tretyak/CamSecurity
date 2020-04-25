package ua.lviv.lgs.CamSecurity.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;


@Getter
@Setter
@EqualsAndHashCode

@Entity
public class Goods {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(unique = true)
    private Integer code;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Long price;

    @OneToMany
    private List<Image> image = new LinkedList<>();

    @ManyToOne
    @NotNull
    private Groups group;
}