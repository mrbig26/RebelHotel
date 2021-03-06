// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.unlv.cs.rebelhotel.web;

import edu.unlv.cs.rebelhotel.file.FileUpload;
import edu.unlv.cs.rebelhotel.file.enums.FileUploadStatus;
import java.io.UnsupportedEncodingException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.Arrays;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect FileUploadController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST)
    public String FileUploadController.create(@Valid FileUpload fileUpload, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("fileUpload", fileUpload);
            addDateTimeFormatPatterns(model);
            return "fileuploads/create";
        }
        fileUpload.persist();
        return "redirect:/fileuploads/" + encodeUrlPathSegment(fileUpload.getId().toString(), request);
    }
    
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String FileUploadController.createForm(Model model) {
        model.addAttribute("fileUpload", new FileUpload());
        addDateTimeFormatPatterns(model);
        return "fileuploads/create";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String FileUploadController.show(@PathVariable("id") Long id, Model model) {
        addDateTimeFormatPatterns(model);
        model.addAttribute("fileupload", FileUpload.findFileUpload(id));
        model.addAttribute("itemId", id);
        return "fileuploads/show";
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String FileUploadController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            model.addAttribute("fileuploads", FileUpload.findFileUploadEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) FileUpload.countFileUploads() / sizeNo;
            model.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            model.addAttribute("fileuploads", FileUpload.findAllFileUploads());
        }
        addDateTimeFormatPatterns(model);
        return "fileuploads/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public String FileUploadController.update(@Valid FileUpload fileUpload, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("fileUpload", fileUpload);
            addDateTimeFormatPatterns(model);
            return "fileuploads/update";
        }
        fileUpload.merge();
        return "redirect:/fileuploads/" + encodeUrlPathSegment(fileUpload.getId().toString(), request);
    }
    
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String FileUploadController.updateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("fileUpload", FileUpload.findFileUpload(id));
        addDateTimeFormatPatterns(model);
        return "fileuploads/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String FileUploadController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
        FileUpload.findFileUpload(id).remove();
        model.addAttribute("page", (page == null) ? "1" : page.toString());
        model.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/fileuploads?page=" + ((page == null) ? "1" : page.toString()) + "&size=" + ((size == null) ? "10" : size.toString());
    }
    
    @ModelAttribute("fileuploadstatuses")
    public Collection<FileUploadStatus> FileUploadController.populateFileUploadStatuses() {
        return Arrays.asList(FileUploadStatus.class.getEnumConstants());
    }
    
    void FileUploadController.addDateTimeFormatPatterns(Model model) {
        model.addAttribute("fileUpload_startofexecution_date_format", DateTimeFormat.patternForStyle("S-", LocaleContextHolder.getLocale()));
        model.addAttribute("fileUpload_endofexecution_date_format", DateTimeFormat.patternForStyle("S-", LocaleContextHolder.getLocale()));
    }
    
    String FileUploadController.encodeUrlPathSegment(String pathSegment, HttpServletRequest request) {
        String enc = request.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        }
        catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}
