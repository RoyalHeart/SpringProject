package com.example.service.import_export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.util.ZipSecureFile;

import com.example.persistence.model.Book;
import com.example.persistence.model.UserDetail;

import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.ClassPathImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
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

    public static void exportPdf(List<Book> books, UserDetail userDetail, String exportPath)
            throws IOException, XDocReportException {
                ZipSecureFile.setMinInflateRatio(0.001); // increase max file size
        InputStream in = DocPdf.class
                .getResourceAsStream("/velocityBookTemplate.docx");
        IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in,
                TemplateEngineKind.Velocity);
        FieldsMetadata fieldsMetadata = report.createFieldsMetadata();
        fieldsMetadata.load("user", UserDetail.class);
        fieldsMetadata.load("books", Book.class, true);
        IImageProvider logo = new ClassPathImageProvider(DocPdf.class, "/static/images/logo.png");
        fieldsMetadata.addFieldAsImage("logo");
        fieldsMetadata.addFieldAsImage("logo");
        fieldsMetadata.addFieldAsList("books.Author");
        fieldsMetadata.addFieldAsList("books.Id");
        fieldsMetadata.addFieldAsList("books.Title");
        fieldsMetadata.addFieldAsList("books.Published");
        IContext context = report.createContext();
        context.put("books", books);
        context.put("user", userDetail);
        context.put("logo", logo);
        context.put("exportDate", new Date(new java.util.Date().getTime()));
        OutputStream out = new FileOutputStream(new File(exportPath));
        PdfOptions pdfOptions = PdfOptions.create();
        Options options = Options.getTo(ConverterTypeTo.PDF).via(ConverterTypeVia.XWPF).subOptions(pdfOptions);
        report.convert(context, options, out);
    }

    public static void exportDoc(List<Book> books, UserDetail userDetail, String path)
            throws IOException, XDocReportException {
        InputStream in = DocPdf.class
                .getResourceAsStream("/velocityBookTemplate.docx");
        IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in,
                TemplateEngineKind.Velocity);
        FieldsMetadata fieldsMetadata = report.createFieldsMetadata();
        fieldsMetadata.load("user", UserDetail.class);
        fieldsMetadata.load("books", Book.class, true);
        IImageProvider logo = new ClassPathImageProvider(DocPdf.class, "/static/images/logo.png");
        fieldsMetadata.addFieldAsImage("logo");
        fieldsMetadata.addFieldAsList("books.Author");
        fieldsMetadata.addFieldAsList("books.Id");
        fieldsMetadata.addFieldAsList("books.Title");
        fieldsMetadata.addFieldAsList("books.Published");
        // report.setFieldsMetadata(fieldsMetadata);
        IContext context = report.createContext();
        context.put("books", books);
        context.put("user", userDetail);
        context.put("logo", logo);
        context.put("exportDate", new Date(new java.util.Date().getTime()));
        OutputStream out = new FileOutputStream(new File(path));
        report.process(context, out);
    }

}
