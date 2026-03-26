package ExportService;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.List;

import Model.Application;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import com.opencsv.CSVWriter;

import DAO.StudentDao;
import DAO.CourseDao;
import DAO.ApplicationDao;

import AdmissionService.AdmissionService;
import AdmissionService.MeritService;

import ExportService.ExportService;

import Model.Student;
import Model.Course;



public class ExportService {

    private final ApplicationDao appDao;

    public ExportService(ApplicationDao appDao) {
        this.appDao = appDao;
    }

    
    public void exportApprovedCSV(String filePath) throws Exception {
        List<Application> approved = appDao.findAllApproved();
        if (approved.isEmpty()) {
            System.out.println(" No approved applications found. CSV not created.");
            return;
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            String[] headers = {"ID", "Student", "Course", "Merit", "Status", "Applied At"};
            writer.writeNext(headers);

            for (Application a : approved) {
                String[] row = {
                        String.valueOf(a.getId()),
                        nvl(a.getStudentName()),
                        nvl(a.getCourseName()),
                        bd(a.getMeritScore()),
                        nvl(a.getStatus()),
                        a.getAppliedAt() != null ? a.getAppliedAt().toString() : "-"
                };
                writer.writeNext(row);
            }
        }

        System.out.println("✅ CSV exported: " + filePath);
    }

    
    public void exportApprovedPDF(String filePath) throws Exception {
        List<Application> approved = appDao.findAllApproved();
        if (approved.isEmpty()) {
            System.out.println("⚠ No approved applications found. PDF not created.");
            return;
        }

        
        System.out.println("Approved Applications Count = " + approved.size());

        try (PdfWriter writer = new PdfWriter(filePath);
             PdfDocument pdf = new PdfDocument(writer);
             Document doc = new Document(pdf)) {

            
            doc.add(new Paragraph("Approved Admission List")
                    .setBold()
                    .setFontSize(16)
                    .setMarginBottom(10));

            // Table
            float[] columnWidths = {1, 3, 3, 2, 2, 3};
            Table table = new Table(UnitValue.createPercentArray(columnWidths))
                    .useAllAvailableWidth();

            
            String[] headers = {"ID", "Student", "Course", "Merit", "Status", "Applied At"};
            for (String h : headers) {
                table.addHeaderCell(new Cell().add(new Paragraph(h).setBold()));
            }

            
            for (Application a : approved) {
                table.addCell(String.valueOf(a.getId()));
                table.addCell(nvl(a.getStudentName()));
                table.addCell(nvl(a.getCourseName()));
                table.addCell(bd(a.getMeritScore()));
                table.addCell(nvl(a.getStatus()));
                table.addCell(a.getAppliedAt() != null ? a.getAppliedAt().toString() : "-");
            }

            doc.add(table);
            doc.flush();
        }

        System.out.println(" PDF exported with data: " + filePath);
    }

    // -------------------- DOCX EXPORT (Apache POI) --------------------
    public void exportApprovedDOCX(String filePath) throws Exception {
        List<Application> approved = appDao.findAllApproved();
        if (approved.isEmpty()) {
            System.out.println(" No approved applications found. DOCX not created.");
            return;
        }

        try (XWPFDocument doc = new XWPFDocument();
             FileOutputStream out = new FileOutputStream(filePath)) {

            // Title
            XWPFParagraph title = doc.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun run = title.createRun();
            run.setBold(true);
            run.setFontSize(16);
            run.setText("Approved Admission List");

            // Table
            XWPFTable table = doc.createTable();
            String[] headers = {"ID", "Student", "Course", "Merit", "Status", "Applied At"};
            XWPFTableRow headerRow = table.getRow(0);
            for (int i = 0; i < headers.length; i++) {
                if (i == 0) {
                    headerRow.getCell(0).setText(headers[i]);
                } else {
                    headerRow.addNewTableCell().setText(headers[i]);
                }
            }

            
            for (Application a : approved) {
                XWPFTableRow row = table.createRow();
                row.getCell(0).setText(String.valueOf(a.getId()));
                row.getCell(1).setText(nvl(a.getStudentName()));
                row.getCell(2).setText(nvl(a.getCourseName()));
                row.getCell(3).setText(bd(a.getMeritScore()));
                row.getCell(4).setText(nvl(a.getStatus()));
                row.getCell(5).setText(a.getAppliedAt() != null ? a.getAppliedAt().toString() : "-");
            }

            doc.write(out);
        }

        System.out.println(" DOCX exported: " + filePath);
    }

    
    private String nvl(String s) {
        return s != null ? s : "";
    }

    private String bd(Number n) {
        return n != null ? String.valueOf(n) : "";
    }
}