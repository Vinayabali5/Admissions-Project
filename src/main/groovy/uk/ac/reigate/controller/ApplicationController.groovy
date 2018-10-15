package uk.ac.reigate.controller

import java.text.SimpleDateFormat

import javax.validation.Valid

import org.apache.log4j.Logger

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.propertyeditors.CustomDateEditor
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

import uk.ac.reigate.domain.Address
import uk.ac.reigate.domain.Contact
import uk.ac.reigate.domain.Person
import uk.ac.reigate.domain.academic.Student
import uk.ac.reigate.domain.admissions.Request
import uk.ac.reigate.dto.ApplicationFormDto
import uk.ac.reigate.repositories.admissions.CollegeFundPaidRepository
import uk.ac.reigate.repositories.ilr.LLDDHealthProblemCategoryRepository
import uk.ac.reigate.repositories.ilr.LLDDHealthProblemRepository
import uk.ac.reigate.repositories.lookup.SchoolReportStatusRepository
import uk.ac.reigate.service.admissions.AcademicYearService
import uk.ac.reigate.service.admissions.ApplicationFormService
import uk.ac.reigate.service.admissions.LookupAdmissionsService
import uk.ac.reigate.service.admissions.PostcodeLookupService
import uk.ac.reigate.service.admissions.StudentAdmissionsService


/**
 * The StudentController is used to process requests to create, view and edit Application objects. This is
 * achieved by using the studentForm form-backing object. The studentForm object can be constructed using
 * an Application object. The studentForm objects are then passed back and forth between the server and the user.
 * <br/><br/>
 *
 * The studentForm object can be processed using the {@link uk.ac.reigate.ApplcationFormService ApplcationFormService} .
 *
 * @author Michael Horgan
 *
 * @see uk.ac.reigate.service.ApplcationFormService
 *
 */
@Controller
@RequestMapping('/application')
class ApplicationController {
    
    private final static Logger log = Logger.getLogger(ApplicationController.class);
    
    @Autowired
    ApplicationFormService applicationFormService
    
    @Autowired
    StudentAdmissionsService studentService
    
    @Autowired
    LookupAdmissionsService lookupService
    
    @Autowired
    PostcodeLookupService postcodeLookupService
    
    @Autowired
    LLDDHealthProblemRepository llddHealthProblemRepository
    
    @Autowired
    LLDDHealthProblemCategoryRepository llddHealthProblemCategoryRepository
    
    @Autowired
    CollegeFundPaidRepository collegeFundPaidRepository
    
    @Autowired
    SchoolReportStatusRepository schoolReportStatusRepository
    
    @Autowired
    AcademicYearService academicYearService
    
