package Runner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import FileTypeConverter.LineTextToJSONConverter;
import Helper.LineProcessingException;

public class ConverterRunner {
    public static void main(String[] args) {
        // Will try to make this part of program args
        String inputFileName = args[0];
        String inputFilePath = "src/InputFiles/" + inputFileName;
        String chatOwner = "Matthew Zane";
        
        FileReader inputFile;
        BufferedReader inputReader = null;

        System.out.println("Openning file: " + inputFileName);
        // Open input file
        try {
            inputFile = new FileReader(inputFilePath);
            inputReader = new BufferedReader (inputFile); 
        } catch (FileNotFoundException e) {
            System.out.println("File: " + inputFilePath + " not found");
            e.printStackTrace();
            System.exit(1);
        }
        
        System.out.println("Successfully openned file: " + inputFileName);
        
        LineTextToJSONConverter converter = new LineTextToJSONConverter(chatOwner);
        boolean noErrors = true;

        System.out.println("Reading file: " + inputFileName);
        
        try {
            // skip first 3 line
            inputReader.readLine();
            inputReader.readLine();
            inputReader.readLine();


            // Read each line in text file
            String currLine = null;
            for (int lineNumber = 0; (currLine = inputReader.readLine()) != null; lineNumber++) {
                // Process line
                try {
                    converter.processLine(currLine);
                } catch (LineProcessingException err) {
                    System.out.println("Line " + lineNumber + ": \"" + currLine +
                            "\" could not be processed" );
                    noErrors = false;
                    continue;
                }          
            }
            converter.finish();
            inputReader.close();
        } catch (IOException e) {
            System.out.println("There was a problem reading file: " + inputFilePath);
            e.printStackTrace();
            System.exit(1);
        }
        
        System.out.println("Successfully read contents of file: " + inputFilePath);
        // Open output file
        // Replace txt extension with json 
        String outputFileName = inputFileName.substring(0, inputFileName.length() - 3) + "json";
        String outputFilePath = "src/OutputFiles/" + outputFileName;
        FileWriter outputFile = null;
        
        System.out.println("Converting file: " + inputFileName + " to file: " + outputFileName);
        try {
            outputFile = new FileWriter(outputFilePath);
        } catch (IOException e) {
            System.out.println("There was a problem opening file: " + outputFilePath);
            System.exit(1);
        }
        
        // Write to output file
        try {
            outputFile.write(converter.getJsonText());
            outputFile.close();
        } catch (IOException e) {
            System.out.println("There was a problem outputing to " + outputFilePath);
            System.exit(1);
        }

        if (noErrors) {
            // Print Success Message
            System.out.println("Successfully converted " + inputFileName + 
                    " to " + outputFileName + " with no errors!");
        }
        else {
            System.out.println("Successfully converted " + inputFileName + 
                    " to " + outputFileName + ", but there was at least one"
                    + " error converting a line!");
        }
        
        //System.out.print(converter.testSequentialTimes());
    }
}
