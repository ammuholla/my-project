package Model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import DAO.StudentDao;
import DAO.CourseDao;
import DAO.ApplicationDao;

import AdmissionService.AdmissionService;
import AdmissionService.MeritService;

import ExportService.ExportService;

import Model.Student;
import Model.Course;

public class Application {
    private int id;
    private int studentId;
    private int courseId;
    private String status; 
    private BigDecimal meritScore;
    private Timestamp appliedAt;

    
    private String studentName;
    private String courseName;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BigDecimal getMeritScore() { return meritScore; }
    public void setMeritScore(BigDecimal meritScore) { this.meritScore = meritScore; }
    public Timestamp getAppliedAt() { return appliedAt; }
    public void setAppliedAt(Timestamp appliedAt) { this.appliedAt = appliedAt; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
}