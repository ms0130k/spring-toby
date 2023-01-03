package springbook.test;

public class Father extends GrandFather {

    public Father(int i) {
        super(i);
    }
    
    void print() {
        System.out.println(i);
    }
}
