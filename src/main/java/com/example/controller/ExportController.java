package com.example.controller;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.persistence.model.UserDetail;
import com.example.persistence.model.Wrapper;
import com.example.service.import_export.DocPdf;
import com.example.service.import_export.ExportToExcel;

@Controller
public class ExportController {
    
    static Logger logger = Logger.getLogger(ExportController.class.getName());
        @RequestMapping(value = "/export", method = RequestMethod.POST)
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
        @RequestMapping(value = "/exportDoc", method = RequestMethod.POST)
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

    @RequestMapping(value = "/exportPdf", method = RequestMethod.POST)
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