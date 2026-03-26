package DAO;

import java.math.BigDecimal;
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
import Model.Application;


public class ApplicationDao {

    public int createApplication(int studentId, int courseId) throws SQLException {
        String sql = "INSERT INTO applications(student_id, course_id, status, merit_score, applied_at) VALUES(?,?,?,?,NOW())";
        try (Connection conn = DB.get();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.setString(3, "PENDING");
            ps.setBigDecimal(4, BigDecimal.ZERO);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) return rs.getInt(1); }
        }
        return -1;
    }

    public void updateMerit(int appId, double meritScore) throws SQLException {
        String sql = "UPDATE applications SET merit_score=? WHERE id=?";
        try (Connection conn = DB.get(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, BigDecimal.valueOf(meritScore));
            ps.setInt(2, appId);
            ps.executeUpdate();
        }
    }

    public void updateStatus(Connection conn, int appId, String status) throws SQLException {
        String sql = "UPDATE applications SET status=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, appId);
            ps.executeUpdate();
        }
    }

    public List<Application> findAllPending() throws SQLException {
        List<Application> apps = new ArrayList<>();
        String sql = "SELECT * FROM applications WHERE status='PENDING'";
        try (Connection conn = DB.get();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) apps.add(mapBasic(rs));
        }
        return apps;
    }

    
    public List<Application> findAllApproved() throws SQLException {
        List<Application> apps = new ArrayList<>();
        String sql = """
            SELECT a.id, a.student_id, a.course_id, a.status, a.merit_score, a.applied_at,
                   s.full_name AS student_name, c.name AS course_name
            FROM applications a
            JOIN students s ON a.student_id = s.id
            JOIN courses  c ON a.course_id  = c.id
            WHERE a.status = 'APPROVED'
            ORDER BY a.merit_score DESC, a.applied_at ASC
        """;
        try (Connection conn = DB.get();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Application a = new Application();
                a.setId(rs.getInt("id"));
                a.setStudentId(rs.getInt("student_id"));
                a.setCourseId(rs.getInt("course_id"));
                a.setStatus(rs.getString("status"));
                a.setMeritScore(rs.getBigDecimal("merit_score"));
                a.setAppliedAt(rs.getTimestamp("applied_at"));
                a.setStudentName(rs.getString("student_name"));
                a.setCourseName(rs.getString("course_name"));
                apps.add(a);
            }
        }
        return apps;
    }

    public List<Integer> findTopAppIdsForCourse(int courseId, int seats, double cutoff) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT id FROM applications WHERE course_id=? AND merit_score>=? ORDER BY merit_score DESC, applied_at ASC LIMIT ?";
        try (Connection conn = DB.get(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ps.setDouble(2, cutoff);
            ps.setInt(3, seats);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) ids.add(rs.getInt("id")); }
        }
        return ids;
    }

    public void approveByIds(List<Integer> appIds) throws SQLException {
        if (appIds == null || appIds.isEmpty()) return;
        String in = String.join(",", appIds.stream().map(x -> "?").toList());
        String sql = "UPDATE applications SET status='APPROVED' WHERE id IN (" + in + ")";
        try (Connection conn = DB.get(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < appIds.size(); i++) ps.setInt(i + 1, appIds.get(i));
            ps.executeUpdate();
        }
    }

    public void rejectOthers(int courseId) throws SQLException {
        String sql = "UPDATE applications SET status='REJECTED' WHERE course_id=? AND status='PENDING'";
        try (Connection conn = DB.get(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ps.executeUpdate();
        }
    }

    private Application mapBasic(ResultSet rs) throws SQLException {
        Application a = new Application();
        a.setId(rs.getInt("id"));
        a.setStudentId(rs.getInt("student_id"));
        a.setCourseId(rs.getInt("course_id"));
        a.setStatus(rs.getString("status"));
        a.setMeritScore(rs.getBigDecimal("merit_score"));
        a.setAppliedAt(rs.getTimestamp("applied_at"));
        return a;
    }
}