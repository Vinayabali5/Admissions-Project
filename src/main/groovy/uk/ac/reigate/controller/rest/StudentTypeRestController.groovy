package uk.ac.reigate.controller.rest

import java.util.logging.Logger

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import uk.ac.reigate.domain.lookup.StudentType
import uk.ac.reigate.repositories.lookup.StudentTypeRepository

@RestController
@RequestMapping(value='/studentTypes')
class StudentTypeRestController {
    
    private final static Logger log = Logger.getLogger(StudentTypeRestController.class.getName());
    
    @Autowired
    StudentTypeRepository studentTypeRepository
    
    @Cacheable(value="studentTypes")
    @RequestMapping(method=RequestMethod.GET)
    List<StudentType> getAll() {
        return studentTypeRepository.findAll()
    }
}
