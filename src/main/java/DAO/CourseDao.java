package DAO;

import java.sql.*;
import DAO.StudentDao;
import DAO.CourseDao;
import DAO.ApplicationDao;

import AdmissionService.AdmissionService;
import AdmissionService.MeritService;

import DBConnection.DB;
import ExportService.ExportService;

import Model.Student;
import Model.Course;
import Model.Application;

public class CourseDao {

    public int insert(Course c) throws SQLException {
        String sql = "INSERT INTO courses(name,seats,cutoff) VALUES(?,?,?)";
        try (Connection conn = DB.get();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getName());
            ps.setInt(2, c.getSeats());
            ps.setBigDecimal(3, c.getCutoff());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) return rs.getInt(1); }
        }
        return -1;
    }

    public Course findById(int id) throws SQLException {
        String sql = "SELECT * FROM courses WHERE id=?";
        try (Connection conn = DB.get(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return map(rs); }
        }
        return null;
    }

    public void updateCourse(Course c) throws SQLException {
        String sql = "UPDATE courses SET name=?, seats=?, cutoff=? WHERE id=?";
        try (Connection conn = DB.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getName());
            ps.setInt(2, c.getSeats());
            ps.setBigDecimal(3, c.getCutoff());
            ps.setInt(4, c.getId());

            int rows = ps.executeUpdate();

            if (rows > 0)
                System.out.println("Course updated successfully!");
            else
                System.out.println("Course not found!");
        }
    }

    private Course map(ResultSet rs) throws SQLException {
        Course c = new Course();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        c.setSeats(rs.getInt("seats"));
        c.setCutoff(rs.getBigDecimal("cutoff"));
        return c;
    }
}