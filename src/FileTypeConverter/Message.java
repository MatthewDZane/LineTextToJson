package FileTypeConverter;

import java.util.Calendar;

import Helper.ContentType;

public class Message {

    public static final String YOU_UNSENT_MESSAGE_MARKER = "You unsent";

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
            return "";
        case VIDEO:
            return "";
        case STICKER:
            return "";
        case LINK:
            return "";
        case UNSENT:
            return "";
        case TEXT:
            return message;
        default:
            return null;
        }
    }

    public static ContentType parseContentType(String messageContent) {
        if (messageContent.equals("[Photo]")) {
            return ContentType.PHOTO;
        }
        else if (messageContent.equals("[Video]")) {
            return ContentType.VIDEO;
        }
        else if (messageContent.equals("[Sticker]")) {
            return ContentType.STICKER;
        }
        else if (isLinkType(messageContent)) {
            return ContentType.LINK;
        }
        else {
            return ContentType.TEXT;
        }



    }

    private static boolean isLinkType(String messageContent) {
        if (messageContent.length() < 6) {
            return false;
        }
        return messageContent.substring(0, 6).equals("https:");
    }

    public void appendContent(String content) {
        message += "\n" + content;
    }

}