    @ModelAttribute
    public void addModelAttributes(Model model) {
        model.addAttribute("genderList", lookupService.findAllGender())
        model.addAttribute("yearList", lookupService.findAllAcademicYear())
        model.addAttribute("academicYearList", lookupService.findAllAcademicYear())
        model.addAttribute("titleList", lookupService.findAllTitle())
        model.addAttribute("schoolList", lookupService.findAllSchool().sort{ it.name })
        model.addAttribute("contactTypeList", lookupService.findAllContactType())
        model.addAttribute("nationalityList", lookupService.findAllNationality())
        model.addAttribute("ethnicityList", lookupService.findAllEthnicity())
        model.addAttribute("tutorGroupList", lookupService.findAllTutorGroup().sort{ it.code})
        model.addAttribute("offerTypeList", lookupService.findAllOfferType().sort{ it.description })
        model.addAttribute("studentTypeList", lookupService.findAllStudentType())
        model.addAttribute("statusList", lookupService.findAllApplicationStatus())
        model.addAttribute("restrictedUseIndicatorList", lookupService.findAllRestrictedUseIndicator())
        model.addAttribute("applicationStatusList", lookupService.findAllApplicationStatus())
        model.addAttribute("specialCategoryList", lookupService.findAllSpecialCategory())
        model.addAttribute("staffList", lookupService.findAllStaffCurrent().sort{ it.person.surname + ', ' + it.person.firstName })
        model.addAttribute("llddHealthProblemList", llddHealthProblemRepository.findAll())
        model.addAttribute("llddHealthProblemCategoryList", llddHealthProblemCategoryRepository.findAll())
        model.addAttribute("collegeFundPaidList", collegeFundPaidRepository.findAll())
        model.addAttribute("schoolReportStatusList", schoolReportStatusRepository.findAll())
    }
    
    
    @InitBinder
    void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
        
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        binder.registerCustomEditor(Date.class, "enrolmentInterviewTime", new CustomDateEditor(timeFormat, true));
        
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        binder.registerCustomEditor(Date.class, "enrolmentInterviewDateTime", new CustomDateEditor(dateTimeFormat, true));
        
        
        // #TODO this code might be adapted to work with the Gender, School, and other classes...
        //		// college binding
        //		binder.registerCustomEditor(College.class, new PropertyEditorSupport() {
        //			@Override
        //			public void setAsText(String text) {
        //				int id = Integer.parseInt(text);
        //				College college = College.get(id);
        //				setValue(college);
        //			}
        //		});
    }
    
    
    /**
     * This method is used to create a new application form. This uses the studentForm object to create a form backed object that is only used for the initial
     * application form creation.
     *
     * @param model the new studentForm backing object
     * @return loads the admissions/application-form template
     */
    @RequestMapping(value='/new', method=RequestMethod.GET)
    String newStudent(Model model) {
        log.info("*** INFO: StudentController - newStudent()")
        def form = new ApplicationFormDto()
        
        // Set Defaults for New Application forms
        form.academicYear = academicYearService.getNextAcademicYear()
        form.countryOfResidence = "UK"
        form.resident = true
        form.nationality = lookupService.findNationalityByDescription("UK");
        
        // Setting randon reference number
        form.referenceNo = ""
        
        // Create Contacts array and populate with a Father and Mother ready to be edited
        form.contacts = new ArrayList<Contact>()
        
        Contact father = new Contact()
        father.contactType = lookupService.contactTypeRepository.findByName("Father");
        father.contact = new Person()
        father.contact.address = new Address()
        form.contacts.add(father)
        Contact mother = new Contact()
        mother.contactType = lookupService.contactTypeRepository.findByName("Mother");
        mother.contact = new Person()
        mother.contact.address = new Address()
        form.contacts.add(mother)
        
        // Add the default 4 requests
        form.requests = new ArrayList<Request>()
        form.requests.add(new Request())
        form.requests.add(new Request())
        form.requests.add(new Request())
        form.requests.add(new Request())
        
        // Add New Application form to the Model
        model.addAttribute("app", form)
        return 'admissions/application-form-new';
    }
    
    /**
     * This method is used to process the saving of a new application the has been retrieved from the application-form-new form.
     *
     * @return redirects to /application/edit/{id} or returns the view admissions/application-form-new if error saving
     */
    @RequestMapping(value='/save-new', method=RequestMethod.POST)
    String saveNewStudent(@ModelAttribute("app") @Valid ApplicationFormDto studentForm, BindingResult result, Model model) {
        log.info("*** INFO: StudentController - saveNewStudent()")
        if (result.hasErrors()) {
            String msg = "You must complete all fields correctly."
            model.addAttribute("message", msg)
            return 'admissions/application-form-new';
        } else {
            ApplicationFormDto app = applicationFormService.save(studentForm)
            return 'redirect:/application/edit/' + app.id
        }
    }
    
    
    /**
     * This method is used to view and edit an application form once it has been created.
     *
     * @param id the id of the Application to be edited
     * @param model the ApplicatioDto object for the Application to be edited
     * @return returns the view admissions/application-form
     */
    @RequestMapping(value='/edit/{id}', method=RequestMethod.GET)
    String editStudent(@PathVariable Integer id, Model model) {
        log.info("*** StudentController.editStudent()")
        ApplicationFormDto app = applicationFormService.getById(id);
        log.info("*** applicationFormService.getById(id)  returned");
        if (app.id == null) {
            return ''
        }
        model.addAttribute("app", app);
        log.info("** Return view: admissions/application-form")
        return 'admissions/application-form';
    }
    
    /**
     * This method is used to save updates to an edited application form.
     *
     * @param form This is an studentForm object submitted via POST to the controller
     * @param result This is used to check the POST data has been bound to the studentForm correctly
     * @param model This is used to create a model to be passed back to the view if the data is invalid.
     * @return redirects to /application/edit/{id} or returns the view admissions/application-form if error saving
     */
    @RequestMapping(value='/edit/{id}', method=RequestMethod.POST)
    String saveStudent(@ModelAttribute("app") @Valid ApplicationFormDto form, BindingResult result, Model model) {
        log.info("*** StudentController.saveStudent")
        if (result.hasErrors()) {
            log.warn("* WARN: The POST data is not valid.")
            String msg = "You must complete all fields correctly.";
            model.addAttribute("message", msg);
            model.addAttribute("error", result.allErrors)
            return 'admissions/application-form';
        } else {
            log.info("* POST data valid - processing save.")
            if (form.id == null) {
                model.addAttribute("message", "An error occurred when trying to save the student form.")
                return 'error'
            }
            if(form.status.id == 4 && form.endDate == null){
                model.addAttribute("message","Please Enter Date of Withdrawal");
                return 'admissions/application-form';
            }
            
            applicationFormService.save(form)
            log.info("** Redirect to: /application/edit/" + form.id)
            return 'redirect:/application/edit/' + form.id
        }
    }
    
    /**
     * This method is used to retrieve a list of all the students
     *
     * @param model a list of all Student objects
     * @return returns the view admissions/list
     */
    @RequestMapping(value='/list', method=RequestMethod.GET)
    String list(Model model) {
        log.info("*** INFO: StudentController - saveStudent()")
        List<Student> students = applicationFormService.findAll()
        model.addAttribute("applications", students)
        return 'admissions/list'
    }
}
