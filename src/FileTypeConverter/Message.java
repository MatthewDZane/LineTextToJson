package FileTypeConverter;

import java.util.Calendar;

import Helper.ContentType;

public class Message {
    private Calendar sendTime;
    
    private String sender;
    
    private String message;
    
    private ContentType contentType;
    
    public Calendar getSendTime() { return sendTime; }
    public String getSender() { return sender; }
    public String getMessage() { return message; }
    public ContentType getContentType() { return contentType; }

    public void setSendTime(Calendar sendTimeIn) { sendTime = sendTimeIn; }
    public void setSender(String senderIn) { sender = senderIn; }
    public void setMessage(String messageIn) { message = messageIn; }
    public void setContentType(ContentType contentTypeIn) { contentType = contentTypeIn; }
    
    public Message(Calendar timeIn, String senderIn, 
                   String messageIn, ContentType contentTypeIn) {
        sendTime = timeIn;
        
        sender = senderIn;
        
        message = messageIn;
        contentType = contentTypeIn;
    }

    public String getContent() {
        switch(contentType) {
        case PHOTO:
            return "[PHOTO]";
        case VIDEO:
            return "[VIDEO]";
        case STICKER:
            return "[STICKER]";
        case LINK:
            return "[LINK]";
        case TEXT:
            return message;
        default:
            return null;
        }
    }
    
    public static ContentType parseContentType(String messageContent) {
        switch(messageContent) {
        case "[PHOTO]":
            return ContentType.PHOTO;
        case "[VIDEO]":
            return ContentType.VIDEO;
        case "[STICKER]":
            return ContentType.STICKER;
        case "[LINK]":
            return ContentType.LINK;
        default:
            return ContentType.TEXT;
        }
    }
    
    public void appendContent(String content) {
        message += "\n" + content;
    }
    
}
