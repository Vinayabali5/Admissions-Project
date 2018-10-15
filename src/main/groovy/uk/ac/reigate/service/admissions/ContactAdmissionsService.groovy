
package uk.ac.reigate.service.admissions

import java.util.logging.Logger

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import uk.ac.reigate.domain.Contact
import uk.ac.reigate.repositories.ContactRepository
import uk.ac.reigate.repositories.PersonRepository
import uk.ac.reigate.repositories.lookup.ContactTypeRepository

@Service
class ContactAdmissionsService {
    
    private final static Logger log = Logger.getLogger(ContactAdmissionsService.class.getName());
    
    @Autowired
    ContactRepository contactRepository
    
    @Autowired
    PersonRepository personRepository
    
    @Autowired
    ContactTypeRepository contactTypeRepository
    
    List<Contact> findAll() {
        return this.contactRepository.findAll();
    }
    
    public void add(final Contact contact){
        this.contactRepository.add(contact);
    }
}
