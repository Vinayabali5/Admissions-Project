package uk.ac.reigate.controller.rest

import java.util.logging.Logger

import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

import uk.ac.reigate.domain.Address
import uk.ac.reigate.domain.academic.Student
import uk.ac.reigate.domain.academic.StudentYear
import uk.ac.reigate.dto.ApplicationFormDto
import uk.ac.reigate.dto.StudentSearchDto
import uk.ac.reigate.repositories.academic.StudentRepository
import uk.ac.reigate.repositories.academic.StudentYearRepository
import uk.ac.reigate.service.admissions.PostcodeLookupService
import uk.ac.reigate.service.admissions.StudentAdmissionsService
import uk.ac.reigate.util.exception.ApplicationNotFoundException

@RestController
@RequestMapping(value='/applications')
class StudentRestController {
    
    private final static Logger log = Logger.getLogger(StudentRestController.class.getName());
    
    @Autowired
    StudentAdmissionsService studentService
    
    @Autowired
    PostcodeLookupService postcodeLookupService
    
    @Autowired
    StudentRepository studentRepository
    
    @Autowired
    StudentYearRepository studentYearRepository
    
    /* Core REST End Points */
    Student app
    
    // GET /applications - Retrieve all Request
    
    @RequestMapping(method=RequestMethod.GET)
    List<ApplicationFormDto> findAll() {
        log.info("*** StudentRestController.findAll")
        List<Student> students = studentService.findAll()
        List<ApplicationFormDto> output = new ArrayList<ApplicationFormDto>()
        students.each { it ->
            output.add(new ApplicationFormDto(it))
        }
        return output
    }
    
    // GET /applications/{id} - Retrieve Application with id
    
    @RequestMapping(value='/{id}', method=RequestMethod.GET)
    ApplicationFormDto getById(@PathVariable(value='id') Integer studentId) {
        log.info("*** StudentRestController.get")
        Student app = studentRepository.findOne(studentId)
        
        StudentYear studentYear = studentYearRepository.findByStudent_Id(studentId)
        if (app == null) {
            throw new ApplicationNotFoundException(studentId.toString())
        }
        return new ApplicationFormDto(app, studentYear)
    }
    
