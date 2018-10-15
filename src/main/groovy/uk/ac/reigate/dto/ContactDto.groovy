package uk.ac.reigate.dto

import uk.ac.reigate.domain.Address
import uk.ac.reigate.domain.Contact
import uk.ac.reigate.domain.lookup.ContactType
import uk.ac.reigate.domain.lookup.Title


class ContactDto {
    
    Integer id
    
    Integer personId
    
    Integer titleId
    
    Title title
    
    String firstName
    
    String surname
    
    String home
    
    String mobile
    
    String work
    
    String email
    
    ContactType contactType
    
    Boolean contactable
    
    Boolean preferred
    
    Address address
    
    ContactDto(){
        contactable = true
        preferred = true
    }
    
    ContactDto(Integer id,Integer personId,Integer titleId, Title title, String firstName, String surname, String home, String mobile, String work, String email, Address address) {
        this()
        this.id = id
        this.personId = personId
        this.titleId = titleId
        this.title = title
        this.firstName = firstName
        this.surname = surname
        this.home = home
        this.mobile = mobile
        this.work = work
        this.email = email
        this.address = address
    }
    
    ContactDto(Contact contact) {
        this.id = contact.id
        this.personId = contact.person.id
        this.titleId = contact.contact.title != null ? contact.contact.title.id : null
        this.title = contact.contact.title
        this.firstName = contact.contact.firstName
        this.surname = contact.contact.surname
        this.home = contact.contact.home
        this.mobile = contact.contact.mobile
        this.work = contact.contact.work
        this.email = contact.contact.email
        this.contactType = contact.contactType
        this.contactable = contact.contactable
        this.preferred = contact.preferred
        this.address = contact.contact.address
    }
    
    String toString() {
        return "id: " + this.id + ", person: " + this.personId + ", title: " + this.titleId + ", title: " + this.title + ", firstName: " + this.firstName +", surname:" + this.surname +", work:" + this.work +", mobile:" + this.mobile +", email:" + this.email
    }
}
