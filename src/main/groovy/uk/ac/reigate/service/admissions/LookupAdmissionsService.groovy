package uk.ac.reigate.service.admissions;

import java.util.logging.Logger

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

import uk.ac.reigate.domain.Address
import uk.ac.reigate.domain.Staff
import uk.ac.reigate.domain.academic.AcademicYear
import uk.ac.reigate.domain.academic.School
import uk.ac.reigate.domain.academic.SpecialCategory
import uk.ac.reigate.domain.admissions.ApplicationStatus
import uk.ac.reigate.domain.admissions.Interview
import uk.ac.reigate.domain.admissions.OfferType
import uk.ac.reigate.domain.ilr.RestrictedUseIndicator
import uk.ac.reigate.domain.lookup.ContactType
import uk.ac.reigate.domain.lookup.Ethnicity
import uk.ac.reigate.domain.lookup.Gender
import uk.ac.reigate.domain.lookup.Nationality
import uk.ac.reigate.domain.lookup.SchoolReportStatus
import uk.ac.reigate.domain.lookup.StudentType
import uk.ac.reigate.domain.lookup.Title
import uk.ac.reigate.domain.lookup.TutorGroup
import uk.ac.reigate.repositories.AddressRepository
import uk.ac.reigate.repositories.SchoolRepository
import uk.ac.reigate.repositories.StaffRepository
import uk.ac.reigate.repositories.academic.AcademicYearRepository
import uk.ac.reigate.repositories.academic.SpecialCategoryRepository
import uk.ac.reigate.repositories.admissions.ApplicationStatusRepository
import uk.ac.reigate.repositories.admissions.InterviewRepository
import uk.ac.reigate.repositories.admissions.OfferTypeRepository
import uk.ac.reigate.repositories.lookup.ContactTypeRepository
import uk.ac.reigate.repositories.lookup.EthnicityRepository
import uk.ac.reigate.repositories.lookup.GenderRepository
import uk.ac.reigate.repositories.lookup.NationalityRepository
import uk.ac.reigate.repositories.lookup.RestrictedUseRepository
import uk.ac.reigate.repositories.lookup.SchoolReportStatusRepository
import uk.ac.reigate.repositories.lookup.StudentTypeRepository
import uk.ac.reigate.repositories.lookup.TitleRepository
import uk.ac.reigate.repositories.lookup.TutorGroupRepository

@Service
class LookupAdmissionsService {
    
    private final static Logger log = Logger.getLogger(LookupAdmissionsService.class.getName());
    
    @Autowired
    GenderRepository genderRepository;
    
    @Autowired
    TitleRepository titleRepository;
    
    @Autowired
    SchoolRepository schoolRepository;
    
    @Autowired
    AddressRepository addressRepository;
    
    @Autowired
    ContactTypeRepository contactTypeRepository
    
    @Autowired
    ApplicationStatusRepository applicationStatusRepository
    
    @Autowired
    OfferTypeRepository offerTypeRepository
    
    @Autowired
    NationalityRepository nationalityRepository
    
    @Autowired
    EthnicityRepository ethnicityRepository
    
    @Autowired
    TutorGroupRepository tutorGroupRepository
    
    @Autowired
    StudentTypeRepository studentTypeRepository
    
    @Autowired
    RestrictedUseRepository restrictedUseRepository
    
    @Autowired
    InterviewRepository interviewRepository
    
    @Autowired
    SpecialCategoryRepository specialCategoryRepository
    
    @Autowired
    SchoolReportStatusRepository schoolReportStatusRepository;
    
    @Autowired
    AcademicYearRepository academicYearRepository;
    
    @Autowired
    StaffRepository staffRepository
    
    public ApplicationStatus findApplicationStatusByDescription(String description) {
        log.finer("-- Lookup Service - Find Application Status By Description")
        return applicationStatusRepository.findByDescription(description)
    }
    
    public List<ApplicationStatus> findAllApplicationStatus() {
        log.finer("-- Lookup Service - Find All Application Status")
        return applicationStatusRepository.findAll();
    }
    
    public ApplicationStatus findApplicationStatusById(Integer id) {
        log.finer("-- Lookup Service - Find Application Status By ID")
        return applicationStatusRepository.findOne(id);
    }
    
    @Cacheable(value="genders")
    public List<Gender> findAllGender() {
        log.finer("-- Lookup Service - Find All Gender")
        return genderRepository.findAll();
    }
    
    @Cacheable(value="genders", key = "#id")
    public Gender findGenderById(Integer id) {
        log.finer("-- Lookup Service - Find Gender By ID")
        return genderRepository.findOne(id);
    }
    
    public List<AcademicYear> findAllAcademicYear() {
        log.finer("-- Lookup Service - Find All AcademicYear")
        return academicYearRepository.findAll();
    }
    
    public AcademicYear findAcademicYearById(Integer id) {
        log.finer("-- Lookup Service - Find AcademicYear By ID")
        return academicYearRepository.findOne(id);
    }
    
    @Cacheable(value="titles")
    public List<Title> findAllTitle() {
        log.finer("-- Lookup Service - Find All Title")
        return titleRepository.findAll();
    }
    
