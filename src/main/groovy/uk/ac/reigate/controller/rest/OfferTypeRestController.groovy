package uk.ac.reigate.controller.rest

import java.util.logging.Logger

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import uk.ac.reigate.domain.admissions.OfferType
import uk.ac.reigate.repositories.admissions.OfferTypeRepository

@RestController
@RequestMapping(value='/offerTypes')
class OfferTypeRestController {
    private final static Logger log = Logger.getLogger(OfferTypeRestController.class.getName());
    
    @Autowired
    OfferTypeRepository offerTypeRepository
    
    @Cacheable(value="offerTypes")
    @RequestMapping(method=RequestMethod.GET)
    List<OfferType> getAll() {
        return offerTypeRepository.findAll()
    }
}
