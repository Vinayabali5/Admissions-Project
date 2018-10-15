package uk.ac.reigate.service.admissions
import java.util.logging.Logger

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import uk.ac.reigate.domain.Address
import uk.ac.reigate.domain.Contact
import uk.ac.reigate.domain.Person
import uk.ac.reigate.domain.academic.Student
import uk.ac.reigate.domain.academic.StudentYear
import uk.ac.reigate.domain.admissions.OfferType
import uk.ac.reigate.domain.admissions.Request
import uk.ac.reigate.domain.system.Setting
import uk.ac.reigate.dto.ApplicationFormDto
import uk.ac.reigate.repositories.AddressRepository
import uk.ac.reigate.repositories.ContactRepository
import uk.ac.reigate.repositories.PersonRepository
import uk.ac.reigate.repositories.academic.StudentRepository
import uk.ac.reigate.repositories.admissions.CollegeFundPaidRepository
import uk.ac.reigate.repositories.lookup.SchoolReportStatusRepository
import uk.ac.reigate.repositories.lookup.TitleRepository
import uk.ac.reigate.util.exception.ApplicationNotFoundException

/**
 * This service is used for retrieving, processing and saving ApplicationForm objects
 *
 * @author Michael Horgan
 *
 */

@Service
class ApplicationFormService {
    
    private final static Logger log = Logger.getLogger(ApplicationFormService.class.getName());
    
    @Autowired
    LookupAdmissionsService lookupService
    
    @Autowired
    SettingAdmissionsService settingService
    
    @Autowired
    PostcodeLookupService postcodeLookupService
    
    @Autowired
    StudentAdmissionsService studentService
    
    @Autowired
    PersonRepository personRepository
    
    @Autowired
    ContactRepository contactRepository
    
    @Autowired
    TitleRepository titleRepository
    
    @Autowired
    AddressRepository addressRepository
    
    @Autowired
    StudentRepository studentRepository
    
    @Autowired
    CollegeFundPaidRepository collegeFundPaidRepository
    
    @Autowired
    SchoolReportStatusRepository schoolReportStatusRepository
    
    @Autowired
    AcademicYearService academicYearService    
    
    /**
     * This method is used to retrieve an ApplicationForm object from the provided Application id
     *
     * @param id the id for the Application to load into an ApplicationForm object
     * @return the ApplicationForm object based on the Application specified by id
     */
    ApplicationFormDto getById(Integer id) {
        Student app = studentService.getByIdAndYear(id, academicYearService.getNextAcademicYear());
        
        StudentYear studentYear = studentService.getByStudentIdAndYear(id, academicYearService.getNextAcademicYear());
        if (app == null) {
            throw new ApplicationNotFoundException(id);
        }
        return new ApplicationFormDto(app, studentYear);
    }
    
    /**
     * This method is used to retrieve an ApplicationForm object from the provided Application Reference No
     *
     * @param ref the Reference No for the Application to load into an ApplicationForm object
     * @return the ApplicationForm object based on the Application specified by ref
     */
    ApplicationFormDto findByReferenceNo(String ref) {
        Student app = studentService.findByReferenceNo(ref)
        if (app == null) {
            throw new ApplicationNotFoundException(ref)
        }
        return new ApplicationFormDto(app)
    }
    
