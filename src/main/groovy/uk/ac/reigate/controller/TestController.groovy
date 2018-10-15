package uk.ac.reigate.controller

import java.util.logging.Logger

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

import uk.ac.reigate.domain.academic.AcademicYear
import uk.ac.reigate.service.admissions.AcademicYearService
import uk.ac.reigate.service.admissions.CourseRequestAdmissionsService
import uk.ac.reigate.service.admissions.StudentAdmissionsService

@Controller
@RequestMapping('/test')
class TestController {
    
    private final static Logger log = Logger.getLogger(ApplicationController.class.getName());
    
    @Autowired
    StudentAdmissionsService studentService
    
    @Autowired
    AcademicYearService academicYearService
    
    @Autowired
    CourseRequestAdmissionsService courseRequestService
    
    @ResponseBody
    @RequestMapping('/year')
    String testYear() {
        log.info("*** TestController.testYear()")
        AcademicYear current = academicYearService.getCurrent()
        AcademicYear next = academicYearService.getNext()
        log.info("** Return string with Current and Next Years")
        return "Current Year: $current - Next Year: $next"
    }
    
    @ResponseBody
    @RequestMapping('/appRef')
    String testAppRef() {
        log.info("*** TestController.testAppRef()")
        String ref = studentService.generateApplicationReference()
        return "Next Ref: $ref"
    }
}
