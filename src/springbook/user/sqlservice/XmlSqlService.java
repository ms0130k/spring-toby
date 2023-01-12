package springbook.user.sqlservice;

import javax.annotation.PostConstruct;

public class XmlSqlService implements SqlService {
    private SqlReader sqlReader;
    private SqlRegistry sqlRegistry;
    
    public void setSqlReader(SqlReader sqlReader) {
        this.sqlReader = sqlReader;
    }
    
    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }
    
    @PostConstruct
    public void loadSql() {
        sqlReader.read(sqlRegistry);
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        try {
            return sqlRegistry.findSql(key);
        } catch (SqlNotFoundException e) {
            throw new SqlRetrievalFailureException(e);
        }
    }
}
