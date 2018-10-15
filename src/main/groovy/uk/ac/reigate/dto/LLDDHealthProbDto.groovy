package uk.ac.reigate.dto

import javax.validation.constraints.NotNull

/**
 * Used to add LLDD Health Problems to an application
 *
 */
class LLDDHealthProbDto {
    
    @NotNull
    Integer studentId
    
    @NotNull
    Integer llddHealthProbId
    
    String llddDescription
}
