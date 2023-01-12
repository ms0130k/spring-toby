package springbook.user.sqlservice;

import org.springframework.oxm.Unmarshaller;

public class OxmSqlService implements SqlService {
    private final OxmSqlReader oxmSqlReader = new OxmSqlReader();
    
    private Unmarshaller unmarshaller;
    private String sqlmapFile;
    
    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }
    
    public void setSqlmapFile(String sqlmapFile) {
        this.sqlmapFile = sqlmapFile;
    }
    
    
    private class OxmSqlReader implements SqlReader {
        @Override
        public void read(SqlRegistry sqlRegistry) {
            // TODO Auto-generated method stub
            
        }
        
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        
        return null;
    }

}
