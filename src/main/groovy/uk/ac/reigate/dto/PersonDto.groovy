package uk.ac.reigate.dto

import javax.persistence.CascadeType
import java.util.Date
import java.util.Set;

import uk.ac.reigate.domain.Address
import uk.ac.reigate.domain.Contact;
import uk.ac.reigate.domain.Person;
import uk.ac.reigate.domain.lookup.Gender;
import uk.ac.reigate.domain.lookup.Title

class PersonDto {
    
    Integer id
    
    Title title
    
    String firstName
    
    String preferredName
    
    String middleNames
    
    String surname
    
    String previousSurname
    
    Date dob
    
    Gender gender
    
    Address address
    
    String home
    
    String mobile
    
    String work
    
    String email
    
    Set<Contact> contacts
    
    PersonDto() {}
    
    PersonDto(String surname, String firstName, String middleNames, String previousSurname, Date dob, Gender gender, Title title) {
        this.title = title
        this.surname = surname
        this.firstName = firstName
        this.middleNames = middleNames
        this.previousSurname = previousSurname
        this.dob = dob
        this.gender = gender
    }
    
    PersonDto(Person person){
        this.id = person.id
        this.title = person.title
        this.surname = person.surname
        this.firstName = person.firstName
        this.middleNames = person.middleNames
        this.previousSurname = person.previousSurname
        this.dob = person.dob
        this.gender = person.gender
        this.address = person.address
        this.home = person.home
        this.mobile = person.mobile
        this.work = person.work
        this.email = person.email
        this.contacts = person.contacts
    }
    
    PersonDto(String surname, String firstName) {
        this(surname, firstName)
        // this.title = title
    }
    
    String toString() {
        return this.surname + ', ' + this.firstName
    }
}
