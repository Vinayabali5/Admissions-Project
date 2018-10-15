package uk.ac.reigate.service.admissions

import java.util.logging.Logger

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

import uk.ac.reigate.domain.Contact
import uk.ac.reigate.domain.Person
import uk.ac.reigate.repositories.PersonRepository

@Service
class PersonAdmissionsService {
    
    private final static Logger log = Logger.getLogger(PersonAdmissionsService.class.getName());
    
    @Autowired
    PersonRepository personRepository
    
    /**
     * The get method is used to retrieve a single Person object from the database by its ID.
     *
     * @param id the ID of the Person to retrieve
     * @return the Person retrieved from the database
     */
    @Cacheable(value="people", key="#id")
    Person get(Integer id) {
        return personRepository.findOne(id)
    }
    
    /**
     * The findByNamePart method is used to find a list of Person objects with part of their name containing the search parameter.
     *
     * @param name the search parameter to look for
     * @return a List<Person> or null if no records found
     */
    List<Person> findByNamePart(String name) {
        log.info("*** PersonService.findByNamePart")
        List<Person> people = new ArrayList<Person>()
        
        people.addAll(personRepository.findBySurnameContainingIgnoreCase(name))
        people.addAll(personRepository.findByFirstNameContainingIgnoreCase(name))
        people.addAll(personRepository.findByPreferredNameContainingIgnoreCase(name))
        people.addAll(personRepository.findByMiddleNamesContainingIgnoreCase(name))
        people.addAll(personRepository.findByPreviousSurnameContainingIgnoreCase(name))
        
        if (people.size() != 0) {
            log.info("* " + people.size() + " records found")
            log.info("** Return: List<Person>")
            return people
        } else {
            log.info("* No records found")
            log.info("** Return: null")
            return null
        }
    }
    
    Set<Contact> getPersonContacts(Integer id) {
        log.info("*** PersonService.getPersonContacts")
        Person person = personRepository.findOne(id)
        Set<Contact> contacts = person.contacts
        log.info("** Return list of contacts for Person")
        return contacts
    }
    
    Person addContactToPerson(Contact contact, Integer personId) {
        log.info("*** PersonService.addContactToPerson")
        log.info('* Retrieving person')
        Person person = personRepository.findOne(id)
        log.info('* Adding contact')
        person.contacts.add(contact)
        log.info('* Saving person')
        return personRepository.save(person)
    }
}