    //POST/applications - Create New Application
    @RequestMapping(value='/', method=RequestMethod.POST)
    ApplicationFormDto processStudent(@Valid @RequestBody ApplicationFormDto app) {
        log.info("*** StudentRestController.processStudent")
        if (app.id == null && app.studentId != null) {
            log.info(" ** Saving new Student")
            
            studentRepository.save(new Student(
                    student: app,
                    referenceNo : app.referneceNo,
                    status : app.status != null ? applicationStatusRepository.findOne(app.status) : null,
                    academicYear : app.academicYear != null ? academicYearRepository.findOne(app.academicYear) : null,
                    endDate : app.endDate,
                    person : person,
                    app : new Student(
                    firstName: app.firstName,
                    surname: app.surname,
                    middleNames: app.middleNames,
                    preferredName: app.preferredName,
                    previousSurname: app.previousSurname,
                    dob: app.dob,
                    gender: app.gender != null ? genderRepository.findOne(app.gender) : null,
                    home: app.home,
                    work: app.work,
                    mobile: app.mobile,
                    email: app.email,
                    nationality: app.nationality != null ? nationalityRepository.findOne(app.nationality) : null,
                    countryOfResidence : app.countryOfResidence,
                    resident : app.resident,
                    
                    address: new Address(
                    paon: app.address.paon,
                    saon: app.address.saon,
                    street: app.address.street,
                    town: app.address.town,
                    county: app.address.county,
                    udprn: app.address.udprn,
                    line1: app.address.line1,
                    line2: app.address.line2,
                    line3: app.address.line3,
                    line4: app.address.line4,
                    line5: app.address.line5,
                    buildingName: app.address.buildingName,
                    subBuilding: app.address.subBuilding,
                    
                    postcode: app.address.postcode
                    ),
                    sibling : app.sibling,
                    siblingName : app.siblingName,
                    siblingYear : app.siblingYear,
                    siblingAdmNo : app.siblingAdmNo,
                    
                    school : app.school,
                    schoolReportStatus : app.schoolReportStatus != null ? schoolReportStatusRepository.findOne(app.schoolReportStatus) : null,
                    // lists
                    contacts: app.contacts,
                    //addresses: app.addresses,
                    requests: app.requests,
                    
                    
                    
                    refRequested: app.refRequested,
                    refReceived : app.refReceived,
                    reportRequested : app.reportRequested,
                    reportReceived : app.reportReceived,
                    
                    tutorGroup : app.tutorGroup,
                    
                    acceptanceReceived : app.acceptanceReceived,
                    ethnicity : app.ethnicity,
                    restrictedUseIndicator : app.restrictedUseIndicator != null ? restrictedUseRepository.findOne(app.restrictedUseIndicator) : null,
                    specialCategory : app.specialCategory != null ? specialCategoryRepository.findOne(app.specialCategory) : null,
                    
                    studentType : app.studentType,
                    uln : app.uln,
                    uci : app.uci,
                    
                    admissionsNotes: app.admissionsNotes,
                    
                    interview : app.interview,
                    interviewer : app.interviewer,
                    interviewDate : app.interviewDate,
                    //            interviewNotes : app.interviewNotes,
                    
                    offerType : app.offerType,
                    offerSent : app.offerSent,
                    
                    medicalNote : app.medicalNote,
                    
                    contactByPost : app.contactByPost,
                    contactByPhone : app.contactByPhone,
                    contactByEmail : app.contactByEmail,
                    
                    cannotAttendIntro : app.cannotAttendIntro,
                    cannotAttendInduction : app.cannotAttendInduction,
                    inductionDate : app.inductionDate,
                    blueCard : app.blueCard,
                    enrolmentInterviewDate = app.enrolmentInterviewDate,
                    enrolmentInterviewTime = app.enrolmentInterviewTime,
                    
                    )))
        }
        log.info(app)
    }
    
    // PUT / applications/{id} - Update application with id
    
