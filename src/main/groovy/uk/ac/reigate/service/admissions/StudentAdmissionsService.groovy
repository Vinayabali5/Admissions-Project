package uk.ac.reigate.service.admissions

import org.apache.log4j.Logger
import org.hibernate.exception.SQLGrammarException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import uk.ac.reigate.domain.Person
import uk.ac.reigate.domain.academic.AcademicYear
import uk.ac.reigate.domain.academic.Student
import uk.ac.reigate.domain.academic.StudentYear
import uk.ac.reigate.repositories.academic.StudentRepository
import uk.ac.reigate.repositories.academic.StudentYearRepository


@Service
class StudentAdmissionsService {
    
    private final static Logger log = Logger.getLogger(StudentAdmissionsService.class.getName());
    
    @Autowired
    PersonAdmissionsService personService
    
    @Autowired
    AcademicYearService academicYearService
    
    @Autowired
    ApplicationFormService studentFormService
    
    @Autowired
    StudentRepository studentRepository
    
    @Autowired
    StudentYearRepository studentYearRepository
    
    /**
     * The get method is used to retrieve a single Application object from the database by its ID.
     *
     * @param id the ID of the Application to retrieve
     * @return the Application retrieved from the database
     */
    public Student get(Integer id) {
        return studentRepository.findOne(id)
    }
    
    public String generateApplicationReference() {
        AcademicYear year = academicYearService.getNextAcademicYear()
        
        //String ref = (String)studentRepository.nextReference(year.code, true)
        String ref = null
        return ref
    }
    
    public Student getByReferenceNo(String referenceNo) {
        return studentRepository.findByReferenceNo(referenceNo);
    }
    
    public Student getById(Integer id) {
        return this.get(id);
    }
    
    public Student getByIdAndYear(Integer id, AcademicYear year) {
        return studentRepository.findByIdAndStudentYears_Year(id, year);
    }
    
    public StudentYear getByStudentAndYear(Student student, AcademicYear academicYear){
        return studentYearRepository.findByStudentAndYear(student, academicYear)
    }
    
    public StudentYear getByStudentIdAndYear(Integer studentId, AcademicYear academicYear){
        return studentYearRepository.findByStudent_IdAndYear(studentId, academicYear)
    }
    /**
     * This method is used to return a list of all the Application objects in the database
     *
     * @return a List of Application objects
     */
    public List<Student> findAll() {
        return studentRepository.findAllByStudentYears_Year(this.academicYearService.getNextAcademicYear())
    }
    
    /**
     * This method is used to save an Application object to the database using the StudentRepository
     *
     * @param app the Application object to save
     * @return the saved Application object
     */
    public Student save(Student app) {
        if (app.academicYear == null) {
            app.academicYear = academicYearService.getNextAcademicYear();
        }
        try {
            def savedApp = studentRepository.save(app);
            def reloadedApp = studentRepository.findByPerson(savedApp.person);
            return reloadedApp
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    
    public List<Student> findByNamePart(String name) {
        log.info("*** StudentService.findByNamePart")
        try {
            List<Person> people = personService.findByNamePart(name)
            if (people != null) {
                log.info("** Return: studentRepository.findByPersonIn(people)")
                return studentRepository.findByPersonInAndStudentYears_Year(people, academicYearService.getNextAcademicYear())
            } else {
                log.info("** Return: null")
                return null
            }
        } catch (SQLGrammarException sgex) {
            log.error("SQL Grammar Exception Occurred: " + sgex.message)
        } catch (Exception ex) {
            log.error("An Exception Occurred: " + ex.message)
        }
    }
    
    public List<Student> findByReferenceNo(String ref) {
        log.info("*** StudentService.findByReferenceNo")
        List<Student> students
        try {
            students = studentRepository.findByReferenceNoContainingIgnoreCaseAndStudentYears_Year(ref, academicYearService.getNextAcademicYear())
        } catch (SQLGrammarException sgex) {
            log.error("SQL Grammar Exception Occurred: " + sgex.message)
        } catch (Exception ex) {
            log.error("An Exception Occurred: " + ex.message)
        }
        return students
    }
}
