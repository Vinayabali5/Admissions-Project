package uk.ac.reigate.dto

import uk.ac.reigate.domain.Address

class AddressDto {
    
    Integer id
    
    String line1
    
    String line2
    
    String line3
    
    String line4
    
    String line5
    
    String town
    
    String county
    
    String postcode
    
    String buildingName
    
    String subBuilding
    
    String udprn
    
    AddressDto(){}
    
    AddressDto (Integer id, String line1, String line2, String line3, String line4,  String line5, String town, String county, String postcode,String buildingName, String subBuilding) {
        this.id= id
        this.line1 = line1
        this.line2 = line2
        this.line3 = line3
        this.line4 = line4
        this.line5 = line5
        this.town = town
        this.buildingName = buildingName
        this.subBuilding = subBuilding
        this.county = county
        this.postcode = postcode
    }
    
    AddressDto(Address address){
        this.id = address.id
        this.line1 = address.line1
        this.line2 = address.line2
        this.line3 = address.line3
        this.line4 = address.line4
        this.line5 = address.line5
        this.town = address.town
        this.buildingName = address.buildingName
        this.subBuilding = address.subBuilding
        this.county = address.county
        this.postcode = address.postcode
        this.udprn = address.udprn
    }
    
    String toString() {
        return "id: " + this.id + ", line1: " + this.line1 + ", line2: " + this.line2 + ", line3: " + this.line3 + ", line4: " + this.line4 +", town:" + this.town +", county:" + this.county +", postcode:" + this.postcode +", buildingName:" + this.buildingName
    }
}
