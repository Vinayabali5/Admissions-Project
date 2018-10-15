package uk.ac.reigate.dto



class StudentSearchDto {
    
    Integer studentId
    
    String referenceNo
    
    String personName
    
    String schoolName
    
    String status
    
    /**
     * Default NoArgs constructor
     */
    StudentSearchDto() {}
    
    /**
     * StudentSearchDto constructor used by the a search controller to display the basic information
     *
     * @param studentId the student's ID
     * @param studentRef the student's reference
     * @param personName the student's name
     * @param schoolName the student's school name
     * @param status the students application status text
     */
    StudentSearchDto(Integer studentId, String referenceNo, String personName, String schoolName, String status) {
        super();
        this.studentId = studentId;
        this.referenceNo = referenceNo;
        this.personName = personName;
        this.schoolName = schoolName;
        this.status = status;
    }
}
