package com.github.martins1930.webstore.domain.store;

import com.github.martins1930.webstore.domain.Category;

/**
 * Created by martin on 2/22/14.
 */
public interface CategoryStore {

    void save(Category category);

    Category findById(Integer name);

    Category findByName(String name);
}
