package uk.ac.reigate.controller.rest
import java.util.logging.Logger

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import uk.ac.reigate.domain.Address
import uk.ac.reigate.domain.lookup.PostcodeLookup
import uk.ac.reigate.repositories.AddressRepository
import uk.ac.reigate.repositories.academic.StudentRepository
import uk.ac.reigate.service.admissions.PostcodeLookupService


@RestController
@RequestMapping('/postcodes')
class PostcodeLookupRestController {
    
    private final static Logger log = Logger.getLogger(PostcodeLookupRestController.class.getName());
    
    @Autowired
    StudentRepository studentRepository
    
    @Autowired
    PostcodeLookupService postcodeLookupService
    
    @Autowired
    AddressRepository addressRepository
    
    
    @RequestMapping(value='/search/{postcode}', method=RequestMethod.GET)
    List<PostcodeLookup> search(@PathVariable (value='postcode') String postcode) {
        //call to postcode lookup service
        log.info("*** PostcodeLookupRestController.search")
        List<PostcodeLookup> postcodes = postcodeLookupService.search(postcode)
        return postcodes
    }
    
    
    // GET /Postcode/ retrieve/ {id}
    @RequestMapping(value='/retrieve/{id}', method=RequestMethod.GET)
    Address getById (@PathVariable (value='id') String id){
        Address postcodelookup = postcodeLookupService.retrieve(id);
        log.info("*** PostcodeLookupRestController.getById(id) returned");
        return postcodelookup
    }
    
}
