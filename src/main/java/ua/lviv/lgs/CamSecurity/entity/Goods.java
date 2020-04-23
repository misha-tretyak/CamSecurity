package ua.lviv.lgs.CamSecurity.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.*;


@Getter
@Setter
@EqualsAndHashCode

@Entity
public class Goods {

    @Id
    @GeneratedValue
    private Long id;

    private Integer code;

    private String name;

    private String description;

    private Long price;

    @OneToMany
    private List<Image> image = new LinkedList<>();

    @ManyToOne
    private Groups group;
}
