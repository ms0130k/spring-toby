import java.sql.SQLException;

public interface Main {

    public static void main(String[] args) {
        try {
            throwE();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        try {
//            throwE();
//        } catch (Exception e) {
//            System.out.println("catch");
//            System.out.println(((SQLException) e.getCause()).getErrorCode());
//            System.out.println(e instanceof SQLException);
//            System.out.println(e.getCause() instanceof SQLException);
//        } finally {
//            System.out.println("finally");
//        }
    }
    
    public static void throwE() {
        throw new RuntimeException(new SQLException("message"));
//        throw new SQLException("message");
    }
}
