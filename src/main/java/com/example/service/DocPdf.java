package com.example.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.example.persistence.model.Book;
import com.example.persistence.model.UserDetail;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class DocPdf {

    static void generateXML() throws XDocReportException, IOException {
        // 1) Create FieldsMetadata by setting Velocity as template engine
        FieldsMetadata fieldsMetadata = new FieldsMetadata(TemplateEngineKind.Velocity.name());
        fieldsMetadata.setTemplateEngineKind(TemplateEngineKind.Velocity.name());

        // 2) Load fields metadata from Java Class
        fieldsMetadata.load("users", UserDetail.class, true);
        // Here load is called with true because model is a list of Developer.
        fieldsMetadata.load("books", Book.class, true);

        // 3) Generate XML fields in the file "project.fields.xml".
        // Extension *.fields.xml is very important to use it with MS Macro
        // XDocReport.dotm
        // FieldsMetadata#saveXML is called with true to indent the XML.
        File xmlFieldsFile = new File("project.fields.xml");
        fieldsMetadata.saveXML(new FileOutputStream(xmlFieldsFile), true);
    }

    public static void exportPdf(String inputPath, String exportPath) throws IOException, XDocReportException {

        // 1) Load DOCX into XWPFDocument
        InputStream in = new FileInputStream(new File(inputPath));
        // IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in,
        // TemplateEngineKind.Velocity);
        XWPFDocument document = new XWPFDocument(in);

        // 2) Prepare Pdf options
        PdfOptions options = PdfOptions.create();

        // 3) Convert XWPFDocument to Pdf
        OutputStream out = new FileOutputStream(new File(exportPath));
        PdfConverter.getInstance().convert(document, out, options);
    }

    public static OutputStream exportDoc(List<Book> books, UserDetail userDetail, String path)
            throws IOException, XDocReportException {
        InputStream in = DocPdf.class
                .getResourceAsStream("/books.docx");
        IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in,
                TemplateEngineKind.Velocity);
        FieldsMetadata fieldsMetadata = report.createFieldsMetadata();
        fieldsMetadata.load("user", UserDetail.class);
        fieldsMetadata.load("books", Book.class, true);
        IContext context = report.createContext();
        context.put("books", books);
        context.put("user", userDetail);
        OutputStream out = new FileOutputStream(new File(path));
        // report.convert(context, Options.getTo("PDF"), out);
        report.process(context, out);
        return out;
    }

    public static void main(String[] args) {
        try {
            List<Book> books = new ArrayList<Book>();
            Book book = new Book();
            book.setAuthor("Tome");
            book.setTitle("Hello world");
            book.setPublished((short) 2002);
            books.add(book);
            book = new Book();
            book.setAuthor("Lily");
            book.setTitle("World");
            book.setPublished((short) 2002);
            books.add(book);
            UserDetail userDetail = new UserDetail();
            userDetail.setUsername("admin");
            userDetail.setUser_role("ADMIN");
            String path = "project_out.docx";
            DocPdf.exportDoc(books, userDetail, path);
            DocPdf.exportPdf("C:\\Users\\Admin\\Downloads\\Books_27.02.2023_15.33.25.docx", "/hello.pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
