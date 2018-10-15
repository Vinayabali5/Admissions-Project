package uk.ac.reigate.service.admissions

import java.util.logging.Logger

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import uk.ac.reigate.domain.academic.Course
import uk.ac.reigate.predicates.CoursePredicates
import uk.ac.reigate.repositories.academic.CourseRepository
import uk.ac.reigate.repositories.lookup.LevelRepository
import uk.ac.reigate.repositories.lookup.SubjectRepository

@Component
class CourseRequestAdmissionsService {
    
    private final static Logger log = Logger.getLogger(CourseRequestAdmissionsService.class.getName());
    
    @Autowired
    SubjectRepository subjectRepository
    
    @Autowired
    LevelRepository levelRepository
    
    @Autowired
    CourseRepository courseRepository
    
    /**
     * This method takes a course request code and translates it into a description.
     *
     * @param requestCode the course code to translate
     * @return the description for the code
     */
    String getCourseDescription(Integer academicYearId, String requestCode) {
        String description = ""
        if (requestCode.size() == 5) {
            List<Course> courses = courseRepository.findAll(CoursePredicates.courseValidInYear(academicYearId).and(CoursePredicates.courseSpecIs(requestCode.substring(2,5))))
            if (courses.size == 0) {
                description = "Invalid course"
            } else {
                description = courses[0].getDescription()
            }
        } else {
            description += 'Request Invalid Format'
        }
        return description
    }
}
