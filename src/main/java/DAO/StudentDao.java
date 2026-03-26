package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import DAO.StudentDao;
import DAO.CourseDao;
import DAO.ApplicationDao;

import AdmissionService.AdmissionService;
import AdmissionService.MeritService;

import DBConnection.DB;
import ExportService.ExportService;

import Model.Student;
import Model.Course;


public class StudentDao {

    public int insert(Student s) throws SQLException {
        String sql = "INSERT INTO students(full_name,email,phone,category,hsc_percentage,entrance_score) VALUES(?,?,?,?,?,?)";
        try (Connection c = DB.get();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getFullName());
            ps.setString(2, s.getEmail());
            ps.setString(3, s.getPhone());
            ps.setString(4, s.getCategory());
            ps.setBigDecimal(5, s.getHscPercentage());
            ps.setBigDecimal(6, s.getEntranceScore());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) return rs.getInt(1); }
        }
        return -1;
    }

    public Student findById(int id) throws SQLException {
        String sql = "SELECT * FROM students WHERE id=?";
        try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return map(rs); }
        }
        return null;
    }

    public List<Student> findAll() throws SQLException {
        List<Student> out = new ArrayList<>();
        try (Connection c = DB.get();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM students");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(map(rs));
        }
        return out;
    }

    private Student map(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setId(rs.getInt("id"));
        s.setFullName(rs.getString("full_name"));
        s.setEmail(rs.getString("email"));
        s.setPhone(rs.getString("phone"));
        s.setCategory(rs.getString("category"));
        s.setHscPercentage(rs.getBigDecimal("hsc_percentage"));
        s.setEntranceScore(rs.getBigDecimal("entrance_score"));
        return s;
    }
}