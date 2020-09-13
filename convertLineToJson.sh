#!/bin/bash
# Scripts to compile and run java code
# Takes two parameters [Name of input file in src/InputFiles directory]  [Name of owner of chat]
javac -Xlint:all -d bin/ src/Helper/*.java src/FileTypeConverter/*.java src/Runner/*.java
java -cp bin/ Runner/ConverterRunner "$1" "$2"
