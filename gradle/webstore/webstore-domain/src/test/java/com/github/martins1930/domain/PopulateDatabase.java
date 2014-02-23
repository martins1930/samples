package com.github.martins1930.domain;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;

/**
 * Created by martin on 2/22/14.
 */
@Component
public class PopulateDatabase {

    @Value("${hsqldb.path}")
    private String hsqldbPath;

    @Value("${user.home}")
    private String userHome;

    public static void main(String[] args) throws IOException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:webstore-domain-test.xml");
        final DataSource dsInitialization = applicationContext.getBean("dsInitialization", DataSource.class);
        final PopulateDatabase populateDatabase = applicationContext.getBean(PopulateDatabase.class);

        cleanHsqldbDirectory(populateDatabase);

        final Resource schemaResource = applicationContext.getResource("scripts/schema.sql");
        final Resource testDataResource = applicationContext.getResource("scripts/test-data.sql");
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(dsInitialization);
        JdbcTestUtils.executeSqlScript(jdbcTemplate, schemaResource,true);
        JdbcTestUtils.executeSqlScript(jdbcTemplate, testDataResource,true);
    }

    private static void cleanHsqldbDirectory(PopulateDatabase populateDatabase) throws IOException {
        String hsqldbAbsolutePath = populateDatabase.userHome+"/"+populateDatabase.hsqldbPath;
        System.out.printf("Removing all hsqldb data in: %s\n", hsqldbAbsolutePath);
        final File directoryHsqldb = new File(hsqldbAbsolutePath);
        FileUtils.cleanDirectory(directoryHsqldb);
    }
}
