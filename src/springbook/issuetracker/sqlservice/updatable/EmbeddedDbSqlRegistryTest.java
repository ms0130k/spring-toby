package springbook.issuetracker.sqlservice.updatable;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import springbook.issuetracker.sqlservice.AbstractUpdatableSqlRegistryTest;
import springbook.issuetracker.sqlservice.SqlUpdateFailureException;
import springbook.issuetracker.sqlservice.UpdatableSqlRegistry;

public class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {
    EmbeddedDatabase db;

    @Override
    protected UpdatableSqlRegistry createUpdatableRegistry() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("classpath:/springbook/issuetracker/sqlservice/updatable/sqlRegistrySchema.txt")
                .build();
        EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
        embeddedDbSqlRegistry.setDataSource(db);
        
        return embeddedDbSqlRegistry;
    }
    
    @After
    public void tearDown() {
        db.shutdown();
    }
    
    @Test
    public void transactionUpdate() {
        checkFind("SQL1", "SQL2", "SQL3");
        Map<String, String> sqlmap = new HashMap<>();
        sqlmap.put("KEY1", "Modified");
        sqlmap.put("KEY9999!@#$", "Modified9999");
        
        try {
            sqlRegistry.updateSql(sqlmap);
            fail();
        } catch (SqlUpdateFailureException e) {
            checkFind("SQL1", "SQL2", "SQL3");
        }
    }
}