    @RequestMapping(value='/{id}', method=RequestMethod.PUT)
    ApplicationFormDto update(@PathVariable(value="id") Integer id, @Valid @RequestBody ApplicationFormDto app) {
        log.info("** StudentRestController.update")
        
        if(app.id == null && app.studentId != null) {
            
            Student updateStudent = studentRepository.findOne (app.id)
            
            StudentYear updateStudentYear = studentYearRepository.findByStudent_Id(app.id)
            
            updateStudent.app.referenceNo = updateStudent.app.referenceNo != app.referenceNo ? app.referenceNo : updateStudent.app.referenceNo
            updateStudent.app.received = updateStudent.app.received != app.received ? app.received : updateStudent.app.received
            updateStudent.app.status = updateStudent.app.status != app.status ? app.status : updateStudent.app.status
            updateStudentYear.app.endDate = updateStudentYear.app.endDate != app.endDate ? app.endDate : updateStudentYear.app.endDate
            //            updateStudent.app.academicYear = updateStudent.app.academicYear != app.academicYear ? app.academicYear : updateStudent.app.academicYear
            
            // Update Person fields
            updateStudent.app.personId = updateStudent.app.personId != app.personId ? app.personId : updateStudent.app.personId
            updateStudent.app.firstName = updateStudent.app.firstName != app.firstName ? app.firstName : updateStudent.app.firstName
            updateStudent.app.surname = updateStudent.app.surname != app.surname ? app.surname : updateStudent.app.surname
            updateStudent.app.middleNames = updateStudent.app.middleNames != app.middleNames ? app.middleNames : updateStudent.app.middleNames
            updateStudent.app.preferredName = updateStudent.app.preferredName != app.preferredName ? app.preferredName : updateStudent.app.preferredName
            updateStudent.app.previousSurname = updateStudent.app.previousSurname != app.previousSurname ? app.previousSurname : updateStudent.app.previousSurname
            updateStudent.app.dob = updateStudent.app.dob != app.dob ? app.dob : updateStudent.app.dob
            updateStudent.app.title = updateStudent.app.title != app.title ? app.title : updateStudent.app.title
            updateStudent.app.gender = updateStudent.app.gender != app.gender ? app.gender : updateStudent.app.gender
            updateStudent.app.home = updateStudent.app.home != app.home ? app.home : updateStudent.app.home
            updateStudent.app.mobile = updateStudent.app.mobile != app.mobile ? app.mobile : updateStudent.app.mobile
            updateStudent.app.email = updateStudent.app.email != app.email ? app.email : updateStudent.app.email
            updateStudent.app.nationality = updateStudent.app.nationality != app.nationality ? app.nationality : updateStudent.app.nationality
            updateStudent.app.countryOfResidence = updateStudent.app.countryOfResidence != app.countryOfResidence ? app.countryOfResidence : updateStudent.app.countryOfResidence
            updateStudent.app.resident = updateStudent.app.resident != app.resident ? app.resident : updateStudent.app.resident
            
            // Update Address field
            updateStudent.app.person.address = updateStudent.app.person.address != app.person.address ? app.person.address : updateStudent.app.person.address
            updateStudent.app.person.address.paon = updateStudent.app.person.address.paon != app.person.address.paon ? app.person.address.paon : updateStudent.app.person.address.paon
            updateStudent.app.person.address.street = updateStudent.app.person.address.street != app.person.address.street ? app.person.address.street : updateStudent.app.person.address.street
            updateStudent.app.person.address.town = updateStudent.app.person.address.town != app.person.address.town ? app.person.address.town : updateStudent.app.person.address.town
            updateStudent.app.person.address.county = updateStudent.app.person.address.county != app.person.address.county ? app.person.address.county : updateStudent.app.person.address.county
            
            updateStudent.app.person.address.line1 = updateStudent.app.person.address.line1 != app.person.address.line1 ? app.person.address.line1 : updateStudent.app.person.address.line1
            updateStudent.app.person.address.line2 = updateStudent.app.person.address.line2 != app.person.address.line2 ? app.person.address.line2 : updateStudent.app.person.address.line2
            updateStudent.app.person.address.line3 = updateStudent.app.person.address.line3 != app.person.address.line3 ? app.person.address.line3 : updateStudent.app.person.address.line3
            updateStudent.app.person.address.line4 = updateStudent.app.person.address.line4 != app.person.address.line4 ? app.person.address.line4 : updateStudent.app.person.address.line4
            updateStudent.app.person.address.line5 = updateStudent.app.person.address.line5 != app.person.address.line5 ? app.person.address.line5 : updateStudent.app.person.address.line5
            updateStudent.app.person.address.buildingName = updateStudent.app.person.address.buildingName != app.person.address.buildingName ? app.person.address.buildingName : updateStudent.app.person.address.buildingName
            updateStudent.app.person.address.subBuilding = updateStudent.app.person.address.subBuilding != app.person.address.subBuilding ? app.person.address.subBuilding : updateStudent.app.person.address.subBuilding
            updateStudent.app.person.address.udprn = updateStudent.app.person.address.udprn != app.person.address.udprn ? app.person.address.udprn : updateStudent.app.person.address.udprn
            updateStudent.app.person.address.postcode = updateStudent.app.person.address.postcode != app.person.address.postcode ? app.person.address.postcode : updateStudent.app.person.address.postcode
            
            // Update Sibling fields
            updateStudent.app.sibling = updateStudent.app.sibling != app.sibling ? app.sibling : updateStudent.app.sibling
            updateStudent.app.siblingName = updateStudent.app.siblingName != app.siblingName ? app.siblingName : updateStudent.app.siblingName
            updateStudent.app.siblingYear = updateStudent.app.siblingYear != app.siblingYear ? app.siblingYear : updateStudent.app.siblingYear
            updateStudent.app.siblingAdmNo = updateStudent.app.siblingAdmNo != app.siblingAdmNo ? app.siblingAdmNo : updateStudent.app.siblingAdmNo
            
            //Update school fields
            updateStudent.app.school = updateStudent.app.school != app.school ? app.school : updateStudent.app.school
            updateStudent.app.schoolReportStatus = updateStudent.app.schoolReportStatus != app.schoolReportStatus ? app.schoolReportStatus : updateStudent.app.schoolReportStatus
            updateStudent.app.refRequested = updateStudent.app.refRequested != app.refRequested ? app.refRequested : updateStudent.app.refRequested
            updateStudent.app.refReceived = updateStudent.app.refReceived != app.refReceived ? app.refReceived : updateStudent.app.refReceived
            updateStudent.app.reportRequested = updateStudent.app.reportRequested != app.reportRequested ? app.reportRequested : updateStudent.app.reportRequested
            updateStudent.app.reportRequested = updateStudent.app.reportRequested != app.reportRequested ? app.reportRequested : updateStudent.app.reportRequested
            
            
            //Update tutor field
            updateStudentYear.app.tutorGroup = updateStudentYear.app.tutorGroup != app.tutorGroup ? app.tutorGroup : updateStudentYear.app.tutorGroup
            
            //Update acceptance field
            updateStudent.app.acceptanceReceived = updateStudent.app.acceptanceReceived != app.acceptanceReceived ? app.acceptanceReceived : updateStudent.app.acceptanceReceived
            updateStudent.app.ethnicity = updateStudent.app.ethnicity != app.ethnicity ? app.ethnicity : updateStudent.app.ethnicity
            updateStudent.app.restrictedUseIndicator = updateStudent.app.restrictedUseIndicator != app.restrictedUseIndicator ? app.restrictedUseIndicator : updateStudent.app.restrictedUseIndicator
            
            //Update medical information field
            updateStudent.app.medicalNote = updateStudent.app.medicalNote != app.medicalNote ? app.medicalNote : updateStudent.app.medicalNote
            updateStudent.app.specialCategory = updateStudent.app.specialCategory != app.specialCategory ? app.specialCategory : updateStudent.app.specialCategory
            
            //Update offer type field
            updateStudent.app.offerType = updateStudent.app.offerType != app.offerType ? app.offerType : updateStudent.app.offerType
            updateStudent.app.offerSent = updateStudent.app.offerSent != app.offerSent ? app.offerSent : updateStudent.app.offerSent
            
            //Update data sharing fields
            updateStudent.app.contactByPost = updateStudent.app.contactByPost != app.contactByPost ? app.contactByPost : updateStudent.app.contactByPost
            updateStudent.app.contactByPhone = updateStudent.app.contactByPhone != app.contactByPhone ? app.contactByPhone : updateStudent.app.contactByPhone
            updateStudent.app.contactByEmail = updateStudent.app.contactByEmail != app.contactByEmail ? app.contactByEmail : updateStudent.app.contactByEmail
            
            //Update Unique Learner No field
            updateStudentYear.app.studentType = updateStudentYear.app.studentType != app.studentType ? app.studentType : updateStudentYear.app.studentType
            updateStudent.app.uln = updateStudent.app.uln != app.uln ? app.uln : updateStudent.app.uln
            updateStudent.app.uci = updateStudent.app.uci != app.uln ? app.uci : updateStudent.app.uci
            
            //Update admissionsNotes
            updateStudent.app.admissionsNotes = updateStudent.app.admissionsNotes != app.admissionsNotes ? app.admissionsNotes : updateStudent.app.admissionsNotes
            
            // Update intro and induction day fields
            updateStudent.app.cannotAttendIntro = updateStudent.app.cannotAttendIntro != app.cannotAttendIntro ? app.cannotAttendIntro : updateStudent.app.cannotAttendIntro
            updateStudent.app.cannotAttendInduction = updateStudent.app.cannotAttendInduction != app.cannotAttendInduction ? app.cannotAttendInduction : updateStudent.app.cannotAttendInduction
            updateStudent.app.inductionDate = updateStudent.app.inductionDate != app.inductionDate ? app.inductionDate : updateStudent.app.inductionDate
            updateStudent.app.blueCard = updateStudent.app.blueCard  != app.blueCard  ? app.blueCard  : updateStudent.app.blueCard
            updateStudent.app.enrolmentInterviewDate = updateStudent.app.enrolmentInterviewDate  != app.enrolmentInterviewDate  ? app.enrolmentInterviewDate  : updateStudent.app.enrolmentInterviewDate
            updateStudent.app.enrolmentInterviewTime = updateStudent.app.enrolmentInterviewTime  != app.enrolmentInterviewTime  ? app.enrolmentInterviewTime  : updateStudent.app.enrolmentInterviewTime
            
            
            // Update interviewer fields
            updateStudent.app.interview = updateStudent.app.interview != app.interview ? app.interview : updateStudent.app.interview
            updateStudent.app.interviewer = updateStudent.app.interviewer != app.interviewer ? app.interviewer : updateStudent.app.interviewer
            updateStudent.app.interviewDate = updateStudent.app.interviewDate != app.interviewDate ? app.interviewDate : updateStudent.app.interviewDate
            //updateStudent.app.interviewNotes = updateStudent.app.interviewNotes != app.interviewNotes ? app.interviewNotes : updateStudent.app.interviewNotes
            
            
            updateStudent.app.requests = updateStudent.app.requests != app.requests ? app.requests : updateStudent.app.requests
            updateStudent.app.refRequested = updateStudent.app.refRequested != app.refRequested ? app.refRequested : updateStudent.app.refRequested
            updateStudent.app.refReceived = updateStudent.app.refReceived != app.refReceived ? app.refReceived : updateStudent.app.refReceived
            updateStudent.app.reportRequested = updateStudent.app.reportRequested != app.reportRequested ? app.reportRequested : updateStudent.app.reportRequested
            updateStudent.app.reportReceived = updateStudent.app.reportReceived != app.reportReceived ? app.reportReceived : updateStudent.app.reportReceived
            
            log.info("*** Updating student id: " + app.id)
            studentRepository.save(updateStudent)
        }
        return updateStudent
    }
    
    
    /* Additional REST End Points */
    
