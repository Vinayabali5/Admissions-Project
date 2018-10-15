package uk.ac.reigate.controller.rest

import java.util.logging.Logger

import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import uk.ac.reigate.domain.academic.Student
import uk.ac.reigate.domain.admissions.CollegeFundPayment
import uk.ac.reigate.dto.CollegeFundPaymentDto
import uk.ac.reigate.repositories.academic.StudentRepository
import uk.ac.reigate.repositories.admissions.CollegeFundPaymentRepository
import uk.ac.reigate.util.exception.IdMismatchException


@RestController
@RequestMapping(value='/collegeFundPayments')
class CollegeFundPaymentRestController {
    
    private final static Logger log = Logger.getLogger(CollegeFundPaymentRestController.class.getName());
    
    @Autowired
    CollegeFundPaymentRepository collegeFundPaymentRepository
    
    @Autowired
    StudentRepository studentRepository
    
    /* Core REST end points */
    @RequestMapping(method=RequestMethod.GET)
    List<CollegeFundPaymentDto> getAll() {
        log.info("*** AddressRestController.findAll")
        List<CollegeFundPayment> collegeFundPayments = collegeFundPaymentRepository.findAll()
        List<CollegeFundPaymentDto> output = new ArrayList<CollegeFundPaymentDto>()
        collegeFundPayments.each { it ->
            output.add(new CollegeFundPaymentDto(it))
        }
        return output
    }
    
    // GET /collegeFundPayment/{id} - Retrieve CollegeFundPayment with id
    @RequestMapping(value='/{id}', method=RequestMethod.GET)
    CollegeFundPaymentDto getById(@PathVariable(value='id') Integer studentId){
        CollegeFundPayment collegeFundPayment = collegeFundPaymentRepository.findOne(studentId)
        return new CollegeFundPaymentDto(collegeFundPayment)
    }
    
    //Post/CollegeFundPayment - Create New CollegeFundPayment
    @RequestMapping(value='/', method=RequestMethod.POST)
    CollegeFundPaymentDto create(@Valid @RequestBody CollegeFundPaymentDto collegeFundPayment) {
        log.info("***CollegeFundPaymentRestController.processCollegeFundPayment")
        if (collegeFundPayment.id == null && collegeFundPayment.studentId != null) { // This is a new collegeFundPayment to be added to an application
            log.info("* Adding new CollegeFundPayment for Student: " + collegeFundPayment.studentId)
            Student app = studentRepository.findOne(collegeFundPayment.studentId)
            
            CollegeFundPayment coll = collegeFundPaymentRepository.save(new CollegeFundPayment(
                    student: app,
                    paymentDate: collegeFundPayment.paymentDate,
                    amount: collegeFundPayment.amount,
                    payee: collegeFundPayment.payee,
                    giftAid: collegeFundPayment.giftAid,
                    cash: collegeFundPayment.cash,
                    chequeDate : collegeFundPayment.chequeDate
                    ))
            return new CollegeFundPaymentDto(new CollegeFundPaymentDto(coll))
        }
        log.info(collegeFundPayment)
    }
    
    //put/ collgefundPayment/{id} - update collegeFundPayment with id
    @RequestMapping(value= '/{id}', method=RequestMethod.PUT)
    CollegeFundPaymentDto update(@PathVariable(value="id") Integer id, @Valid @RequestBody CollegeFundPaymentDto collegeFundPayment){
        log.info("***CollegeFundPaymentRestController.update")
        if(collegeFundPayment.id != id) {
            throw new IdMismatchException("ID does not match")
        }
        CollegeFundPayment updateCollegeFundPayment = collegeFundPaymentRepository.findOne(collegeFundPayment.id)
        
        if (collegeFundPayment.id != null && collegeFundPayment.studentId != null){ // This is an existing CollegeFundPayment and the database should be updated if the id and application Id are found to match
            log.info("* Updating collegeFundPayment id: " + collegeFundPayment.id)
            updateCollegeFundPayment.paymentDate = updateCollegeFundPayment.paymentDate != collegeFundPayment.paymentDate ? collegeFundPayment.paymentDate : updateCollegeFundPayment.paymentDate
            updateCollegeFundPayment.amount = updateCollegeFundPayment.amount != collegeFundPayment.amount ? collegeFundPayment.amount : updateCollegeFundPayment.amount
            updateCollegeFundPayment.payee = updateCollegeFundPayment.payee != collegeFundPayment.payee ? collegeFundPayment.payee : updateCollegeFundPayment.payee
            updateCollegeFundPayment.giftAid = updateCollegeFundPayment.giftAid != collegeFundPayment.giftAid ? collegeFundPayment.giftAid : updateCollegeFundPayment.giftAid
            updateCollegeFundPayment.cash = updateCollegeFundPayment.cash != collegeFundPayment.cash ? collegeFundPayment.cash : updateCollegeFundPayment.cash
            updateCollegeFundPayment.chequeDate = updateCollegeFundPayment.chequeDate != collegeFundPayment.chequeDate ? collegeFundPayment.chequeDate : updateCollegeFundPayment.chequeDate
            
            log.info("*** Updating CollegeFundPayment id: " + collegeFundPayment.id)
            collegeFundPaymentRepository.save(updateCollegeFundPayment)
        }
        return new CollegeFundPaymentDto(updateCollegeFundPayment)
    }
    
    /* Additional REST end points */
    
    @RequestMapping(value='/getByStudent/{id}', method=RequestMethod.GET, produces='application/json')
    List<CollegeFundPaymentDto> getByStudent(@PathVariable(value='id') Integer studentId) {
        log.info("*** CollegeFundPaymentRestController.getByStudent")
        List<CollegeFundPayment> collegeFundPayments = collegeFundPaymentRepository.findByStudent_Id(studentId)
        List<CollegeFundPaymentDto> output = new ArrayList<CollegeFundPaymentDto>()
        collegeFundPayments.each { it ->
            output.add(new CollegeFundPaymentDto(it))
        }
        return output
    }
    
    @RequestMapping(value='/addToStudent/{id}', method=RequestMethod.POST)
    CollegeFundPaymentDto addToStudentForm(@Valid @RequestBody CollegeFundPaymentDto collegeFundPayment, @PathVariable(value="id") Integer studentId) {
        log.info("*** RequestRestController.addToStudentForm")
        log.info(collegeFundPayment)
    }
    
}
