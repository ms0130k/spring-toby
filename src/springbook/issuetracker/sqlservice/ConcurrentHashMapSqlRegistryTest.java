package springbook.issuetracker.sqlservice;

public class ConcurrentHashMapSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {
    UpdatableSqlRegistry sqlRegistry;
    
    protected UpdatableSqlRegistry createUpdatableRegistry() {
        return new ConcurrentHashMapSqlRegistry();
    };
}
