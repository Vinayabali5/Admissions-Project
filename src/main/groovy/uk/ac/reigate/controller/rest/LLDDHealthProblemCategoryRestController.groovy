package uk.ac.reigate.controller.rest

import java.util.logging.Logger

import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import uk.ac.reigate.domain.academic.Student
import uk.ac.reigate.domain.ilr.LLDDHealthProblemCategory
import uk.ac.reigate.dto.LLDDHealthProbDto
import uk.ac.reigate.repositories.academic.StudentRepository
import uk.ac.reigate.repositories.ilr.LLDDHealthProblemCategoryRepository
import uk.ac.reigate.service.admissions.CourseRequestAdmissionsService
import uk.ac.reigate.util.exception.ApplicationNotFoundException

@RestController
@RequestMapping(value='/llddHealthProb')
class LLDDHealthProblemCategoryRestController {
    
    private final static Logger log = Logger.getLogger(LLDDHealthProblemCategoryRestController.class.getName());
    
    @Autowired
    CourseRequestAdmissionsService courseRequestService
    
    @Autowired
    LLDDHealthProblemCategoryRepository llddHealthProblemCategoryRepository
    
    @Autowired
    StudentRepository studentRepository
    
    // POST /requests - Create New Request
    @RequestMapping(value='/', method=RequestMethod.POST)
    ResponseEntity<?> create(@Valid @RequestBody LLDDHealthProbDto llddHealthProb) {
        log.info("*** LLDDHealthProblemCategoryRestController.create")
        if (llddHealthProb.llddHealthProbId != null && llddHealthProb.studentId != null) { // This is a new request to be added to an application
            log.info("* Adding new LLDD Health Prob for student: " + llddHealthProb.studentId)
            Student app = studentRepository.findOne(llddHealthProb.studentId)
            if (app == null ) {
                throw new ApplicationNotFoundException(llddHealthProb.studentId)
            } else {
                LLDDHealthProblemCategory newLLDDHealthProb = llddHealthProblemCategoryRepository.findOne(llddHealthProb.llddHealthProbId)
                if (app.llddHealthProblemCategory.contains(newLLDDHealthProb)) {
                    return new ResponseEntity<List<LLDDHealthProblemCategory>>(app.llddHealthProblemCategory, HttpStatus.CONFLICT)
                }
                llddHealthProb.llddDescription = newLLDDHealthProb.shortDescription
                app.llddHealthProblemCategory.add(newLLDDHealthProb)
                studentRepository.save(app)
                return new ResponseEntity<LLDDHealthProbDto>(llddHealthProb, HttpStatus.CREATED)
            }
        }
        return new ResponseEntity<?>(HttpStatus.BAD_REQUEST)
    }
    
    
    // DELETE  /requests/{id} - Delete request with id *
    @RequestMapping(value='/{studentId}/{llddHealthProbId}', method=RequestMethod.DELETE)
    ResponseEntity<?> deleteById(@PathVariable(value="studentId") Integer studentId, @PathVariable(value="llddHealthProbId") Integer llddHealthProbId) {
        log.info("*** LLDDHealthProblemCategoryRestController.deleteById")
        if (llddHealthProbId != null && studentId != null) { // This is a new request to be added to an application
            log.info("* Deleting an LLDD Health Prob from student: " + studentId)
            Student app = studentRepository.findOne(studentId)
            if (app == null ) {
                throw new ApplicationNotFoundException(studentId)
            } else {
                LLDDHealthProblemCategory toRemove = llddHealthProblemCategoryRepository.findOne(llddHealthProbId)
                app.llddHealthProblemCategory.remove(toRemove)
                studentRepository.save(app)
                return new ResponseEntity<?>(HttpStatus.OK)
            }
        }
    }
    
    
    /* Additional REST end points */
    
    @RequestMapping(value='/getByStudent/{id}', method=RequestMethod.GET, produces='application/json')
    List<LLDDHealthProbDto> getByStudent(@PathVariable(value='id') Integer studentId) {
        log.info("*** LLDDHealthProblemCategoryRestController.getByStudent")
        Student app = studentRepository.findOne(studentId)
        List<LLDDHealthProbDto> output = new ArrayList<LLDDHealthProbDto>()
        app.llddHealthProblemCategory.each { it ->
            LLDDHealthProbDto prob = new LLDDHealthProbDto()
            prob.studentId = app.id
            prob.llddHealthProbId = it.id
            prob.llddDescription = it.shortDescription
            output.add(prob)
        }
        return output
    }
    
}