    @Cacheable(value="titles", key = "#id")
    public Title findTitleById(Integer id) {
        log.finer("-- Lookup Service - Find Title By ID")
        return titleRepository.findOne(id);
    }
    
    public Address findAddressById(Integer id) {
        log.finer("-- Lookup Service - Find address By ID")
        return addressRepository.findOne(id);
    }
    
    public List<School> findAllSchool() {
        log.finer("-- Lookup Service - Find All School")
        return schoolRepository.findAll();
    }
    
    public SchoolReportStatus findSchoolReportStatusById(Integer id) {
        log.finer("-- Lookup Service - Find SchoolReportStatus By ID")
        return schoolReportStatusRepository.findOne(id);
    }
    
    public List<SchoolReportStatus> findAllSchoolReportStatus() {
        log.finer("-- Lookup Service - Find All SchoolReportStatus")
        return schoolReportStatusRepository.findAll();
    }
    
    
    @Cacheable(value="tutorGroups")
    public List<TutorGroup> findAllTutorGroup() {
        log.finer("-- Lookup Service - Find All Tutor Group")
        return tutorGroupRepository.findAll();
    }
    
    @Cacheable(value="tutorGroups", key = "#id")
    public TutorGroup findTutorGroupById(Integer id) {
        log.finer("-- Lookup Service - Find Tutor Group By ID")
        return tutorGroupRepository.findOne(id)
    }
    
    
    @Cacheable(value="contactTypes")
    public List<ContactType> findAllContactType() {
        log.finer("-- Lookup Service - Find All Contact Type")
        return contactTypeRepository.findAll()
    }
    
    @Cacheable(value="ethnicities")
    public List<Ethnicity> findAllEthnicity() {
        log.finer("-- Lookup Service - Find All Ethnicity")
        return ethnicityRepository.findAll()
    }
    
    @Cacheable(value="ethnicities", key = "#id")
    public Ethnicity findEthnicityById(Integer id) {
        log.finer("-- Lookup Service - Find Ethnicity By ID")
        return ethnicityRepository.findOne(id)
    }
    
    @Cacheable(value="nationalities")
    public List<Nationality> findAllNationality() {
        log.finer("-- Lookup Service - Find All Nationality")
        return nationalityRepository.findAll()
    }
    
    @Cacheable(value="nationalities", key="#id")
    public Nationality findNationalityById(Integer id) {
        log.finer("-- Lookup Service - Find Nationality By ID")
        return nationalityRepository.findOne(id)
    }
    
    @Cacheable(value="nationalities", key="#description")
    public Nationality findNationalityByDescription(String description) {
        log.finer("-- Lookup Service - Find Nationality By Description")
        return nationalityRepository.findByDescription(description)
    }
    
    @Cacheable(value="offerTypes")
    public List<OfferType> findAllOfferType() {
        log.finer("-- Lookup Service - Find All Offer Type")
        return offerTypeRepository.findAll();
    }
    
    @Cacheable(value="offerTypes", key="#id")
    public OfferType findOfferTypeById(Integer id) {
        log.finer("-- Lookup Service - Find Offer Type By ID")
        return offerTypeRepository.findOne(id);
    }
    
    @Cacheable(value="offerTypes", key="#description")
    public OfferType findOfferTypeByDescription(String description) {
        log.finer("-- Lookup Service - Find Offer Type By Description")
        return offerTypeRepository.findByDescription(description);
    }
    
    @Cacheable(value="studentTypes")
    public List<StudentType> findAllStudentType() {
        log.finer("-- Lookup Service - Find All Student Type")
        return studentTypeRepository.findAll();
    }
    
    @Cacheable(value="studentTypes", key="#id")
    public StudentType findStudentTypeById(Integer id) {
        log.finer("-- Lookup Service - Find Student Type By ID")
        return studentTypeRepository.findOne(id);
    }
    
    public List<RestrictedUseIndicator> findAllRestrictedUseIndicator() {
        log.finer("-- Lookup Service - Find All Restricted Use Indicator")
        return restrictedUseRepository.findAll()
    }
    
    public RestrictedUseIndicator findRestrictedUseIndicatorById(Integer id) {
        log.finer("-- Lookup Service - Find Learning Difficulty By ID")
        return restrictedUseRepository.findOne(id);
    }
    
    public List<Interview> findAllInterview() {
        log.finer("-- Lookup Service - Find All Interview")
        return interviewRepository.findAll();
    }
    
    public List<SpecialCategory> findAllSpecialCategory() {
        log.finer("-- Lookup Service - Find All Special Category")
        return specialCategoryRepository.findAll();
    }
    
    public SpecialCategory findSpecialCategoryById(Integer id) {
        log.finer("-- Lookup Service - Find Special Category By ID")
        return specialCategoryRepository.findOne(id);
    }
    
    public List<Staff> findAllStaffCurrent() {
        log.finer("-- Lookup Service - Find All Staff")
        return staffRepository.findAllCurrent() //.sort { it.person.surname + it.person.firstName };
        //return staffRepository.findByEndDateIsNull().sort { it.person.surname + it.person.firstName };
    }
}
