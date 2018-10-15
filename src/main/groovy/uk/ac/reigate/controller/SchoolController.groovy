package uk.ac.reigate.controller

import java.util.logging.Logger

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

import uk.ac.reigate.domain.academic.School
import uk.ac.reigate.repositories.SchoolRepository


@Controller
@RequestMapping('/school')
class SchoolController {
    
    private final static Logger log = Logger.getLogger(SchoolController.class.getName());
    
    @Autowired
    SchoolRepository schoolRepository
    
    @RequestMapping(value='/list', method=RequestMethod.GET)
    String listSchools() {
        log.info('* SchoolController - listSchools')
        return 'data/school/list'
    }
    
    @RequestMapping(value='/search', method = RequestMethod.GET)
    List<School> searchSchool(@RequestParam String schoolName){
        log.info('* SchoolController - searchSchools')
        return schoolRepository.findByNameContainingIgnoreCase(schoolName);
    }
}
