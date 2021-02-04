package com.meama.atom.language.storage.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Language implements Serializable {

    private static final long serialVersionUID = -3009157732242241207L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    @Column(columnDefinition = "boolean default false")
    private boolean main;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }
}
