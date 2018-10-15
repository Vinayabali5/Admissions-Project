package uk.ac.reigate.controller.rest

import java.util.logging.Logger

import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import uk.ac.reigate.domain.academic.School
import uk.ac.reigate.repositories.SchoolRepository

@RestController
@RequestMapping(value='/schools')
class SchoolRestController {
    private final static Logger log = Logger.getLogger(SchoolRestController.class.getName());
    
    @Autowired
    SchoolRepository schoolRepository
    
    @Cacheable(value="schools")
    @RequestMapping(method=RequestMethod.GET)
    List<School> getAll() {
        return schoolRepository.findAll()
    }
    
    @RequestMapping(method=RequestMethod.PUT, value='/{id}')
    School updateSchool(@PathVariable(value="id") Integer id, @RequestBody @Valid School school) {
        if (id != null && id == school.id) {
            School updatedSchool = schooolRepository.findOne(id)
            updatedSchool.name = school.name
            updatedSchool.type = school.type
            updatedSchool.priority = school.priority
            
            updatedSchool.urn = school.urn
            
            updatedSchool.line1 = school.line1
            updatedSchool.line2 = school.line2
            updatedSchool.line3 = school.line3
            updatedSchool.postcode = school.postcode
            
            schoolRepository.save(updateSchool)
        } else {
            throw IdMismatchException("The ID: $id does not match the ID of the school being updated.")
        }
    }
}
