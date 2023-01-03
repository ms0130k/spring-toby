package springbook.user.service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import org.junit.Test;

public class DecoTest {

    @Test
    public void test() throws IOException {
        String filePath = Paths.get(System.getProperty("user.dir"), "resource", "a.txt").toString();
//        System.out.println(filePath);
        InputStream is = new BufferedInputStream(new FileInputStream(filePath));
//        System.out.println(is.read());
//        System.out.println(is.read());
//        System.out.println(is.read());
        
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath));
        System.out.println(bis.read());
        
//        FileInputStream fis = new FileInputStream(filePath);
//        System.out.println(fis.read());
    }
    
    
}