    @Deprecated
    @ResponseBody
    @RequestMapping('/OLDsearch/{q}')
    List<ApplicationFormDto> searchOldVersion(@PathVariable(value="q") String searchTerm) {
        log.info("*** StudentRestController.search")
        if (searchTerm.length() > 0) {
            List<ApplicationFormDto> output = new ArrayList<ApplicationFormDto>()
            List<Student> students = studentService.findByNamePart(searchTerm)
            if (students != null) {
                students.each { it ->
                    output.add(new ApplicationFormDto(it))
                }
            }
            students = studentService.findByReferenceNo(searchTerm)
            if (students != null) {
                students.each { it ->
                    output.add(new ApplicationFormDto(it))
                }
            }
            return output
        } else {
            return null
        }
    }
    
    
    @ResponseBody
    @RequestMapping('/search/{q}')
    List<StudentSearchDto> search(@PathVariable(value="q") String searchTerm) {
        log.info("*** StudentRestController.search")
        if (searchTerm.length() > 0) {
            List<StudentSearchDto> output = new ArrayList<StudentSearchDto>()
            List<Student> students = studentService.findByNamePart(searchTerm)
            if (students != null) {
                students.each { it ->
                    String personName = it.person
                    String schoolName = it.school
                    output.add(new StudentSearchDto(it.id, it.referenceNo, personName, schoolName, it.status.toString()))
                }
            }
            students = studentService.findByReferenceNo(searchTerm)
            if (students != null) {
                students.each { it ->
                    String personName = it.person
                    String schoolName = it.school
                    output.add(new StudentSearchDto(it.id, it.referenceNo, personName, schoolName, it.status.toString()))
                }
            }
            return output
        } else {
            return null
        }
    }
    
    
    
}
