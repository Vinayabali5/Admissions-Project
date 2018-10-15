package uk.ac.reigate.dto

import javax.validation.constraints.NotNull

import uk.ac.reigate.domain.admissions.Request

class RequestDto {
    
    Integer id
    
    Integer studentId
    
    Integer academicYearId
    
    String _academicYearCode
    
    @NotNull
    String request
    
    String description
    
    RequestDto() {}
    
    RequestDto(Integer id, Integer studentId, String request) {
        this()
        this.id = id
        this.studentId = studentId
        this.request = request
    }
    
    RequestDto(Request request) {
        this.id = request.id
        this.studentId = request.student.id
        this.request = request.request
        this.academicYearId =  request.academicYear.id
        this._academicYearCode = request.academicYear != null ? request.academicYear.code : ''
    }
    
    String toString() {
        return "id: " + this.id + ", studentId: " + this.studentId + ", request: " + this.request +",  academicYearId: " + this.academicYearId
    }
}
