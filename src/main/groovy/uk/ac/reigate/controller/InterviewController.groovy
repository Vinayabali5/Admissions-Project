package uk.ac.reigate.controller

import java.util.logging.Logger

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping


@Controller
@RequestMapping('/interview')
class InterviewController {
    
    private final static Logger log = Logger.getLogger(InterviewController.class.getName());
    
    /**
     * This method is used to load the Interview entry page
     *
     * @return returns the view admissions/interview
     */
    @RequestMapping('/')
    String interview() {
        log.info("*** InterviewController.interview()")
        log.info("** Return view: admissions/search")
        return 'admissions/interview'
    }
}
