package uk.ac.reigate.controller.rest

import java.util.logging.Logger

import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import uk.ac.reigate.domain.Contact
import uk.ac.reigate.domain.Person
import uk.ac.reigate.dto.ContactDto
import uk.ac.reigate.repositories.ContactRepository
import uk.ac.reigate.repositories.PersonRepository
import uk.ac.reigate.repositories.academic.StudentRepository
import uk.ac.reigate.repositories.lookup.ContactTypeRepository
import uk.ac.reigate.repositories.lookup.TitleRepository
import uk.ac.reigate.util.exception.ContactNotFoundException
import uk.ac.reigate.util.exception.IdMismatchException


@RestController
@RequestMapping(value='/contacts')
class ContactRestController {
    
    private final static Logger log = Logger.getLogger(ContactRestController.class.getName());
    
    @Autowired
    StudentRepository studentRepository
    
    @Autowired
    PersonRepository personRepository
    
    @Autowired
    ContactRepository contactRepository
    
    @Autowired
    TitleRepository titleRepository
    
    @Autowired
    ContactTypeRepository contactTypeRepository
    
    /*
     Required End Points:
     GET /requests - Retrieve all Requests
     @RequestMapping(method=RequestMethod.GET)
     GET /requests/{id} - Retrieve Request with id
     @RequestMapping(value='/{id}', method=RequestMethod.GET)
     POST /requests - Create New Request
     @RequestMapping(method=RequestMethod.POST)
     PUT /requests/{id} - Update Request with id
     @RequestMapping(value="/{id}" method=RequestMethod.POST)
     */
    
    /* Core REST end points */
    
    // GET /contacts - Retrieve all Contacts
    @RequestMapping(method=RequestMethod.GET)
    List<ContactDto> getAll() {
        log.info("*** ContactRestController.findAll")
        List<Contact> contacts = contactRepository.findAll()
        List<ContactDto> output = new ArrayList<ContactDto>()
        contacts.each { it ->
            output.add(new ContactDto(it))
        }
        
        return output
    }
    
    // GET /contacts/{id} - Retrieve Contact with id
    @RequestMapping(value='/{id}', method=RequestMethod.GET)
    ContactDto getId(@PathVariable(value='id') Integer contactId){
        Contact contact = contactRepository.findOne(contactId)
        if (contact == null) {
            throw new ContactNotFoundException(contactId.toString())
        }
        return new ContactDto(contact)
    }
    
    
    //POST/contacts - Create New Contact
    @RequestMapping(value='/', method=RequestMethod.POST)
    ContactDto processContact(@Valid @RequestBody ContactDto contact) {
        log.info("*** ContactRestController.processContact")
        if (contact.id == null && contact.personId != null) {
            log.info(" ** Saving new contact")
            Person person = personRepository.findOne(contact.personId)
            contactRepository.save(new Contact(
                    person: person,
                    contact: new Person(
                    title: contact.title.id != null ? titleRepository.findOne(contact.title.id) : null,
                    firstName: contact.firstName,
                    surname: contact.surname,
                    home: contact.home,
                    mobile: contact.mobile,
                    work: contact.work,
                    email: contact.email,
                    address: contact.address != null ? contact.address : null
                    ),
                    contactType: contact.contactType.id != null ? contactTypeRepository.findOne(contact.contactType.id) : null,
                    contactable: contact.contactable,
                    preferred: contact.preferred,
                    ))
        }
    }
    
    // PUT / contacts/{id} - Update contact with id
    @RequestMapping(value='/{id}', method=RequestMethod.PUT)
    ContactDto update(@PathVariable(value="id") Integer id, @Valid @RequestBody ContactDto contact) {
        log.info("** ContactRestController.update")
        if (contact.id != id) {
            throw new IdMismatchException("ID does not match")
        }
        Contact updateContact = contactRepository.findOne(contact.id)
        if (updateContact == null) {
            throw new ContactNotFoundException("Cannot find contact with ID: " + id)
        }
        if (id != null && contact.id != null ) {
            updateContact.contact.title = contact.title.id !=null ? titleRepository.findOne(contact.title.id) : updateContact.contact.title
            updateContact.contact.firstName = updateContact.contact.firstName != contact.firstName  ? contact.firstName : updateContact.contact.firstName
            updateContact.contact.surname = updateContact.contact.surname != contact.surname ? contact.surname : updateContact.contact.surname
            updateContact.contact.home = updateContact.contact.home != contact.home ? contact.home : updateContact.contact.home
            updateContact.contact.mobile = updateContact.contact.mobile != contact.mobile ? contact.mobile : updateContact.contact.mobile
            updateContact.contact.work = updateContact.contact.work != contact.work  ? contact.work : updateContact.contact.work
            updateContact.contact.email = updateContact.contact.email != contact.email ? contact.email : updateContact.contact.email
            updateContact.contactType = updateContact.contactType.id != contact.contactType.id ? contactTypeRepository.findOne(contact.contactType.id) : updateContact.contactType
            updateContact.contactable = updateContact.contactable != contact.contactable ? contact.contactable : updateContact.contactable
            updateContact.preferred = updateContact.preferred != contact.preferred ? contact.preferred : updateContact.preferred
            if ( updateContact.contact.address != null && contact.address != null ) {
                updateContact.contact.address.updateAddress(contact.address)
            } else if ( updateContact.contact.address != null && contact.address == null ){
                updateContact.contact.address = null
            } else {
                updateContact.contact.address = contact.address
            }
            log.info("*** Updating contact id: " + contact.id)
            contactRepository.save(updateContact)
        }
        return new ContactDto(updateContact)
    }
    
    
    // DELETE  /contacts/{id} - Delete contact with id *
    @RequestMapping(value='/{id}', method=RequestMethod.DELETE)
    ResponseEntity<ContactDto> deleteByPersonId(@PathVariable(value="id") Integer id) {
        contactRepository.delete(id)
        return new ResponseEntity<ContactDto>(new ContactDto(contact), HttpStatus.OK)
    }
    
    /* Additional REST end points */
    
    @RequestMapping(value='/getByPerson/{id}', method=RequestMethod.GET, produces='application/json')
    List<ContactDto> getByPerson(@PathVariable(value='id') Integer personId) {
        log.info("*** ContactRestController.getByPerson")
        List<Contact> contacts = contactRepository.findByPerson(personRepository.findOne(personId))
        List<ContactDto> output = new ArrayList<ContactDto>()
        contacts.each { it ->
            output.add(new ContactDto(it))
        }
        return output
    }
    
}
