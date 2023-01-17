package springbook.learningtest.spring.embeddeddb;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

import java.io.File;
import java.util.List;
import java.util.Map;


public class EmbeddedDbTest {
    EmbeddedDatabase db;
    SimpleJdbcTemplate template;

    @Before
    public void setUp() {
        db = new EmbeddedDatabaseBuilder()
                .setType(HSQL)
                .addScript("classpath:/springbook/learningtest/spring/embeddeddb/schema.txt")
                .addScript("classpath:/springbook/learningtest/spring/embeddeddb/data.txt")
                .build();
        
        template = new SimpleJdbcTemplate(db);
    }

    @After
    public void tearDown() {
        db.shutdown();
    }
    
    @Test
    public void initData() {
        assertThat(template.queryForInt("SELECT COUNT(*) FROM sqlmap"), is(2));
        List<Map<String, Object>> list = template.queryForList("SELECT * FROM sqlmap ORDER BY key_");
        
        assertThat((String) list.get(0).get("key_"), is("KEY1"));
        assertThat((String) list.get(0).get("sql_"), is("SQL1"));
        assertThat((String) list.get(1).get("key_"), is("KEY2"));
        assertThat((String) list.get(1).get("sql_"), is("SQL2"));
    }
    
    @Test
    public void insert() {
        template.update("INSERT INTO sqlmap(key_, sql_) VALUES(?, ?)", "KEY3", "SQL3");
        assertThat(template.queryForInt("SELECT COUNT(*) FROM sqlmap"), is(3));
    }
}
