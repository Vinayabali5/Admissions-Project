package uk.ac.reigate.dto

import com.fasterxml.jackson.annotation.JsonFormat

import uk.ac.reigate.domain.admissions.CollegeFundPayment

/**
 * Used to add CollegeFundPayment to a student
 *
 */
class CollegeFundPaymentDto {
    
    Integer id
    
    Integer studentId
    
    @JsonFormat(pattern='dd/MM/yyyy')
    Date paymentDate
    
    float amount
    
    String payee
    
    boolean giftAid
    
    boolean cash
    
    @JsonFormat(pattern='dd/MM/yyyy')
    Date chequeDate
    
    CollegeFundPaymentDto(){
        giftAid = true
        cash = true
    }
    
    CollegeFundPaymentDto(Integer id, Integer studentId, Date paymentDate, float amount, String payee, boolean giftAid, boolean cash, Date chequeDate) {
        this()
        this.id = id
        this.studentId = studentId
        this.paymentDate = paymentDate
        this.amount = amount
        this.payee = payee
        this.giftAid = giftAid
        this.cash = cash
        this.chequeDate = chequeDate
    }
    
    CollegeFundPaymentDto(CollegeFundPaymentDto collegeFundPaymentDto) {
        this()
        this.id = collegeFundPaymentDto.id
        this.studentId = collegeFundPaymentDto.studentId
        this.paymentDate =collegeFundPaymentDto. paymentDate
        this.amount = collegeFundPaymentDto.amount
        this.payee = collegeFundPaymentDto.payee
        this.giftAid = collegeFundPaymentDto.giftAid
        this.cash = collegeFundPaymentDto.cash
        this.chequeDate = collegeFundPaymentDto.chequeDate
    }
    
    CollegeFundPaymentDto(CollegeFundPayment collegeFundPayment){
        this.id = collegeFundPayment.id
        this.studentId = collegeFundPayment.studentId
        this.paymentDate = collegeFundPayment.paymentDate
        this.amount = collegeFundPayment.amount
        this.payee = collegeFundPayment.payee
        this.giftAid = collegeFundPayment.giftAid
        this.cash = collegeFundPayment.cash
        this.chequeDate = collegeFundPayment.chequeDate
    }
    
    String toString() {
        return "id: " + this.id + ", studentId: " + this.studentId + ", paymentDate: " + this.paymentDate+ ", amount: " + this.amount+ ", payee: " + this.payee+ ", giftAid: " + this.giftAid+ ", cash: " + this.cash+ ", chequeDate: " + this.chequeDate
    }
}
