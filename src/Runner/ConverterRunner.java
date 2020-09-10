package Runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import FileTypeConverter.LineTextToJSONConverter;
import Helper.LineProcessingException;

public class ConverterRunner {
    public static void main(String[] args) {
        String inputFileName = "[LINE] Chat with Hinako Terasaki.txt";
        String inputFilePath = "src/InputFiles/" + inputFileName;
        File inputFile;
        Scanner inputScanner = null;

        // Open input file
        try {
            inputFile = new File(inputFilePath);
            inputScanner = new Scanner(inputFile); 
        } catch (FileNotFoundException e) {
            System.out.println("File: " + inputFilePath + " not found");
            e.printStackTrace();
            System.exit(1);
        }

        LineTextToJSONConverter converter = new LineTextToJSONConverter();
        boolean noErrors = true;

        // Read each line in text file
        for (int lineNumber = 0; inputScanner.hasNextLine(); lineNumber++) {
            String currLine = inputScanner.nextLine();
            
            // Process line
            try {
                converter.processLine(currLine);
            } catch (LineProcessingException e) {
                System.out.println("Line " + lineNumber + ": \"" + currLine +
                        "\" could not be processed" );
                noErrors = false;
                continue;
            }          
        }

        inputScanner.close();

        // Open output file
        // Replace txt extension with json 
        String outputFileName = inputFileName.substring(0, inputFileName.length() - 3) + "json";
        String outputFilePath = "src/OutputFiles/" + outputFileName;
        FileWriter outputFile = null;
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
    }
}
