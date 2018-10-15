package uk.ac.reigate.dto

import uk.ac.reigate.domain.Person;

class DuplicateApplicationDto {
    
    Integer studentId
    
    Person person
    
    public DuplicateApplicationDto(Integer studentId, Person person) {
        super();
        this.studentId = studentId;
        this.person = person;
    }
}
