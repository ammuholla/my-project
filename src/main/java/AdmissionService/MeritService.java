package AdmissionService;


import DAO.StudentDao;
import DAO.CourseDao;
import DAO.ApplicationDao;

import AdmissionService.AdmissionService;
import AdmissionService.MeritService;

import ExportService.ExportService;

import Model.Student;
import Model.Course;
import Model.Application;

public class MeritService {
    // Simple merit formula: 60% HSC + 40% normalized entrance (out of 200)
    public double compute(Student s) {
        double hsc = s.getHscPercentage().doubleValue();     
        double ent = s.getEntranceScore().doubleValue();     
        double entPct = (ent / 200.0) * 100.0;
        return (0.60 * hsc) + (0.40 * entPct);
    }
}