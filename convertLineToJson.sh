#!/bin/bash
javac -Xlint:all -d bin/ src/Helper/*.java src/FileTypeConverter/*.java src/Runner/*.java
java -cp bin/ Runner/ConverterRunner "$1"
