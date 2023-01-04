package springbook.learningtest.spring;

public class Message {
    String text;

    private Message(String text) {
        this.text = text;
    }
    
    public static Message newMessage(String text) {
        return new Message(text);
    }
    
    public String getText() {
        return text;
    }
}
