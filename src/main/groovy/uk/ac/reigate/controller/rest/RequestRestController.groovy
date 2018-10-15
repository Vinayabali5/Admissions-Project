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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import uk.ac.reigate.domain.academic.AcademicYear
import uk.ac.reigate.domain.academic.Student
import uk.ac.reigate.domain.admissions.Request
import uk.ac.reigate.dto.RequestDto
import uk.ac.reigate.repositories.academic.StudentRepository
import uk.ac.reigate.repositories.admissions.RequestRepository
import uk.ac.reigate.service.admissions.AcademicYearService
import uk.ac.reigate.service.admissions.CourseRequestAdmissionsService

@RestController
@RequestMapping(value='/requests')
class RequestRestController {
    
    private final static Logger log = Logger.getLogger(RequestRestController.class.getName());
    
    @Autowired
    CourseRequestAdmissionsService courseRequestService
    
    @Autowired
    RequestRepository requestRepository
    
    @Autowired
    StudentRepository studentRepository
    
    @Autowired
    AcademicYearService academicYearService
    
    /* Core REST end points */
    
    // GET /requests - Retrieve all Request
    @RequestMapping(method=RequestMethod.GET)
    List<RequestDto> getAll() {
        List<Request> requests = requestRepository.findAll()
        List<RequestDto> output = new ArrayList<RequestDto>()
        requests.each { it ->
            RequestDto req = new RequestDto(it)
            req.description = courseRequestService.getCourseDescription(req.request)
            output.add(req)
        }
        return output
    }
    
    // GET /requests/{id} - Retrieve Request with id
    @RequestMapping(value='/{id}', method=RequestMethod.GET)
    RequestDto getById(@PathVariable(value="id") Integer id) {
        RequestDto req = new RequestDto(requestRepository.findOne(id))
        req.description = courseRequestService.getCourseDescription(request)
        output.add(req)
        return req
    }
    
    // POST /requests - Create New Request
    @RequestMapping(value='/', method=RequestMethod.POST)
    ResponseEntity<?> create(@Valid @RequestBody RequestDto request) {
        log.info("*** CourseReqestRestController.create")
        if (request.id == null && request.studentId != null) { // This is a new request to be added to an Student
            AcademicYear nextYear =academicYearService.getNextAcademicYear()
            
            //#TODO VBM Add check for existence of request then return http status code or CONFLICT or something
            Request existingRequest = requestRepository.findByStudent_IdAndRequestAndAcademicYear(request.studentId, request.request , nextYear)
            if (existingRequest != null) {
                return new ResponseEntity<?>(HttpStatus.CONFLICT)
            }
            
            log.info("* Adding new request for Student: " + request.studentId)
            Student student = studentRepository.findOne(request.studentId)
            if (student == null ) {
                throw RequestNotFoundException(request.studentId)
            }
            Request req = new Request((Student) student, (String) request.request)
            Request savedReq = requestRepository.save(req)
            return new ResponseEntity<RequestDto>(new RequestDto(savedReq), HttpStatus.CREATED)
        }
        log.info(request)
        return new ResponseEntity<?>(HttpStatus.BAD_REQUEST)
    }
    
    
    // PUT /requests/{id} - Update Request with id
    @RequestMapping(value='/{id}', method=RequestMethod.PUT)
    RequestDto update(@PathVariable(value="id") Integer id, @Valid @RequestBody RequestDto request) {
        log.info("*** CourseReqestRestController.update")
        Request updateRequest
        if (request.id == null && request.studentId != null) { // This is an existing request and the database should be updated if the id and Student Id are found to match
            log.info("* Updating request id: " + request.id)
            updateRequest = requestRepository.findOne(request.studentId)
            updateRequest.request = request.request
            if (updateRequest.studentId != request.studentId) {
                updateRequest.student = studentRepository.findOne(request.studentId)
            }
            requestRepository.save(updateRequest)
        }
        log.info(updateRequest)
        return new RequestDto(updateRequest)
    }
    
    
    // DELETE  /requests/{id} - Delete request with id *
    @RequestMapping(value='/{id}', method=RequestMethod.DELETE)
    ResponseEntity<RequestDto> deleteById(@PathVariable(value="id") Integer id) {
        requestRepository.delete(id)
        return new ResponseEntity<RequestDto>(new RequestDto(request), HttpStatus.OK)
    }
    
    
    /* Additional REST end points */
    
    @RequestMapping(value='/getByStudent/{id}', method=RequestMethod.GET, produces='application/json')
    List<RequestDto> getByStudent(@PathVariable(value='id') Integer studentId,  @RequestParam(value = "academicYearId" , required = false) Integer academicYearId) {
        log.info("*** RequestRestController.getByStudent")
        if (academicYearId == null) {
            academicYearId = academicYearService.getNextAcademicYear().id;
        }
        List<Request> requests = requestRepository.findByStudent_IdAndAcademicYear_Id(studentId, academicYearId)
        List<RequestDto> output = new ArrayList<RequestDto>()
        requests.each { it ->
            RequestDto req = new RequestDto(it)
            req.description = courseRequestService.getCourseDescription(academicYearId, req.request)
            output.add(req)
        }
        return output
    }
    
    
    @RequestMapping(value='/addToStudent/{id}', method=RequestMethod.POST)
    RequestDto addToStudentForm(@Valid @RequestBody RequestDto request, @PathVariable(value="id") Integer studentId) {
        log.info("*** RequestRestController.addToStudentForm")
        log.info(request)
    }
    
}
