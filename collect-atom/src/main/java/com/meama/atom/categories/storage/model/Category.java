package com.meama.atom.categories.storage.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Category implements Serializable {

    private static final long serialVersionUID = -3009157732242241207L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    private String imageUrl;
    private Long parentCategoryId;
    @Column(columnDefinition = "boolean default false")
    private boolean active;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryTranslation> translations;
    @Column(columnDefinition = "integer default 0")
    private int sortIndex;
    @Enumerated(value = EnumType.STRING)
    private CategoryType categoryType;
}
