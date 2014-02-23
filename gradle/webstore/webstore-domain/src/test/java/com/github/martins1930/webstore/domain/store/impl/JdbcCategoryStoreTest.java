package com.github.martins1930.webstore.domain.store.impl;

import com.github.martins1930.webstore.domain.Category;
import com.github.martins1930.webstore.domain.store.CategoryStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by martin on 2/23/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:webstore-domain-test.xml")
public class JdbcCategoryStoreTest {

    @Autowired
    private CategoryStore categoryStore;

    @Test
    public void shouldFindCategoryByName() throws Exception {
        String categoryName = "Sport";
        Category sportCategory = categoryStore.findByName(categoryName);

        assertNotNull("Not found the category " + categoryName, sportCategory);
        assertEquals(
                Category.createCategory(0, categoryName, "Search for all sports stuff!!"),
                sportCategory
        );
    }

    @Test
    public void shouldReturnNullWhenNotFoundCategoryByName() throws Exception {
        String categoryName = "CategoryThatNotExists";
        Category notExistsCategory = categoryStore.findByName(categoryName);

        assertNull("This category must not exists!! " + categoryName, notExistsCategory);
    }
}
