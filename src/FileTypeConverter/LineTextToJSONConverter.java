package FileTypeConverter;

import Helper.LineProcessingException;

public class LineTextToJSONConverter {
    
    public String processLine(String currLine) throws LineProcessingException {
        if(currLine == null) {
            throw new LineProcessingException();
        }
        return "";
    }
}
