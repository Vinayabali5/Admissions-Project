package uk.ac.reigate.controller

import java.util.logging.Logger

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

import uk.ac.reigate.domain.Address
import uk.ac.reigate.domain.Contact
import uk.ac.reigate.domain.academic.Student
import uk.ac.reigate.repositories.AddressRepository
import uk.ac.reigate.repositories.academic.StudentRepository
import uk.ac.reigate.service.admissions.StudentAdmissionsService
import uk.ac.reigate.service.admissions.WordGeneratorService


@Controller
@RequestMapping(value='/wordGenerator')
class WordGeneratorController {
    
    private final static Logger log = Logger.getLogger(WordGeneratorController.class.getName());
    
    @Autowired
    AddressRepository addressRepository
    
    @Autowired
    StudentRepository studentRepository
    
    @Autowired
    WordGeneratorService wordGeneratorService
    
    @Autowired
    StudentAdmissionsService studentService
    
    
    @RequestMapping (value="studentLetter/{id}", method=RequestMethod.GET)
    void getById(@PathVariable(value='id') Integer studentId,HttpServletRequest request, HttpServletResponse response) {
        log.info("*** WordGeneratorController.get")
        
        // retrieve application
        Student app = studentRepository.findOne(studentId)
        Address address = app.person.address
        String addressee = app.person.firstName + ' ' + app.person.surname
        String salutation = app.person.firstName
        
        //Microsoft office extension MIME type for .docx
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        
        OutputStream outputStream = null;
        XWPFDocument document = wordGeneratorService.generateLetter(address, addressee, salutation, new Date());
        outputStream = response.getOutputStream();
        document.write(outputStream);
    }
    
    @RequestMapping (value="parentLetter/{id}", method=RequestMethod.GET)
    void getByParentId(@PathVariable(value='id') Integer studentId, HttpServletRequest request, HttpServletResponse response) {
        log.info("*** WordGeneratorController.get")
        
        // Retrieve student information
        Student app = studentRepository.findOne(studentId)
        
        if (app != null && app.person != null) {
            
            List<Contact> contactList = new ArrayList<Contact>()
            List<Contact> contactAltAddressList = new ArrayList<Contact>()
            
            // Get contacts that are contactable and preferred
            app.person.contacts.each {
                if (it.contactable && it.preferred) {
                    if (it.contact.address == null) {
                        contactList.add(it)
                    } else {
                        contactAltAddressList.add(it)
                    }
                }
            }
            
            Address address = null
            String addressee = null
            
            // Extract Addressee from the available contacts
            if (contactList.size != 0) {
                int size = contactList.size()
                switch (size) {
                    case 1:
                        Contact contact = contactList[0]
                        address = contact.person.address != null ? contact.person.address : app.person.address
                        addressee = contact.contact.title.toString() + ' ' + contact.contact.surname
                        break;
                    case 2:
                        Contact father = null
                        Contact mother = null
                        Contact guardian1 = null
                        address = app.person.address
                        for (Contact contact in contactList) {
                            if (contact.contactType.name == 'Mother' && mother == null) {
                                mother = contact
                            } else if (contact.contactType.name == 'Father' && father == null) {
                                father = contact
                            } else if (contact.contactType.name == 'Guardian' && guardian1 == null) {
                                guardian1 = contact
                            }
                        }
                        String surname = ''
                        Contact contact1 = null
                        Contact contact2 = null
                        if (guardian1 != null) {
                            // Check if Guardian provided. If so use this over everything else.
                            contact1 = guardian1
                            addressee = contact1.contact.title.toString() + ' ' + contact1.contact.surname
                        } else {
                            // If no Guardian then use Father and Mother details
                            if (father != null) {
                                contact1 = father
                            }
                            if (mother != null) {
                                contact2 = mother
                            }
                            if (contact1.contact.surname == contact2.contact.surname) {
                                addressee = contact1.contact.title.toString() + ' & ' + contact2.contact.title.toString() + ' ' + contact1.contact.surname
                            } else {
                                addressee = contact1.contact.title.toString() + ' ' + contact1.contact.surname + ' & ' + contact2.contact.title.toString() + ' ' + contact2.contact.surname
                            }
                        }
                }
            }
            
            // Extract address from the Students address details
            if (address == null) {
                address = app.person.address
            }
            // Set generic addressee if no other addressee is set
            if (addressee == null) {
                addressee = 'Parents/Guardians of ' + app.person.firstName + ' ' + app.person.surname
            }
            
            //Microsoft office extension MIME type for .docx
            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            
            OutputStream outputStream = null;
            XWPFDocument document = wordGeneratorService.generateLetter(address, addressee, new Date());
            outputStream = response.getOutputStream();
            document.write(outputStream);
        } else {
            throw ApplicationNotFoundException()
        }
    }
}
