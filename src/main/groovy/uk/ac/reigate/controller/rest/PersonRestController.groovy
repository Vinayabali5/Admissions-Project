package uk.ac.reigate.controller.rest

import java.util.logging.Logger

import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import uk.ac.reigate.domain.Address
import uk.ac.reigate.domain.Person
import uk.ac.reigate.repositories.PersonRepository
import uk.ac.reigate.repositories.academic.StudentRepository
import uk.ac.reigate.repositories.lookup.TitleRepository

@RestController
@RequestMapping(value='/people')
class PersonRestController {
    
    private final static Logger log = Logger.getLogger(PersonRestController.class.getName());
    
    @Autowired
    StudentRepository studentRepository
    
    @Autowired
    PersonRepository personRepository
    
    @Autowired
    TitleRepository titleRepository
    
    /*
     Required End Points:
     GET /people - Retrieve all people
     @RequestMapping(method=RequestMethod.GET)
     GET /people/{id} - Retrieve Person with id
     @RequestMapping(value='/{id}', method=RequestMethod.GET)
     POST /people - Create New Person
     @RequestMapping(method=RequestMethod.POST)
     PUT /people/{id} - Update Person with id
     @RequestMapping(value="/{id}" method=RequestMethod.POST)
     */
    
    /* Core REST end points */
    
    // GET /people - Retrieve all People
    @RequestMapping(method=RequestMethod.GET)
    List<Person> getAll() {
        log.info("*** PersonRestController.findAll")
        List<Person> people = personRepository.findAll()
        return people
        
    }
    
    // GET / people/{id} - Retrieve Person with id
    @RequestMapping(value='/{id}', method=RequestMethod.GET)
    Person getById(@PathVariable(value='id') Integer personId) {
        log.info("*** PersonRestController.get")
        Person person = personRepository.findOne(personId)
        return person
    }
    
    //POST/people - Create New Person
    @RequestMapping(value='/', method=RequestMethod.POST)
    Person processPerson(@Valid @RequestBody Person person) {
        log.info("*** PersonRestController.processPerson")
        if (person.id == null && person.personId != null) {
            log.info(" ** Saving new Person")
            Person newPerson = new Person(
                    title: person.title.id != null ? titleRepository.findOne(person.title.id) : null,
                    firstName: person.firstName,
                    surname: person.surname,
                    middleNames: person.middleNames,
                    preferredName: person.preferredName,
                    previousSurname: person.previousSurname,
                    gender: person.gender.id != null ? genderRepository.findOne(person.gender.id) : null,
                    address: new Address(
                    line1: person.address.line1,
                    line2: person.address.line2,
                    line3: person.address.line3,
                    line4: person.address.line4,
                    line5: person.address.line5,
                    buildingName: person.address.buildingName,
                    subBuilding: person.address.subBuilding,
                    udprn:person.address.udprn,
                    town: person.address.town,
                    county: person.address.county,
                    postcode: person.address.postcode
                    ),
                    home: person.home,
                    work: person.work,
                    mobile: person.mobile,
                    email: person.email,
                    notes: person.notes
                    )
            
            if (person.title == null){
                return ''
            }
            personRepository.save(newPerson)
        }
        log.info(newPerson)
        
        
    }
    
    
    // PUT / person/{id} - Update person with id
    
    @RequestMapping(value='/{id}', method=RequestMethod.PUT)
    Person update(@PathVariable(value="id") Integer id, @Valid @RequestBody Person person) {
        log.info("** PersonRestController.update")
        // #TODO add code to retrieve the database request and compare the ids to ensure the request to update is genuine
        Person updatePerson = personRepository.findOne(id)
        
        if (person.id == null && updatePerson != null && updatePerson.id.equals(id)) {
            updatePerson.person.title = updatePerson.person.title.id != person.titleId ? titleRepository.findOne(person.titleId) : updatePerson.person.title
            updatePerson.person.firstName = updatePerson.person.firstName != person.firstName  ? person.firstName : updatePerson.person.firstName
            updatePerson.person.surname = updatePerson.person.surname != person.surname ? person.surname : updatePerson.person.surname
            updatePerson.person.middleNames = updatePerson.person.middleNames != person.middleNames ? person.middleNames : updatePerson.person.middleNames
            updatePerson.person.preferredName = updatePerson.person.preferredName != person.preferredName ? person.preferredName : updatePerson.person.preferredName
            updatePerson.person.previousSurname = updatePerson.person.previousSurname != person.previousSurname ? person.previousSurname : updatePerson.person.previousSurname
            updatePerson.person.gender = updatePerson.person.gender.id != person.genderId ? genderRepository.findOne(person.genderId) : updatePerson.person.gender
            
            updatePerson.person.address = updatePerson.person.address != person.address ? person.address : updatePerson.person.address
            updatePerson.person.address.poan = updatePerson.person.address.poan != person.address.poan ? person.address.poan : updatePerson.person.address.poan
            updatePerson.person.address.saon = updatePerson.person.address.saon != person.address.saon ? person.address.saon : updatePerson.person.address.saon
            updatePerson.person.address.street = updatePerson.person.address.street != person.address.street ? person.address.street : updatePerson.person.address.street
            updatePerson.person.address.line1 = updatePerson.person.address.line1 != person.address.line1 ? person.address.line1 : updatePerson.person.address.line1
            updatePerson.person.address.line2 = updatePerson.person.address.line2 != person.address.line2 ? person.address.line2 : updatePerson.person.address.line2
            updatePerson.person.address.line3 = updatePerson.person.address.line3 != person.address.line3 ? person.address.line3 : updatePerson.person.address.line3
            updatePerson.person.address.line4 = updatePerson.person.address.line4 != person.address.line4 ? person.address.line4 : updatePerson.person.address.line4
            updatePerson.person.address.line5 = updatePerson.person.address.line5 != person.address.line5 ? person.address.line5 : updatePerson.person.address.line5
            updatePerson.person.address.buildingName = updatePerson.person.address.buildingName != person.address.buildingName ? person.address.buildingName : updatePerson.person.address.buildingName
            updatePerson.person.address.subBuilding = updatePerson.person.address.subBuilding != person.address.subBuilding ? person.address.subBuilding : updatePerson.person.address.subBuilding
            updatePerson.person.address.udprn = updatePerson.person.address.udprn != person.address.udprn ? person.address.udprn : updatePerson.person.address.udprn
            updatePerson.person.address.town = updatePerson.person.address.town != person.address.town ? person.address.town : updatePerson.person.address.town
            updatePerson.person.address.county = updatePerson.person.address.county != person.address.county ? person.address.county : updatePerson.person.address.county
            updatePerson.person.address.postcode = updatePerson.person.address.postcode != person.address.postcode ? person.address.postcode : updatePerson.person.address.postcode
            
            
            updatePerson.person.home = updatePerson.person.home != person.home ? person.home : updatePerson.person.home
            updatePerson.person.work = updatePerson.person.work != person.work  ? person.work : updatePerson.person.work
            updatePerson.person.mobile = updatePerson.person.mobile != person.mobile ? person.mobile : updatePerson.person.mobile
            updatePerson.person.email = updatePerson.person.email != person.email ? person.email : updateContact.person.email
            
            
            
            log.info("*** Updating person id: " + person.id)
            personRepository.save(updatePerson)
        }
        
        return updatePerson
        
    }
    
}
