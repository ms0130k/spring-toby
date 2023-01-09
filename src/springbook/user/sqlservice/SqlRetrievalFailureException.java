package springbook.user.sqlservice;

public class SqlRetrievalFailureException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 2730242373387657735L;

    public SqlRetrievalFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlRetrievalFailureException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }
}
