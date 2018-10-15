package uk.ac.reigate.controller.rest

import uk.ac.reigate.repositories.ContactRepository

import java.util.List;
import java.util.logging.Logger

import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import uk.ac.reigate.domain.Address
import uk.ac.reigate.dto.AddressDto
import uk.ac.reigate.dto.ContactDto;
import uk.ac.reigate.repositories.AddressRepository
import uk.ac.reigate.util.exception.AddressNotFoundException


@RestController
@RequestMapping(value='/address')
class AddressRestController {
    
    private final static Logger log = Logger.getLogger(AddressRestController.class.getName());
    
    @Autowired
    AddressRepository addressRepository
    
    @RequestMapping(method=RequestMethod.GET)
    List<AddressDto> getAll() {
        log.info("*** AddressRestController.findAll")
        List<Address> address = addressRepository.findAll()
        List<AddressDto> output = new ArrayList<AddressDto>()
        address.each { it ->
            output.add(new AddressDto(it))
        }
        
        return output
    }
    
    // GET /contacts/{id} - Retrieve Contact with id
    @RequestMapping(value='/{id}', method=RequestMethod.GET)
    AddressDto getId(@PathVariable(value='id') Integer addressId){
        Address address = addressRepository.findOne(addressId)
        if (address == null) {
            throw new AddressNotFoundException(addressId.toString())
        }
        return new AddressDto(address)
    }
    
    @RequestMapping(value='getById/{id}', method=RequestMethod.GET, produces='application/json')
    List<AddressDto> getById(@PathVariable(value='id') Integer addressId) {
        log.info("*** AddressRestController.findById")
        List<Address> address = addressRepository.findOne(addressId)
        List<AddressDto> output = new ArrayList<AddressDto>()
        address.each { it ->
            output.add(new AddressDto(it))
        }
        return new AddressDto(address)
    }
}
