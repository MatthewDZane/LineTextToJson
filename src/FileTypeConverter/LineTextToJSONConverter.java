package FileTypeConverter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Helper.ContentType;
import Helper.LineProcessingException;

public class LineTextToJSONConverter {
    private static final String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    
    private static final int MONTH_START_INDEX = 5;
    private static final int MONTH_END_INDEX = 7;
    private static final int DAY_START_INDEX = 8;
    private static final int DAY_END_INDEX = 10;
    private static final int YEAR_START_INDEX = 11;
    private static final int YEAR_END_INDEX = 15;
    
    private static final int HOUR_START_INDEX = 0;
    private static final int HOUR_END_INDEX = 2;
    private static final int MIN_START_INDEX = 3;
    private static final int MIN_END_INDEX = 5;
    
    private static final int HOURS_IN_DAY = 24;
    private static final int MIN_IN_HOUR = 60;
    
    private static final int NAME_START_INDEX = 6;
    
    private Set<String> participants = new HashSet<String>();
    private List<Message> messages = new ArrayList<Message>();
    
    private Calendar saveDate;
    private Time saveTime;
    
    private Calendar currDate = Calendar.getInstance();
    
    private String previousLine = null;
    
    private Message getLastMessage() {
        if (messages.size() > 1) {
            return messages.get(messages.size() - 1);
        }
        return null;
    }
    
    public void processLine(String currLine) throws LineProcessingException {
        // figure out what type of line based on first word
        String firstWord = getFirstWord(currLine);
        
        if (isDateLine(firstWord)) {
            parseDate(currLine);
        }
        else {
            // append previous line to previous message content
            if (previousLine != null) {
               Message lastMessage = getLastMessage();
               lastMessage.appendContent(previousLine);
               previousLine = null;
            }
            
            
            if (isIgnorableLine(firstWord)) {
                //skip line, because there is no info in this that we need
                return;
            } 
            else if (isTimeWord(firstWord)) {
                parseMessage(currLine);
            }
            else {
                // save line for next line processing
                previousLine = currLine;
            }
        }
        
    }
    
    private String getFirstWord(String currLine) {
        for(int i = 0; i < currLine.length(); i++) {
            if (currLine.charAt(i) == ' ' || currLine.charAt(i) == ',' || currLine.charAt(i) == '\t') {
                return currLine.substring(0, i);
            }
        }
        
        return currLine;
    }
    
    private boolean isIgnorableLine(String firstWord) {
        return firstWord.equals("[LINE]");
    }
    
    private boolean isDateLine(String firstWord) {
        for (String day : days) {
            if (firstWord.equals(day)) {
                return true;
            }
        }
        return false;
    }
    
    private void parseDate(String dateLine) throws LineProcessingException {
        // clear the currDate
        currDate.clear();
        
        // skip first word by start at the 6th char
        String monthString = dateLine.substring(MONTH_START_INDEX, MONTH_END_INDEX);
        String dayString = dateLine.substring(DAY_START_INDEX, DAY_END_INDEX);
        String yearString = dateLine.substring(YEAR_START_INDEX, YEAR_END_INDEX);
        try {
            int month = Integer.parseInt(monthString);
            int day = Integer.parseInt(dayString);
            int year = Integer.parseInt(yearString);
            
            
            currDate.set(year, month, day, 0, 0, 0);
        } catch (NumberFormatException e) {
            throw new LineProcessingException();
        }
        
    }
    
    private boolean isTimeWord(String firstWord) {
        String hourString;
        String minString;
        try {
            hourString = firstWord.substring(HOUR_START_INDEX, HOUR_END_INDEX);
            minString = firstWord.substring(MIN_START_INDEX, MIN_END_INDEX);
        } catch (StringIndexOutOfBoundsException e) {
            return false;
        }
        
        try {
            int hour = Integer.parseInt(hourString);
            int min = Integer.parseInt(minString);

            return hour < HOURS_IN_DAY && min < MIN_IN_HOUR;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private Calendar parseTime(String timeWord) throws LineProcessingException {
        String hourString = timeWord.substring(HOUR_START_INDEX, HOUR_END_INDEX);
        String minString = timeWord.substring(MIN_START_INDEX, MIN_END_INDEX);
        
        try {
            int hour = Integer.parseInt(hourString);
            int min = Integer.parseInt(minString);

            Calendar time = Calendar.getInstance();
            time.set(currDate.get(Calendar.YEAR), currDate.get(Calendar.MONTH),
                     currDate.get(Calendar.DATE), hour, min, 0);
            return time;
        } catch (NumberFormatException e) {
            throw new LineProcessingException();
        }
    }
    
    private void parseMessage(String messageLine) throws LineProcessingException {
        String firstWord = getFirstWord(messageLine);
        Calendar messageTime = parseTime(firstWord);
        
        /// find participant name by searching for second space char after time
        int nameEndIndex = messageLine.length();
        for (int i = firstWord.length() + 1; i < messageLine.length(); i++) {
            if (messageLine.charAt(i) == '\t') {
                nameEndIndex = i;
                break;
               
            }
        }
        
        String participant = messageLine.substring(NAME_START_INDEX, nameEndIndex);
        participants.add(participant);
        
        String content = messageLine.substring(nameEndIndex + 1);
        
        // parse type of content
        ContentType contentType = Message.parseContentType(messageLine);
        Message currMessage = new Message(messageTime, participant, content, contentType);
        
        messages.add(currMessage);
    }
    
    public String getJsonText() {
        String jsonText = "{\n";
        
        jsonText += getParticipantsText();
        
        jsonText += getMessagesText();
        
        jsonText += "}";
        
        return jsonText;
    }
    
    private String getParticipantsText() {
        String participantsText = "  \"participants\": [\n";
        
        for (String participant : participants) {
            participantsText += "    {\n";
            
            participantsText += "      \"name\": \"" + participant + "\"\n";
        
            participantsText += "    },\n";
        }
        
        participantsText = participantsText.substring(0, 
                                participantsText.length() - 2);
        participantsText += "\n";
        
        participantsText += "  ],\n";
        return participantsText;
    }
    
    private String getMessagesText() {
        String messagesText = "  \"messages\": [\n";
        
        for (Message message : messages) {
            messagesText += "    {\n";
            
            messagesText += "      \"sender_name\": \"" + message.getSender() + "\",\n";
            messagesText += "      \"timestamp_ms\": " + message.getSendTime().getTimeInMillis() + ",\n";
            messagesText += "      \"content\": \"" + message.getContent() + "\",\n";
            messagesText += "      \"type\": \"Generic\"\n";
            
            messagesText += "    },\n";
        
        }
        
        messagesText = messagesText.substring(0, 
                messagesText.length() - 2);
        messagesText += "\n";
        
        messagesText += "  ]\n";

        return messagesText;
    }
}
