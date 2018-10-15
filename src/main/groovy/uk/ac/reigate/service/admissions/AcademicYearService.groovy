package uk.ac.reigate.service.admissions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import uk.ac.reigate.domain.academic.AcademicYear
import uk.ac.reigate.domain.system.Setting
import uk.ac.reigate.repositories.academic.AcademicYearRepository

@Service
class AcademicYearService {
    
    @Autowired
    AcademicYearRepository academicYearRepository
    
    @Autowired
    SettingAdmissionsService settingService
    
    /**
     * The get method is used to retrieve a single Year object from the database by its ID.
     *
     * @param id the ID of the Year to retrieve
     * @return the Year retrieved from the database
     */
    AcademicYear get(Integer id) {
        return academicYearRepository.findOne(id)
    }
    
    /**
     * The get method is used to retrieve a single Year object from the database by its ID.
     *
     * @param id the ID of the Year to retrieve
     * @return the Year retrieved from the database
     */
    AcademicYear getByCode(String code) {
        return academicYearRepository.findByCode(code)
    }
    
    AcademicYear getCurrent() {
        String code = settingService.getSetting("CurrentYearCode").value
        if (code != null) {
            return getByCode(code)
        } else {
            return null
        }
    }
    
    AcademicYear getNextAcademicYear() {
        String code = settingService.getSetting("NextYearCode").value
        if (code != null) {
            return getByCode(code)
        } else {
            return null
        }
    }
}
