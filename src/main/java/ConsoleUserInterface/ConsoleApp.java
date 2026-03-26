package ConsoleUserInterface;

import java.math.BigDecimal;
import java.util.Scanner;
import DAO.StudentDao;
import DAO.CourseDao;
import DAO.ApplicationDao;

import AdmissionService.AdmissionService;
import AdmissionService.MeritService;

import ExportService.ExportService;

import Model.Student;
import Model.Course;
import Model.Application;

public class ConsoleApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        StudentDao studentDao = new StudentDao();
        CourseDao courseDao = new CourseDao();
        ApplicationDao appDao = new ApplicationDao();
        MeritService meritService = new MeritService();
        AdmissionService admissionService = new AdmissionService(studentDao, courseDao, appDao, meritService);
        ExportService exportService = new ExportService(appDao);

        while (true) {
            System.out.println("""
                    1) Register Student
                    2) Add Course
                    3) Apply to Course
                    4) Compute Merit for ALL Applications
                    5) Run Allocation for Course
                    6) Export Approved (CSV/PDF/DOCX)
                    7) Edit Course
                    0) Exit
                    """);
            System.out.print("Choice: ");
            int ch = Integer.parseInt(sc.nextLine().trim());
            if (ch == 0) break;

            try {
                switch (ch) {
                    case 1 -> {
                        Student s = new Student();
                        System.out.print("Full name: "); s.setFullName(sc.nextLine());
                        System.out.print("Email: "); s.setEmail(sc.nextLine());
                        System.out.print("Phone: "); s.setPhone(sc.nextLine());
                        System.out.print("Category [GEN/OBC/SC/ST/EWS]: "); s.setCategory(sc.nextLine().trim());
                        System.out.print("HSC %: "); s.setHscPercentage(new BigDecimal(sc.nextLine()));
                        System.out.print("Entrance score (0-200): "); s.setEntranceScore(new BigDecimal(sc.nextLine()));
                        int id = studentDao.insert(s);
                        System.out.println(" Student registered with ID: " + id);
                    }
                    case 2 -> {
                        Course c = new Course();
                        System.out.print("Course name: "); c.setName(sc.nextLine());
                        System.out.print("Seats: "); c.setSeats(Integer.parseInt(sc.nextLine()));
                        System.out.print("Cutoff (merit): "); c.setCutoff(new BigDecimal(sc.nextLine()));
                        int id = courseDao.insert(c);
                        System.out.println(" Course created with ID: " + id);
                    }
                    case 3 -> {
                        System.out.print("Student ID: "); int sid = Integer.parseInt(sc.nextLine());
                        System.out.print("Course ID: ");  int cid = Integer.parseInt(sc.nextLine());
                        int appId = admissionService.apply(sid, cid);
                        System.out.println(" Application created with ID: " + appId);
                    }
                    case 4 -> {
                        admissionService.computeMeritForAllPending();
                        System.out.println(" Merit computed for pending applications.");
                    }
                    case 5 -> {
                        System.out.print("Course ID: ");
                        int cid = Integer.parseInt(sc.nextLine());
                        admissionService.runAllocationForCourse(cid);
                        System.out.println(" Allocation completed for course " + cid);
                    }
                    case 6 -> {
                        exportService.exportApprovedCSV("admission_list.csv");
                        exportService.exportApprovedPDF("admission_list.pdf");
                        exportService.exportApprovedDOCX("admission_list.docx");
                        System.out.println(" Export completed!");
                    }
                    case 7 -> {
                        Course c = new Course();

                        System.out.print("Enter Course ID to edit: ");
                        int id = Integer.parseInt(sc.nextLine());

                        System.out.print("New Course Name: ");
                        c.setName(sc.nextLine());

                        System.out.print("New Seats: ");
                        c.setSeats(Integer.parseInt(sc.nextLine()));

                        System.out.print("New Cutoff: ");
                        c.setCutoff(new BigDecimal(sc.nextLine()));

                        c.setId(id);

                        courseDao.updateCourse(c);
                    }
                    default -> System.out.println(" Invalid choice. Try again.");
                }
            } catch (Exception e) {
                System.out.println(" Error: " + e.getMessage());
                e.printStackTrace();
            }
        }

        sc.close();
        System.out.println(" Exiting the Admission Management System.");
    }
}