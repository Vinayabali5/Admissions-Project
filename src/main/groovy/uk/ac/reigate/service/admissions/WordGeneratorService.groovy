package uk.ac.reigate.service.admissions

import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFParagraph
import org.apache.poi.xwpf.usermodel.XWPFRun
import org.springframework.stereotype.Service

import uk.ac.reigate.domain.Address

@Service
class WordGeneratorService {
    
    XWPFDocument generateLetter(String address, String addressee, String salutation, Date date, String reference) {
        XWPFDocument document = new XWPFDocument();
        
        XWPFParagraph paragraph1 = document.createParagraph();
        XWPFRun dateBlock = paragraph1.createRun();
        dateBlock.setText(date.format('dd MMMM yyyy'));
        
        XWPFParagraph paragraph2 = document.createParagraph();
        
        XWPFRun addressBlock = paragraph2.createRun();
        addressBlock.setText(addressee);
        addressBlock.addBreak();
        String[] addressParts = address.replace('"', '').split("\r\n")
        for (String addressLine : addressParts) {
            addressBlock.setText(addressLine);
            addressBlock.addBreak();
        }
        addressBlock.removeBreak();
        
        XWPFParagraph paragraph3 = document.createParagraph();
        XWPFRun salutationBlock = paragraph3.createRun();
        salutationBlock.setText("Dear " + salutation);
        salutationBlock.addBreak();
        
        if (reference != null) {
            XWPFParagraph paragraph4 = document.createParagraph();
            XWPFRun referenceBlock = paragraph3.createRun();
            
            referenceBlock.setText(reference);
            referenceBlock.setBold(true);
        }
        
        return document;
    }
    /*
     XWPFDocument generateLetter(Address address, String addressee, String salutation, Date date, String reference) {
     XWPFDocument document = new XWPFDocument();
     XWPFParagraph paragraph1 = document.createParagraph();
     XWPFRun dateBlock = paragraph1.createRun();
     dateBlock.setText("Date: " + date.format('dd/MM/yyyy'));
     XWPFParagraph paragraph2 = document.createParagraph();
     XWPFRun addressBlock = paragraph2.createRun();
     addressBlock.setText(addressee);
     addressBlock.addBreak();
     if (address.line1 != null) {
     addressBlock.setText(address.line1);
     addressBlock.addBreak();
     }
     if (address.line2 != null && !address.line3.equals("")) {
     addressBlock.setText(address.line2);
     addressBlock.addBreak();
     }
     if (address.line3 != null&& !address.line3.equals("")) {
     addressBlock.setText(address.line3);
     addressBlock.addBreak();
     }
     if (address.line4 != null && !address.line4.equals("")) {
     addressBlock.setText(address.line4);
     addressBlock.addBreak();
     }
     if (address.line5 != null && !address.line5.equals("")) {
     addressBlock.setText(address.line5);
     addressBlock.addBreak();
     }
     if (address.town != null && !address.town.equals("")) {
     addressBlock.setText(address.town);
     addressBlock.addBreak();
     }
     if (address.county != null && !address.county.equals("")) {
     addressBlock.setText(address.county);
     addressBlock.addBreak();
     }
     if (address.postcode != null) {
     addressBlock.setText(address.postcode);
     addressBlock.addBreak();
     }
     addressBlock.removeBreak();
     XWPFParagraph paragraph3 = document.createParagraph();
     XWPFRun addresseeBlock = paragraph3.createRun();
     addresseeBlock.setText("Dear " + salutation);
     if (reference != null) {
     XWPFParagraph paragraph4 = document.createParagraph();
     XWPFRun referenceBlock = paragraph3.createRun();
     referenceBlock.setText(reference);
     referenceBlock.setBold(true);
     }
     return document;
     }
     */
    XWPFDocument generateLetter(String address, String addressee, String salutation, Date date) {
        return generateLetter(address, addressee, salutation, date, null)
    }
    
    XWPFDocument generateLetter(String address, String addressee, Date date) {
        return generateLetter(address, addressee, addressee, date)
    }
    
    XWPFDocument generateLetter(Address address, String addressee, String salutation, Date date, String reference) {
        return generateLetter(address.fullAddress, addressee, salutation, date, reference)
    }
    
    XWPFDocument generateLetter(Address address, String addressee, String salutation, Date date) {
        return generateLetter(address, addressee, salutation, date, null)
    }
    
    XWPFDocument generateLetter(Address address, String addressee, Date date) {
        return generateLetter(address, addressee, addressee, date)
    }
}
