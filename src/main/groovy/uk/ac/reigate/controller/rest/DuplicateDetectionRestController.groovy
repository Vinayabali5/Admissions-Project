package uk.ac.reigate.controller.rest

import java.util.logging.Logger

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

import uk.ac.reigate.domain.academic.Student
import uk.ac.reigate.dto.DuplicateApplicationDto
import uk.ac.reigate.dto.DuplicateDetectionDto
import uk.ac.reigate.repositories.PersonRepository
import uk.ac.reigate.repositories.academic.StudentRepository


@RestController
@RequestMapping('/duplicate-detection')
class DuplicateDetectionRestController {
    
    private final static Logger log = Logger.getLogger(DuplicateDetectionRestController.class.getName());
    
    @Autowired
    StudentRepository studentRepository
    
    @Autowired
    PersonRepository personRepository
    
    /**
     * This method is used to detect potential duplicate applications
     *
     * @param dupp a DuplicateDetectionDto Json Packet with at minimum a Surname and DoB
     * @return ResponseEntity with the DuplicateDetectionDto and the appropriate HttpStatus code
     */
    @RequestMapping(value='/', method= RequestMethod.POST, produces='application/json')
    @ResponseBody
    ResponseEntity<DuplicateDetectionDto> detectDuplicate(@RequestBody DuplicateDetectionDto dupp) {
        log.info("*** DuplicateDetectionRestController.detectDuplicate")
        if(dupp.surname != null && dupp.dob != null) {
            log.info('Dupplicate Detection for: ' + dupp.surname + ' ' + dupp.dob)
            List<Student> duplicates = studentRepository.findByPerson_SurnameAndPerson_Dob(dupp.surname, (Date) dupp.dob)
            if(duplicates != null && duplicates.size != 0) {
                dupp.matches = new ArrayList<DuplicateApplicationDto>()
                duplicates.each { it ->
                    dupp.matches.add(new DuplicateApplicationDto(it.id, it.person))
                }
                return new ResponseEntity<DuplicateDetectionDto>(dupp, HttpStatus.OK)
            }   else {
                return new ResponseEntity<DuplicateDetectionDto>(HttpStatus.OK)
            }
        } else {
            return new ResponseEntity<DuplicateDetectionDto>(dupp, HttpStatus.BAD_REQUEST)
        }
    }
}
