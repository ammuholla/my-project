package DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import DAO.StudentDao;
import DAO.CourseDao;
import DAO.ApplicationDao;

import AdmissionService.AdmissionService;
import AdmissionService.MeritService;

import ExportService.ExportService;

import Model.Student;
import Model.Course;

public class DB {
    //private static final String URL  = "jdbc:mysql://localhost:3306/coep_admission?useSSL=false&serverTimezone=UTC";
    private static final String URL  = "jdbc:mysql://localhost:3306/coep_admission?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "Ammu@013";

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}