package AdmissionService;

import java.sql.Connection;
import java.sql.SQLException;
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




public class AdmissionService {
    private final StudentDao studentDao;
    private final CourseDao courseDao;
    private final ApplicationDao appDao;
    private final MeritService meritService;

    public AdmissionService(StudentDao s, CourseDao c, ApplicationDao a, MeritService m) {
        this.studentDao = s; this.courseDao = c; this.appDao = a; this.meritService = m;
    }

    
    public int apply(int studentId, int courseId) throws SQLException {
        return appDao.createApplication(studentId, courseId);
    }

    
    public void computeMeritForAllPending() throws SQLException {
        var pending = appDao.findAllPending();
        for (var a : pending) {
            Student s = studentDao.findById(a.getStudentId());
            if (s != null) {
                double merit = meritService.compute(s);
                appDao.updateMerit(a.getId(), merit);
            }
        }
    }

    
    public void runAllocationForCourse(int courseId) throws SQLException {
        Course c = courseDao.findById(courseId);
        if (c == null) throw new SQLException("Course not found: " + courseId);

        double cutoff = c.getCutoff().doubleValue();
        int seats = c.getSeats();
        List<Integer> winners = appDao.findTopAppIdsForCourse(courseId, seats, cutoff);

        try (Connection conn = DB.get()) {
            conn.setAutoCommit(false);
            try {
                appDao.approveByIds(winners);
                appDao.rejectOthers(courseId);
                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}