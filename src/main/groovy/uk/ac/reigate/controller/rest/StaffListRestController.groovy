package uk.ac.reigate.controller.rest

import java.util.logging.Logger

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import uk.ac.reigate.domain.Staff
import uk.ac.reigate.repositories.StaffRepository
import uk.ac.reigate.service.admissions.LookupAdmissionsService

@RestController
@RequestMapping(value='/staff')
class StaffListRestController {
    private final static Logger log = Logger.getLogger(StaffListRestController.class.getName());
    
    @Autowired
    StaffRepository staffRepository
    
    @Autowired
    LookupAdmissionsService lookupService
    
    @RequestMapping(method=RequestMethod.GET)
    List<Staff> getDefault() {
        return lookupService.findAllStaffCurrent()
    }
    
    @RequestMapping(value="/all", method=RequestMethod.GET)
    List<Staff> getAll() {
        return staffRepository.findByType_NeedInitialsTrue()
    }
    
    @RequestMapping(value="/current", method=RequestMethod.GET)
    List<Staff> getCurrent() {
        return lookupService.findAllStaffCurrent()
    }
}
