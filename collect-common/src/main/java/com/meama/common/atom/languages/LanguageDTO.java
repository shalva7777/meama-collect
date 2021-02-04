package com.meama.common.atom.languages;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class LanguageDTO implements Serializable {

    private static final long serialVersionUID = -3009157732242241207L;

    private Long id;
    @NotNull
    @Pattern(regexp = "^(?!-)[a-zA-Z0-9_-]{1,20}$")
    private String code;
    @NotNull
    @Size(min = 1, max = 50)
    private String name;
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
