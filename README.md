<!-- Start the Project using on Mac or Linux Devices -->
```bash
chmod +x start.sh
./start.sh
```

# Student Information System

A comprehensive Java-based application designed to manage student records, courses, and grades with a user-friendly graphical interface and persistent data storage.

## Project Overview

The Student Information System is an educational management solution that demonstrates advanced object-oriented programming concepts while providing practical functionality for academic administration. The system allows for efficient management of student data, course information, and grade records through an intuitive GUI interface backed by MongoDB for data persistence.

## Key Technologies

- **Java 11**: Core programming language with OOP principles
- **Swing**: GUI framework for the desktop application interface
- **MongoDB**: NoSQL database for persistent data storage
- **Maven**: Dependency management and build automation
- **MongoDB Java Driver**: Integration layer between Java and MongoDB

## Object-Oriented Programming Features

- **Classes and Objects**: Modular design with specialized classes
- **Inheritance**: Multilevel inheritance with Person → Student → GraduateStudent
- **Polymorphism**: Method overriding for specialized behavior
- **Encapsulation**: Private fields with getter/setter methods
- **Interfaces**: GradeCalculator interface implementation
- **Method Overloading**: Multiple constructors and methods with different parameters
- **Static Members**: Class variables and methods for global tracking

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

## System Features

### Student Management
- Registration of undergraduate and graduate students
- Student information storage and retrieval
- Student record updating and deletion
- Differentiated handling of graduate vs. undergraduate students

### Course Management
- Course creation and cataloging
- Course information updates
- Course registration and enrollment
- Credits and instructor assignment

### Grade Management
- Course grade assignment and recording
- GPA calculation with customizable weighting
- Grade reports and statistics
- Performance tracking over time

### Database Integration
- Persistent data storage in MongoDB
- Data integrity and validation
- Efficient querying and data retrieval
- Real-time data updates

### Graphical User Interface
- Intuitive tabbed interface
- Form-based data entry
- Table-based data visualization
- Interactive reports and statistics

## Prerequisites

1. Java 11 or higher
2. MongoDB server installed and running (default: localhost:27017)
3. Maven (for dependency management)

## Installation and Setup

1. Clone the repository
2. Ensure MongoDB is running on localhost:27017
3. Build the project using Maven or the provided start script

## How to Run

### Using the Start Script (Recommended)

On macOS or Linux:
```bash
chmod +x start.sh
./start.sh
```

The script automatically checks for MongoDB, builds the project with Maven, and launches the application.

### Using Maven

1. Navigate to the project root directory
2. Build the project:
   ```bash
   mvn clean package
   ```
3. Run the application:
   ```bash
   java -jar target/student-information-system-1.0.0-jar-with-dependencies.jar
   ```

### Using an IDE (Eclipse, IntelliJ IDEA, etc.)

1. Import the project as a Maven project
2. Build the project
3. Run either the Main class (for console version) or MainFrame class (for GUI version)

## Database Configuration

By default, the application connects to a MongoDB server running on localhost:27017. You can modify the connection settings in `MongoDBConnection.java`.

The system uses the following MongoDB collections:
- `students`: Stores student information
- `courses`: Stores course information

## Class Descriptions

### Domain Classes

#### Person
- Base class with basic person attributes (name, age, address)
- Has constructors, getters/setters and a displayInfo() method

#### Student
- Extends Person and implements GradeCalculator
- Adds student-specific attributes (studentId, department)
- Manages course enrollment and grades
- Implements grade calculation methods

#### GraduateStudent
- Extends Student (forming multilevel inheritance)
- Adds graduate-specific attributes (researchArea, supervisor, thesis status)
- Overrides grade calculation methods with stricter grading policies

#### Course
- Contains course information (id, name, instructor, credits)
- Includes static variable to track total number of courses

#### GradeCalculator
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


