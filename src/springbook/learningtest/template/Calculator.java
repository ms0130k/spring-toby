package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    public int calcSum(String filepath) throws IOException {
        LineCallback<Integer> callback = new LineCallback<Integer>() {
            @Override
            public Integer doSomethingWithLine(String line, Integer result) {
                return result + Integer.parseInt(line);
            }
        };
        return template(filepath, 0, callback);
    }

    private <T> T template(String filepath, T init, LineCallback<T> callback)
            throws IOException, FileNotFoundException {
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line = null;
            T result = init;
            while ((line = br.readLine()) != null) {
                result = callback.doSomethingWithLine(line, result);
            }
            return result;
        }
    }

    public int calcMultiply(String filepath) throws IOException {
        LineCallback<Integer> callback = new LineCallback<Integer>() {
            @Override
            public Integer doSomethingWithLine(String line, Integer result) {
                return result * Integer.parseInt(line);
            }
        };
        return template(filepath, 1, callback);
    }

    public String calcConcatenate(String filepath) throws IOException {
        LineCallback<String> callback = new LineCallback<String>() {
            @Override
            public String doSomethingWithLine(String line, String result) {
                return result + line;
            }
        };
        return template(filepath, "", callback);
    }

    interface LineCallback<T> {
        T doSomethingWithLine(String line, T result);
    }
}
