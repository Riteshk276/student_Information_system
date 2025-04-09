# Student Information System

A Java project demonstrating Object-Oriented Programming principles including:
- Classes and Objects
- Inheritance (multilevel inheritance)
- Method and Constructor Overloading
- Interfaces
- Static Variables and Methods
- Graphical User Interface (GUI) with Swing
- Database Integration with MongoDB

## Project Structure

```
src/main/java/com/studentinfo/
├── Person.java               # Base class
├── Student.java              # Extends Person, implements GradeCalculator
├── GraduateStudent.java      # Extends Student (multilevel inheritance)
├── Course.java               # Course class
├── GradeCalculator.java      # Interface for grade calculation
├── Main.java                 # Console application class
├── db/                       # Database package
│   ├── MongoDBConnection.java # MongoDB connection utility
│   ├── StudentRepository.java # Repository for student operations
│   └── CourseRepository.java  # Repository for course operations
└── gui/                      # GUI package
    ├── MainFrame.java        # Main application window
    ├── StudentPanel.java     # Student management panel
    ├── CoursePanel.java      # Course management panel
    └── GradePanel.java       # Grade management panel
```

## Features

1. **Multilevel Inheritance**: Person -> Student -> GraduateStudent
2. **Interface Implementation**: Student implements GradeCalculator
3. **Method Overloading**: Multiple constructors and methods with different parameters
4. **Method Overriding**: Methods like displayInfo(), calculateGrade() are overridden in child classes
5. **Static Variables/Methods**: For counting total students, courses, etc.
6. **GUI Interface**: User-friendly interface for managing students, courses, and grades
7. **Database Integration**: MongoDB for persistent data storage

## Prerequisites

1. Java 11 or higher
2. MongoDB server installed and running (default: localhost:27017)
3. Maven (for dependency management)

## How to Compile and Run

### Using Maven

1. Navigate to the project root directory
2. Build the project:
   ```
   mvn clean package
   ```
3. Run the application:
   ```
   java -jar target/student-information-system-1.0.0-jar-with-dependencies.jar
   ```

### Using Command Line (without Maven)

1. Navigate to the project root directory
2. Compile the Java files:
   ```
   javac -d target/classes -cp path/to/mongodb-driver.jar src/main/java/com/studentinfo/*.java src/main/java/com/studentinfo/db/*.java src/main/java/com/studentinfo/gui/*.java
   ```
3. Run the Main class (Console version):
   ```
   java -cp target/classes:path/to/mongodb-driver.jar com.studentinfo.Main
   ```
4. Run the GUI application:
   ```
   java -cp target/classes:path/to/mongodb-driver.jar com.studentinfo.gui.MainFrame
   ```

### Using an IDE (Eclipse, IntelliJ IDEA, etc.)

1. Import the project into your IDE as a Maven project
2. Build the project
3. Run either the Main class (for console version) or MainFrame class (for GUI version)

## Database Configuration

By default, the application connects to a MongoDB server running on localhost:27017. You can modify the connection settings in `MongoDBConnection.java`.

The system uses the following MongoDB collections:
- `students`: Stores student information
- `courses`: Stores course information

## GUI Features

The application includes a user-friendly graphical interface with the following features:

### Dashboard
- Overview of system statistics
- Quick access to main functions

### Student Management
- Add new undergraduate and graduate students
- View list of students
- Edit and delete students
- Data is automatically saved to MongoDB

### Course Management
- Add new courses
- View list of courses
- Edit and delete courses
- Data is automatically saved to MongoDB

### Grade Management
- Assign/update grades for students in courses
- View all grades
- Calculate GPA for individual students
- View grade distribution statistics
- Grade data is stored with student records in MongoDB

## Class Descriptions

### Person
- Base class with basic person attributes (name, age, address)
- Has constructors, getters/setters and a displayInfo() method

### Student
- Extends Person and implements GradeCalculator
- Adds student-specific attributes (studentId, department)
- Manages course enrollment and grades
- Implements grade calculation methods

### GraduateStudent
- Extends Student (forming multilevel inheritance)
- Adds graduate-specific attributes (researchArea, supervisor, thesis status)
- Overrides grade calculation methods with stricter grading policies

### Course
- Contains course information (id, name, instructor, credits)
- Includes static variable to track total number of courses

### GradeCalculator
- Interface defining methods for grade calculation
- Implemented by Student class

### Database Classes
- **MongoDBConnection**: Singleton class to manage database connection
- **StudentRepository**: Handles CRUD operations for students
- **CourseRepository**: Handles CRUD operations for courses

### GUI Classes
- **MainFrame**: Main application window with tabbed interface
- **StudentPanel**: Panel for student management
- **CoursePanel**: Panel for course management
- **GradePanel**: Panel for grade management 