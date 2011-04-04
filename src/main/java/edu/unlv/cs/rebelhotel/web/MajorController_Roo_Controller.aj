// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.unlv.cs.rebelhotel.web;

import edu.unlv.cs.rebelhotel.domain.Major;
import edu.unlv.cs.rebelhotel.domain.Term;
import edu.unlv.cs.rebelhotel.domain.WorkRequirement;
import edu.unlv.cs.rebelhotel.domain.enums.Degree;
import java.io.UnsupportedEncodingException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.Arrays;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect MajorController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST)
    public String MajorController.create(@Valid Major major, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("major", major);
            return "majors/create";
        }
        uiModel.asMap().clear();
        major.persist();
        return "redirect:/majors/" + encodeUrlPathSegment(major.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String MajorController.createForm(Model uiModel) {
        uiModel.addAttribute("major", new Major());
        return "majors/create";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String MajorController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("major", Major.findMajor(id));
        uiModel.addAttribute("itemId", id);
        return "majors/show";
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String MajorController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            uiModel.addAttribute("majors", Major.findMajorEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) Major.countMajors() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("majors", Major.findAllMajors());
        }
        return "majors/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public String MajorController.update(@Valid Major major, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("major", major);
            return "majors/update";
        }
        uiModel.asMap().clear();
        major.merge();
        return "redirect:/majors/" + encodeUrlPathSegment(major.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String MajorController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("major", Major.findMajor(id));
        return "majors/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String MajorController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Major.findMajor(id).remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/majors";
    }
    
    @ModelAttribute("majors")
    public Collection<Major> MajorController.populateMajors() {
        return Major.findAllMajors();
    }
    
    @ModelAttribute("terms")
    public java.util.Collection<Term> MajorController.populateTerms() {
        return Term.findAllTerms();
    }
    
    @ModelAttribute("workrequirements")
    public java.util.Collection<WorkRequirement> MajorController.populateWorkRequirements() {
        return WorkRequirement.findAllWorkRequirements();
    }
    
    @ModelAttribute("degrees")
    public java.util.Collection<Degree> MajorController.populateDegrees() {
        return Arrays.asList(Degree.class.getEnumConstants());
    }
    
    String MajorController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
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
