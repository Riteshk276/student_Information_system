#!/bin/bash

# Check if MongoDB is running
echo "Checking if MongoDB is running..."
if nc -z localhost 27017 > /dev/null 2>&1; then
    echo "MongoDB is running."
else
    echo "MongoDB is not running. Please start MongoDB before running the application."
    echo "You can typically start MongoDB with: sudo service mongod start"
    exit 1
fi

# Check if Maven is installed
if command -v mvn > /dev/null 2>&1; then
    echo "Building project with Maven..."
    mvn clean package
    
    echo "Starting application..."
    java -jar target/student-information-system-1.0.0-jar-with-dependencies.jar
else
    echo "Maven not found. Trying to build manually..."
    
    # Create target directories if they don't exist
    mkdir -p target/classes
    
    # Check if java compiler is available
    if command -v javac > /dev/null 2>&1; then
        echo "Compiling Java files..."
        if [ -d "lib" ] && [ "$(ls -A lib/)" ]; then
            # Use libraries in lib folder if available
            CP="$(find lib -name "*.jar" | tr '\n' ':')"
            javac -d target/classes -cp "${CP}" src/main/java/com/studentinfo/*.java src/main/java/com/studentinfo/db/*.java src/main/java/com/studentinfo/gui/*.java
            
            echo "Starting application..."
            java -cp target/classes:${CP} com.studentinfo.gui.MainFrame
        else
            echo "MongoDB libraries not found in lib directory."
            echo "Please run 'mvn clean package' to download dependencies or place MongoDB Java driver jars in the lib directory."
            exit 1
        fi
    else
        echo "Java compiler not found. Please ensure you have JDK installed."
        exit 1
    fi
fi 