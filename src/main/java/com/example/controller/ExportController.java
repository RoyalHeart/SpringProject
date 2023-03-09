package com.example.controller;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.persistence.model.UserDetail;
import com.example.persistence.model.Wrapper;
import com.example.service.export_import.DocPdf;
import com.example.service.export_import.ExportToExcel;

@Controller
public class ExportController {

    // max form input can only receive 255 Object, increase to 1000
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(1000);
        // SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        // dateFormat.setLenient(false);
        // binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat,
        // true));
    }

    static Logger logger = Logger.getLogger(ExportController.class.getName());

    @PostMapping("/export")
    public String exportBook(@ModelAttribute(name = "wrapper") Wrapper wrapper,
            Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String referer = request.getHeader("Referer");
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy_HH.mm.ss");
            Date date = new Date(new java.util.Date().getTime());
            String currentTime = formatter.format(date);
            String home = System.getProperty("user.home");
            String filename = "Books_" + currentTime + ".xlsx";
            String exportPath = home + "/Downloads/" + filename;
            ExportToExcel.writeExcel(wrapper.getBooks(), exportPath);
            redirectAttributes.addFlashAttribute("exportExcelSuccessfully", "Exported at: " + exportPath);
        } catch (Exception e) {
            logger.log(Level.SEVERE, ">>> Export error: " + e.getMessage());
        }
        return "redirect:" + referer;
    }

    @PostMapping("/exportDoc")
    public String exportDoc(@ModelAttribute(name = "wrapper") Wrapper wrapper,
            Model model, HttpServletRequest request, RedirectAttributes redirectAttributes, Authentication auth) {
        String referer = request.getHeader("Referer");
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy_HH.mm.ss");
            Date date = new Date(new java.util.Date().getTime());
            String currentTime = formatter.format(date);
            String home = System.getProperty("user.home");
            String filename = "Books_" + currentTime + ".docx";
            String exportPath = home + "/Downloads/" + filename;
            redirectAttributes.addFlashAttribute("exportDocSuccessfully", "Exported at: " + exportPath);
            UserDetail user = new UserDetail();
            logger.log(Level.INFO, ">>> Username:" + auth.getName());
            logger.log(Level.INFO, ">>> Role:" + auth.getAuthorities().iterator().next().getAuthority());
            user.setUsername(auth.getName());
            user.setUser_role(auth.getAuthorities().iterator().next().getAuthority());
            DocPdf.exportDoc(wrapper.getBooks(), user, exportPath);
        } catch (Exception e) {
            logger.log(Level.SEVERE, ">>> Export error: " + e.getMessage());
        }
        return "redirect:" + referer;
    }

    @PostMapping("/exportPdf")
    public String exportPdf(@ModelAttribute(name = "wrapper") Wrapper wrapper,
            Model model, HttpServletRequest request, RedirectAttributes redirectAttributes, Authentication auth) {
        String referer = request.getHeader("Referer");
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy_HH.mm.ss");
            Date date = new Date(new java.util.Date().getTime());
            String currentTime = formatter.format(date);
            String home = System.getProperty("user.home");
            String filenamePdf = "Books_" + currentTime + ".pdf";
            String exportPath = home + "/Downloads/" + filenamePdf;
            UserDetail user = new UserDetail();
            user.setUsername(auth.getName());
            user.setUser_role(auth.getAuthorities().iterator().next().getAuthority());
            DocPdf.exportPdf(wrapper.getBooks(), user, exportPath);
            redirectAttributes.addFlashAttribute("exportPdfSuccessfully", "Exported at: " + exportPath);
        } catch (Exception e) {
            logger.log(Level.SEVERE, ">>> Export error: " + e.getMessage());
        }
        return "redirect:" + referer;
    }

}
