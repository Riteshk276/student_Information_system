package com.studentinfo.db;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.studentinfo.Course;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class CourseRepository {
    private final MongoCollection<Document> courseCollection;
    
    public CourseRepository() {
        // Get database from connection singleton
        this.courseCollection = MongoDBConnection.getInstance()
                               .getDatabase()
                               .getCollection("courses");
    }
    
    /**
     * Convert a Course object to a MongoDB Document
     */
    private Document courseToDocument(Course course) {
        Document doc = new Document("_id", course.getCourseId())
                .append("courseName", course.getCourseName())
                .append("instructorName", course.getInstructorName())
                .append("credits", course.getCredits());
        
        return doc;
    }
    
    /**
     * Convert a MongoDB Document to a Course object
     */
    public Course documentToCourse(Document doc) {
        String courseId = doc.getString("_id");
        String courseName = doc.getString("courseName");
        String instructorName = doc.getString("instructorName");
        int credits = doc.getInteger("credits");
        
        return new Course(courseId, courseName, instructorName, credits);
    }
    
    /**
     * Save a course to the database
     */
    public boolean saveCourse(Course course) {
        try {
            Document doc = courseToDocument(course);
            
            // Check if course already exists
            Document existingCourse = courseCollection.find(
                    Filters.eq("_id", course.getCourseId())).first();
            
            if (existingCourse != null) {
                // Update existing course
                Bson filter = Filters.eq("_id", course.getCourseId());
                UpdateResult result = courseCollection.replaceOne(filter, doc);
                return result.getModifiedCount() > 0;
            } else {
                // Insert new course
                InsertOneResult result = courseCollection.insertOne(doc);
                return result.wasAcknowledged();
            }
        } catch (Exception e) {
            System.err.println("Error saving course: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Find a course by ID
     */
    public Course findCourseById(String courseId) {
        try {
            Document doc = courseCollection.find(Filters.eq("_id", courseId)).first();
            if (doc != null) {
                return documentToCourse(doc);
            }
            return null;
        } catch (Exception e) {
            System.err.println("Error finding course: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get all courses
     */
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        try {
            FindIterable<Document> docs = courseCollection.find();
            for (Document doc : docs) {
                courses.add(documentToCourse(doc));
            }
        } catch (Exception e) {
            System.err.println("Error getting all courses: " + e.getMessage());
            e.printStackTrace();
        }
        return courses;
    }
    
    /**
     * Delete a course by ID
     */
    public boolean deleteCourse(String courseId) {
        try {
            DeleteResult result = courseCollection.deleteOne(Filters.eq("_id", courseId));
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            System.err.println("Error deleting course: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
} 