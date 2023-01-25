
public class StringPrinter implements Printer {
    private StringBuffer bf = new StringBuffer();

    @Override
    public void print(String message) {
        bf.append(message);
    }

    @Override
    public String toString() {
        return bf.toString();
    }
}
