package uk.ac.reigate.dto

import javax.validation.constraints.NotNull

import org.springframework.format.annotation.DateTimeFormat

/**
 * The DuplicateDetectionDto is used to send data back and forth between the client and the server during the creation of a new Application
 * to detect any potential duplicate applications or potential twins.
 *
 */
class DuplicateDetectionDto {
    
    //#TODO May not be needed
    Integer studentId
    
    //#TODO May not be needed
    Integer personId
    
    String firstName
    
    @NotNull
    String surname
    
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @NotNull
    Date dob
    
    /**
     * The matches list is used to store the matches found by the DuplicateDetectionRestController.
     */
    List<DuplicateApplicationDto> matches
    
    DuplicateDetectionDto(){}
    
    DuplicateDetectionDto(DuplicateDetectionDto that){
        this.studentId = that.studentId
        this.personId = that.personId
        this.firstName = that.firstName
        this.surname = that.surname
        this.dob = that.dob
        this.matches = that.matches
    }
    
    DuplicateDetectionDto(Integer studentId, personId, String firstName, String surname, Date dob){
        this.studentId = studentId
        this.personId = personId
        this.firstName = firstName
        this.surname = surname
        this.dob = dob
    }
    
    String toString() {
        return "studentId: " + this.studentId + ", personId:" + this.personId + ", firstName: " + this.firstName + ", surname:" + this.surname+ ", dob: " + this.dob
    }
}
