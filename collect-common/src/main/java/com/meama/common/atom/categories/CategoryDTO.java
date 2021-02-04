package com.meama.common.atom.categories;

import com.meama.common.common.StandardTranslationDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDTO implements Serializable {

    private Long id;
    @NotNull
    @Size(min = 2, max = 50)
    private String name;
    private String imageUrl;
    private Long parentCategoryId;
    private CategoryDTO parentCategory;
    private boolean active;
    private List<StandardTranslationDTO> translations;

}
