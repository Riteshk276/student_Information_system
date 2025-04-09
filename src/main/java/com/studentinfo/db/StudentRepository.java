package com.studentinfo.db;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.studentinfo.GraduateStudent;
import com.studentinfo.Student;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentRepository {
    private final MongoCollection<Document> studentCollection;
    
    public StudentRepository() {
        // Get database from connection singleton
        this.studentCollection = MongoDBConnection.getInstance()
                                .getDatabase()
                                .getCollection("students");
    }
    
    /**
     * Convert a Student object to a MongoDB Document
     */
    private Document studentToDocument(Student student) {
        Document doc = new Document("_id", student.getStudentId())
                .append("name", student.getName())
                .append("age", student.getAge())
                .append("address", student.getAddress())
                .append("department", student.getDepartment())
                .append("type", "Undergraduate");
        
        // Convert course maps to a savable format
        List<Document> courses = new ArrayList<>();
        for (int i = 0; i < student.getEnrolledCourses().size(); i++) {
            String courseId = student.getEnrolledCourses().get(i).getCourseId();
            Double marks = student.getCourseMarks().get(courseId);
            
            courses.add(new Document("courseId", courseId)
                        .append("marks", marks));
        }
        doc.append("courses", courses);
        
        // If it's a graduate student, add graduate-specific fields
        if (student instanceof GraduateStudent) {
            GraduateStudent gradStudent = (GraduateStudent) student;
            doc.append("type", "Graduate")
               .append("researchArea", gradStudent.getResearchArea())
               .append("supervisor", gradStudent.getSupervisor())
               .append("thesisSubmitted", gradStudent.isThesisSubmitted());
        }
        
        return doc;
    }
    
    /**
     * Convert a MongoDB Document to a Student object
     */
    public Student documentToStudent(Document doc) {
        String name = doc.getString("name");
        int age = doc.getInteger("age");
        String address = doc.getString("address");
        String studentId = doc.getString("_id");
        String department = doc.getString("department");
        String type = doc.getString("type");
        
        Student student;
        
        if ("Graduate".equals(type)) {
            String researchArea = doc.getString("researchArea");
            String supervisor = doc.getString("supervisor");
            boolean thesisSubmitted = doc.getBoolean("thesisSubmitted", false);
            
            student = new GraduateStudent(name, age, address, studentId, department, 
                                       researchArea, supervisor);
            ((GraduateStudent) student).setThesisSubmitted(thesisSubmitted);
        } else {
            student = new Student(name, age, address, studentId, department);
        }
        
        // Get courses and marks
        List<Document> courses = (List<Document>) doc.get("courses");
        if (courses != null) {
            for (Document courseDoc : courses) {
                String courseId = courseDoc.getString("courseId");
                Double marks = courseDoc.getDouble("marks");
                
                // Note: We need to retrieve the actual course from the database
                // or pass it in from somewhere. For now, we'll just add the marks.
                student.addMarks(courseId, marks);
            }
        }
        
        return student;
    }
    
    /**
     * Save a student to the database
     */
    public boolean saveStudent(Student student) {
        try {
            Document doc = studentToDocument(student);
            
            // Check if student already exists
            Document existingStudent = studentCollection.find(
                    Filters.eq("_id", student.getStudentId())).first();
            
            if (existingStudent != null) {
                // Update existing student
                Bson filter = Filters.eq("_id", student.getStudentId());
                UpdateResult result = studentCollection.replaceOne(filter, doc);
                return result.getModifiedCount() > 0;
            } else {
                // Insert new student
                InsertOneResult result = studentCollection.insertOne(doc);
                return result.wasAcknowledged();
            }
        } catch (Exception e) {
            System.err.println("Error saving student: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Find a student by ID
     */
    public Student findStudentById(String studentId) {
        try {
            Document doc = studentCollection.find(Filters.eq("_id", studentId)).first();
            if (doc != null) {
                return documentToStudent(doc);
            }
            return null;
        } catch (Exception e) {
            System.err.println("Error finding student: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get all students
     */
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try {
            FindIterable<Document> docs = studentCollection.find();
            for (Document doc : docs) {
                students.add(documentToStudent(doc));
            }
        } catch (Exception e) {
            System.err.println("Error getting all students: " + e.getMessage());
            e.printStackTrace();
        }
        return students;
    }
    
    /**
     * Delete a student by ID
     */
    public boolean deleteStudent(String studentId) {
        try {
            DeleteResult result = studentCollection.deleteOne(Filters.eq("_id", studentId));
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            System.err.println("Error deleting student: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update student grades for a course
     */
    public boolean updateStudentGrade(String studentId, String courseId, double marks) {
        try {
            // First, get the student
            Document student = studentCollection.find(Filters.eq("_id", studentId)).first();
            if (student == null) {
                return false;
            }
            
            // Get current courses
            List<Document> courses = (List<Document>) student.get("courses");
            boolean courseFound = false;
            
            if (courses != null) {
                // Check if course exists and update
                for (Document course : courses) {
                    if (courseId.equals(course.getString("courseId"))) {
                        course.put("marks", marks);
                        courseFound = true;
                        break;
                    }
                }
            } else {
                courses = new ArrayList<>();
            }
            
            // If course not found, add it
            if (!courseFound) {
                courses.add(new Document("courseId", courseId).append("marks", marks));
            }
            
            // Update in database
            Bson filter = Filters.eq("_id", studentId);
            Bson update = Updates.set("courses", courses);
            UpdateResult result = studentCollection.updateOne(filter, update);
            
            return result.getModifiedCount() > 0;
        } catch (Exception e) {
            System.err.println("Error updating student grade: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
} 