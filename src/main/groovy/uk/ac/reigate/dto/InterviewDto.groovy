package uk.ac.reigate.dto

import uk.ac.reigate.domain.academic.School
import uk.ac.reigate.domain.academic.Student
import uk.ac.reigate.domain.academic.StudentYear

import com.fasterxml.jackson.annotation.JsonFormat


class InterviewDto {
    
    String studentRef
    
    Integer studentId
    
    Integer personId
    
    String surname
    
    String firstName
    
    String middleNames
    
    @JsonFormat(pattern='dd/MM/yyyy')
    Date dob
    
    School school
    
    Integer interviewerId
    
    @JsonFormat(pattern='dd/MM/yyyy')
    Date interviewDate
    
    Integer offerTypeId
    
    Integer studentTypeId
    
    InterviewDto() {}
    
    InterviewDto(Student student, StudentYear studentYear){
        this.studentRef = student.referenceNo
        this.studentId = student.id
        this.surname = student.person.surname
        this.firstName = student.person.firstName
        this.middleNames = student.person.middleNames
        this.dob = student.person.dob
        this.school = student.school
        this.interviewerId = student.interviewer != null ? student.interviewer.id : null
        this.interviewDate = student.interviewDate
        this.offerTypeId = student.offerType != null ? student.offerType.id : null
        this.studentTypeId = studentYear.studentType != null ? studentYear.studentType.id : null
    }
    
    @Override
    public String toString() {
        return "InterviewDto [studentRef=" + studentRef + ", studentId=" + studentId + ", personId=" + personId + ", surname=" + surname + ", firstName=" + firstName + ", middleNames=" + middleNames + ", dob=" + dob + ", school=" + school + ", interviewerId=" + interviewerId + ", interviewDate=" + interviewDate + ", offerTypeId=" + offerTypeId + ", studentTypeId=" + studentTypeId + "]";
    }
}