    /**
     * This method is used to set all the default values for an Application
     */
    Student setDefaultValues(Student app) {
        app.offerType = lookupService.offerTypeRepository.findByDescription(settingService.getSetting('DefaultOfferType'))
        return app
    }
    
    
    /**
     * This method is used to save a new Application entity to the database using the provided ApplicationDto object
     *
     * @param form the ApplicationDto object to use for the new Application
     * @return the id of the saved Application entity
     */
    Integer saveNew(ApplicationFormDto form) {
        log.info("*** ApplicationFormService.saveNew()")
        Student app = new Student()
        if (app != null) {
            log.info("* Setting Student Status.")
            app.status = lookupService.findApplicationStatusByDescription("New")
            
            //#TODO MAH test that this section works correctly
            log.info("* Setting Default offer Type.")
            Setting defaultOfferType = settingService.getSetting('DefaultOfferType')
            if (defaultOfferType != null) {
                OfferType offer = lookupService.findOfferTypeByDescription(defaultOfferType.value)
                app.offerType = lookupService.findOfferTypeByDescription(defaultOfferType.value)
            } else {
                log.info("WW - No DefaultOfferType in settings, using null or database default.")
            }
            
            log.info("* Setting Reference No.")
            app.referenceNo = form.referenceNo != null ? form.referenceNo : app.referenceNo
            app.admissionsNotes = form.admissionsNotes != null ? form.admissionsNotes : app.admissionsNotes
            //  app.endDate = form.endDate != null ? form.endDate : app.endDate
            app.academicYear = form.academicYear != null ? lookupService.findAcademicYearById(form.academicYear.id) : null
            app.received = form.received != null ? form.received : app.received
            if (app.submitted == null) {
                app.submitted =  new Date()
            }
            // #TODO add the separate field for the ApplicationForm here
            log.info("* Setting person fields.")
            app.person = new Person()
            app.person.title= form.title
            app.person.firstName = form.firstName
            app.person.surname = form.surname
            app.person.middleNames = form.middleNames
            app.person.preferredName = form.preferredName
            app.person.previousSurname = form.previousSurname
            app.person.dob = form.dob
            app.person.gender= form.gender != null ? lookupService.findGenderById(form.gender.id) : null
            app.person.home = form.home
            app.person.mobile = form.mobile
            app.person.email = form.email
            app.nationality = form.nationality != null ? lookupService.findNationalityById(form.nationality.id) : null
            app.countryOfResidence= form.countryOfResidence
            app.resident = form.resident
            
            log.info("* Setting address fields.")
            if (form.addressId != null) {
                //  app.person.address = addressRepository.findOne(form.address.id)
                Address address = addressRepository.findOne(form.addressId)
                app.person.address = address
            } else {
                app.person.address = new Address()
                
                app.person.address.paon = form.paon
                app.person.address.street = form.street
                app.person.address.town = form.town
                app.person.address.county = form.county
                
                app.person.address.line1 = form.line1
                app.person.address.line2 = form.line2
                app.person.address.line3 = form.line3
                app.person.address.line4 = form.line4
                app.person.address.line5 = form.line5
                app.person.address.buildingName = form.buildingName
                app.person.address.subBuilding = form.subBuilding
                app.person.address.udprn = form.udprn
                app.person.address.postcode = form.postcode
            }
            
            log.info("* Setting other fields.")
            app.resident = form.resident
            app.sibling= form.sibling
            app.siblingName= form.siblingName
            app.siblingYear= form.siblingYear
            app.siblingAdmNo= form.siblingAdmNo
            
            log.info("* Student Details fields.")
            app.uci = form.uci
            app.uln = form.uln
            
            log.info("* Setting school.")
            app.school = form.school
            app.schoolReportStatus = form.schoolReportStatus
            
            log.info("* Setting contacts.")
            if (form.contacts != null) {
                app.person.contacts = new ArrayList<Contact>()
                for (Contact c : form.contacts) {
                    if (c.id != null) {
                        // #TODO add lookup of contact by id the add this to the app.person.contacts
                        Contact contact = contactRepository.findOne(c.id)
                        app.person.contacts.add(new Contact(app.person, contact.contact, contact.contactType, contact.contactable, contact.preferred))
                    } else {
                        
                        if (c.contact.surname.length() != 0) {
                            c.person = app.person
                            if (c.contact.title.id == null) {
                                c.contact.title = titleRepository.findByDescription('None')
                            }
                            if (c.contact.address.postcode == null || c.contact.address.postcode == '') {
                                c.contact.address = null
                            }
                            app.person.contacts.add(c)
                        }
                    }
                }
            }
            
            log.info("* Saving the Application - Without Requests.")
            app = studentService.save(app)
            
            log.info("* Adding reqests to Application.")
            if (form.requests != null) {
                app.requests = new ArrayList<Request>()
                form.requests.unique { a, b -> a.request <=> b.request }.each { req ->
                    if (req.request.length() != 0) {
                        app.requests.add(new Request(app, req.request))
                    }
                }
            }
            
            log.info("* Saving the Application with Requests. ")
            app = studentService.save(app)
            
        }
        log.info("*** Return Application id")
        return app.id
    }
    
