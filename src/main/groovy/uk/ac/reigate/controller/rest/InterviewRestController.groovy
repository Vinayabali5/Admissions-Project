package uk.ac.reigate.controller.rest

import java.util.logging.Logger

import javax.servlet.http.HttpServletRequest

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

import uk.ac.reigate.domain.academic.AcademicYear
import uk.ac.reigate.domain.academic.Student
import uk.ac.reigate.domain.academic.StudentYear
import uk.ac.reigate.dto.ErrorMessageDto
import uk.ac.reigate.dto.InterviewDto
import uk.ac.reigate.repositories.PersonRepository
import uk.ac.reigate.repositories.StaffRepository
import uk.ac.reigate.repositories.academic.StudentRepository
import uk.ac.reigate.repositories.academic.StudentYearRepository
import uk.ac.reigate.repositories.admissions.OfferTypeRepository
import uk.ac.reigate.repositories.lookup.StudentTypeRepository
import uk.ac.reigate.service.admissions.AcademicYearService
import uk.ac.reigate.util.exception.IdMismatchException


@RestController
@RequestMapping(value='/interviews')
class InterviewRestController {
    
    private final static Logger log = Logger.getLogger(InterviewRestController.class.getName());
    
    @Autowired
    AcademicYearService academicYearService
    
    @Autowired
    StudentRepository studentRepository
    
    @Autowired
    StudentYearRepository studentYearRepository
    
    @Autowired
    StaffRepository staffRepository
    
    @Autowired
    PersonRepository personRepository
    
    @Autowired
    OfferTypeRepository offerTypeRepository
    
    @Autowired
    StudentTypeRepository studentTypeRepository
    
    /* Core REST end points */
    
    // PUT /interviews/{id} - Update Interview with id
    @RequestMapping(value='/{id}', method=RequestMethod.PUT, consumes='application/json', produces='application/json')
    InterviewDto update(@PathVariable(value="id") Integer studentId, @RequestBody InterviewDto interview, BindingResult bindingResult) {
        log.info("** ApplicationtRestController.update")
        if (bindingResult.hasErrors()) {
            throw new RuntimeException(bindingResult.allErrors.toString())
        }
        if(studentId == null && interview.studentId == null) {
            throw new RuntimeException('The Application ID is missing from this request.')
        }
        if (studentId!= interview.studentId) {
            throw new IdMismatchException('The id supplied in the body does not match the ID of the record you are trying to update.')
        }
        
        AcademicYear year = academicYearService.getNextAcademicYear()
        
        Student applicationToUpdate = studentRepository.findOne(interview.studentId)
        
        StudentYear studentYearToUpdate = studentYearRepository.findByStudent_IdAndYear_Id(interview.studentId, year.id)
        log.info("** InterviewRestController.update--" + studentId)
        
        applicationToUpdate.interviewer = interview.interviewerId != null ? staffRepository.findOne(interview.interviewerId) : applicationToUpdate.interviewer
        applicationToUpdate.offerType = interview.offerTypeId != null ? offerTypeRepository.findOne(interview.offerTypeId) : applicationToUpdate.offerType
        studentYearToUpdate.studentType = interview.studentTypeId != null ? studentTypeRepository.findOne(interview.studentTypeId) : studentYearToUpdate.studentType
        applicationToUpdate.interviewDate = interview.interviewDate != null ? interview.interviewDate : applicationToUpdate.interviewDate
        try {
            Student savedApplication = studentRepository.save(applicationToUpdate)
            StudentYear savedStudentYear = studentYearRepository.save(studentYearToUpdate)
            return new InterviewDto(savedApplication, savedStudentYear)
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong")
        }
        
    }
    
    /* Additional REST end points */
    
    @RequestMapping(value='/getByStudent/{id}', method=RequestMethod.GET, produces='application/json')
    InterviewDto getByStudent(@PathVariable(value='id') Integer id) {
        log.info("*** InterviewRestController.getByStudent")
        Student student = studentRepository.findOne(id)
        AcademicYear nextYear = academicYearService.getNextAcademicYear()
        StudentYear studentYear = studentYearRepository.findByStudentAndYear(student, nextYear)
        if (student != null && studentYear != null) {
            // Create blank interview DTO if no interview data exists
            return new InterviewDto(student, studentYear)
        } else {
            throw new MissingResourceException('Cannot find an application with id: ' + id)
        }
    }
    
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    ErrorMessageDto handleJsonException(final HttpMessageNotReadableException ex, final HttpServletRequest request) {
        if (ex.getCause() instanceof JsonParseException) {
            return new ErrorMessageDto(ex.getMessage())
        }
        if (ex.getCause() instanceof JsonMappingException) {
            return new ErrorMessageDto(ex.getMessage())
        }
        return new ErrorMessageDto(ex.getMessage())
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    ErrorMessageDto handleException(final Exception ex, final HttpServletRequest request) {
        return new ErrorMessageDto(ex.getMessage())
    }
    
}