    Integer update(ApplicationFormDto form) {
        log.info("*** StudentFormService.update()")
        Student app = studentService.getById(form.id)
        
        StudentYear studentYear = studentService.getByStudentAndYear(app, this.academicYearService.getNextAcademicYear())
        
        if (app != null) {
            log.info("* Student retrieve for update")
            // #TODO MAH: decided if this is right
            app.referenceNo = form.referenceNo != null ? form.referenceNo : app.referenceNo
            app.admissionsNotes= form.admissionsNotes != null ? form.admissionsNotes : app.admissionsNotes
            app.status = form.status != null ? lookupService.findApplicationStatusById(form.status.id) : app.status
            //   app.academicYear = form.academicYear.id != null ? lookupService.findAcademicYearById(form.academicYear.id) : app.academicYear
            studentYear.endDate = form.endDate
            app.received = form.received != null ? form.received : app.received
            if (app.submitted == null) {
                app.submitted =  new Date()
            }
            
            // Update the Person object from the Application
            log.info("* Update the Person information")
            app.person.firstName = form.firstName
            app.person.surname = form.surname
            app.person.middleNames = form.middleNames
            app.person.preferredName = form.preferredName
            app.person.previousSurname = form.previousSurname
            app.person.dob = form.dob
            app.person.gender = form.gender.id != null ? lookupService.findGenderById(form.gender.id) : app.person.gender
            app.person.home = form.home
            app.person.mobile = form.mobile
            app.person.email = form.email
            app.nationality = form.nationality
            app.countryOfResidence= form.countryOfResidence
            app.resident = form.resident
            
            // Update sibling information
            log.info("* Update the Sibling information")
            app.sibling = form.sibling
            app.siblingName = form.siblingName
            app.siblingYear = form.siblingYear
            app.siblingAdmNo = form.siblingAdmNo
            
            
            // Update the Person.Address object form the Application
            log.info("* Update the Address information")
            app.person.address.paon = form.paon
            app.person.address.street = form.street
            app.person.address.town = form.town
            app.person.address.county = form.county
            
            app.person.address.line1 = form.line1
            app.person.address.line2 = form.line2
            app.person.address.line3 = form.line3
            app.person.address.line4 = form.line4
            app.person.address.line5 = form.line5
            app.person.address.buildingName = form.buildingName
            app.person.address.subBuilding = form.subBuilding
            app.person.address.udprn = form.udprn
            app.person.address.postcode = form.postcode
            
            
            // #TODO MAH: change the way the contacts are processed.
            if (form.contacts != null) {
                // This section is for a new ApplicationForm the contacts
                if (app.person.contacts == null) {
                    app.person.contacts = new ArrayList<Contact>()
                    for (Contact c : form.contacts) {
                        if (c.contact.surname.length() != 0) {
                            c.person = app.person
                            if(c.contact.address.postcode == null && c.contact.address.postcode == ''){
                                c.contact.address = null
                            }
                            app.person.contacts.add(c)
                        }
                    }
                }
            }
            
            // Phase 2 Fields
            log.info("* Update the School information")
            app.school = form.school
            app.schoolReportStatus = form.schoolReportStatus.id != null ? lookupService.findSchoolReportStatusById(form.schoolReportStatus.id) : null
            log.info("* Update Reference information")
            app.refRequested = form.refRequested
            app.refReceived = form.refReceived
            app.reportRequested = form.reportRequested
            app.reportReceived = form.reportReceived
            app.requests = form.requests
            
            log.info('* Update college fund payment information')
            app.collegeFundPaid = form.collegeFundPaid.id != null ? collegeFundPaidRepository.findOne(form.collegeFundPaid.id) : app.collegeFundPaid
            app.collegeFundPaidYears = form.collegeFundPaidYears
            //	app.collegeFundPayments = form.collegeFundPayments
            
            log.info("* Update Student information")
            studentYear.studentType = form.studentType.id != null ? lookupService.findStudentTypeById(form.studentType.id) : studentYear.studentType
            app.uln = form.uln
            app.uci = form.uci
            
            app.admissionsNotes = form.admissionsNotes
            
            log.info("* Update Interview information")
            app.interview = form.interview
            app.interviewer = form.interviewer
            app.interviewDate = form.interviewDate
            
            
            log.info("* Update Offer information")
            app.offerType = form.offerType
            app.offerSent = form.offerSent
            
            log.info("* Update Tutor information")
            studentYear.tutorGroup = form.tutorGroup.id != null ? lookupService.findTutorGroupById(form.tutorGroup.id) : studentYear.tutorGroup
            
            log.info("* Update Disability and MedicalNote  information")
            app.llddHealthProblem = form.llddHealthProblem
            app.ehcp = form.ehcp
            app.medicalNote = form.medicalNote
            app.specialCategory = form.specialCategory.id != null ? lookupService.findSpecialCategoryById(form.specialCategory.id) : app.specialCategory
            
            //  app.lLDDHealthProblem = form.lLDDHealthProblem
            
            // Phase 3 Fields
            log.info("* Update acceptance information")
            app.acceptanceReceived = form.acceptanceReceived
            app.ethnicity = form.ethnicity.id != null ? lookupService.findEthnicityById(form.ethnicity.id) : app.ethnicity
            
            if(form.restrictedUseIndicator.id == null){
                app.restrictedUseIndicator = null
            }else {
                app.restrictedUseIndicator = form.restrictedUseIndicator.id != null ? lookupService.findRestrictedUseIndicatorById(form.restrictedUseIndicator.id) : app.restrictedUseIndicator
            }
            
            log.info("* Update DataSharing information")
            app.contactByPost = form.contactByPost
            app.contactByPhone = form.contactByPhone
            app.contactByEmail = form.contactByEmail
            
            app.lrsOptOut = form.lrsOptOut
            
            log.info("* Update Intro and induction information")
            app.cannotAttendIntro = form.cannotAttendIntro
            app.cannotAttendInduction = form.cannotAttendInduction
            app.inductionDate = form.inductionDate
            app.blueCard  = form.blueCard
            app.enrolmentInterviewDate = form.enrolmentInterviewDateTime
            app.enrolmentInterviewTime = form.enrolmentInterviewTime
            
            // Save the Application
            app = studentService.save(app)
        }
        log.info("*** Return Student id")
        return app.id
    }
    
    ApplicationFormDto save(ApplicationFormDto form) {
        Integer id
        if (form.id == null) {
            // Create new Application
            id = saveNew(form)
        } else {
            // Load existing Application
            id = update(form)
        }
        
        // Set the ApplicationForm id to the Application id
        if (form.id == null) { form.id = id }
        
        // Return the ApplicationForm
        return form
    }
    
    List<Student> findAll() {
        return studentService.findAll()
    }
    
}
