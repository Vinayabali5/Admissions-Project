package uk.ac.reigate.service.admissions;

import java.util.logging.Logger

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

import uk.ac.reigate.domain.Address
import uk.ac.reigate.domain.lookup.PostcodeLookup
import uk.ac.reigate.repositories.AddressRepository


@Component
@Service
class PostcodeLookupService {
    
    private final static Logger log = Logger.getLogger(PostcodeLookupService.class.getName());
    
    @Autowired
    AddressRepository addressRepository
    
    
    @Value("\${postcode.lookup.key}")
    private String key;
    
    @Value("\${postcode.lookup.username}")
    private String username;
    
    /**
     * This method is code provided by the Postcode Anywhere service. You can find all the information needed and references
     * on the APIs web site: http://www.pcapredict.com/support/webservice/postcodeanywhere/interactive/retrievebyid/1.3/
     *
     * @param Key The API key provided by the Postcode Anywhere administrator.
     * @param SearchTerm The search term that you what to lookup.
     * @param PreferredLanguage The preferred language to use, see the API documentation for options..
     * @param Filter The filter you want to apply, see the API documentation for options.
     * @param UserName The username of the account for the Postcode Anywhere service
     * @return a Hashtable with the data from the API.
     * @throws Exception
     */
    private Hashtable[] PostcodeAnywhere_Interactive_Find_v1_10(String Key, String SearchTerm, String PreferredLanguage, String Filter, String UserName) throws Exception {
        
        String requestUrl = new String();
        String key = new String();
        String value = new String();
        
        //Build the url
        requestUrl = "http://services.postcodeanywhere.co.uk/PostcodeAnywhere/Interactive/Find/v1.10/xmla.ws?";
        requestUrl += "&Key=" + java.net.URLEncoder.encode(Key);
        requestUrl += "&SearchTerm=" + java.net.URLEncoder.encode(SearchTerm);
        requestUrl += "&PreferredLanguage=" + java.net.URLEncoder.encode(PreferredLanguage);
        requestUrl += "&Filter=" + java.net.URLEncoder.encode(Filter);
        requestUrl += "&UserName=" + java.net.URLEncoder.encode(UserName);
        
        //Get the data
        java.net.URL url = new java.net.URL(requestUrl);
        java.io.InputStream stream = url.openStream();
        javax.xml.parsers.DocumentBuilder docBuilder = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder();
        org.w3c.dom.Document dataDoc = docBuilder.parse(stream);
        
        //Get references to the schema and data
        org.w3c.dom.NodeList schemaNodes = dataDoc.getElementsByTagName("Column");
        org.w3c.dom.NodeList dataNotes = dataDoc.getElementsByTagName("Row");
        
        //Check for an error
        if (schemaNodes.getLength()==4 && schemaNodes.item(0).getAttributes().getNamedItem("Name").getNodeValue().equals("Error"))
        {
            throw new Exception(dataNotes.item(0).getAttributes().getNamedItem("Description").getNodeValue());
        };
        
        //Work though the items in the response
        java.util.Hashtable[] results = new java.util.Hashtable[dataNotes.getLength()];
        for (int rowCounter=0; rowCounter<dataNotes.getLength(); rowCounter++)
        {
            java.util.Hashtable rowData = new java.util.Hashtable();
            for (int colCounter=0; colCounter<schemaNodes.getLength(); colCounter++)
            {
                key = (String)schemaNodes.item(colCounter).getAttributes().getNamedItem("Name").getNodeValue();
                if(dataNotes.item(rowCounter).getAttributes().getNamedItem(key)==null)
                {
                    value="";
                }
                else
                {
                    value = (String)dataNotes.item(rowCounter).getAttributes().getNamedItem(key).getNodeValue();
                };
                rowData.put (key, value);
            }
            results[rowCounter] = rowData;
        }
        
        //Return the results
        return results;
    }
    
    /**
     * This method is code provided by the Postcode Anywhere service. You can find all the information needed and references
     * on the APIs web site: http://www.pcapredict.com/support/webservice/postcodeanywhere/interactive/retrievebyid/1.3/
     *
     * @param Key The API key provided by the Postcode Anywhere administrator.
     * @param Id The ID for the address to retrieve.
     * @param PreferredLanguage The preferred language to use, see the API documentation for options..
     * @param UserName The username of the account for the Postcode Anywhere service
     * @return a Hashtable with the data from the API.
     * @throws Exception
     */
    private Hashtable[] PostcodeAnywhere_Interactive_RetrieveById_v1_30(String Key, String Id, String PreferredLanguage, String UserName) throws Exception {
        
        String requestUrl = new String();
        String key = new String();
        String value = new String();
        
        //Build the url
        requestUrl = "http://services.postcodeanywhere.co.uk/PostcodeAnywhere/Interactive/RetrieveById/v1.30/xmla.ws?";
        requestUrl += "&Key=" + java.net.URLEncoder.encode(Key);
        requestUrl += "&Id=" + java.net.URLEncoder.encode(Id);
        requestUrl += "&PreferredLanguage=" + java.net.URLEncoder.encode(PreferredLanguage);
        requestUrl += "&UserName=" + java.net.URLEncoder.encode(UserName);
        
        //Get the data
        java.net.URL url = new java.net.URL(requestUrl);
        java.io.InputStream stream = url.openStream();
        javax.xml.parsers.DocumentBuilder docBuilder = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder();
        org.w3c.dom.Document dataDoc = docBuilder.parse(stream);
        
        //Get references to the schema and data
        org.w3c.dom.NodeList schemaNodes = dataDoc.getElementsByTagName("Column");
        org.w3c.dom.NodeList dataNotes = dataDoc.getElementsByTagName("Row");
        
        //Check for an error
        if (schemaNodes.getLength()==4 && schemaNodes.item(0).getAttributes().getNamedItem("Name").getNodeValue().equals("Error"))
        {
            throw new Exception(dataNotes.item(0).getAttributes().getNamedItem("Description").getNodeValue());
        };
        
        //Work though the items in the response
        java.util.Hashtable[] results = new java.util.Hashtable[dataNotes.getLength()];
        for (int rowCounter=0; rowCounter<dataNotes.getLength(); rowCounter++)
        {
            java.util.Hashtable rowData = new java.util.Hashtable();
            for (int colCounter=0; colCounter<schemaNodes.getLength(); colCounter++)
            {
                key = (String)schemaNodes.item(colCounter).getAttributes().getNamedItem("Name").getNodeValue();
                if(dataNotes.item(rowCounter).getAttributes().getNamedItem(key)==null)
                {
                    value="";
                }
                else
                {
                    value = (String)dataNotes.item(rowCounter).getAttributes().getNamedItem(key).getNodeValue();
                };
                rowData.put (key, value);
            }
            results[rowCounter] = rowData;
        }
        
        //Return the results
        return results;
    }
    
    /**
     * This method is code provided by the Postcode Anywhere service. You can find all the information needed and references
     * on the APIs web site: http://www.pcapredict.com/Support/WebService/PostcodeAnywhere/Interactive/FindByPostcode/
     *
     * @param Key The API key provided by the Postcode Anywhere administrator.
     * @param Postcode The postcode that you what to lookup.
     * @param UserName The username of the account for the Postcode Anywhere service
     * @return a Hashtable with the data from the API.
     * @throws Exception
     */
    private  Hashtable[] PostcodeAnywhere_Interactive_FindByPostcode_v1_00(String Key, String Postcode, String UserName) throws Exception
    {
        
        String requestUrl = new String();
        String key = new String();
        String value = new String();
        
        //Build the url
        requestUrl = "http://services.postcodeanywhere.co.uk/PostcodeAnywhere/Interactive/FindByPostcode/v1.00/xmla.ws?";
        requestUrl += "&Key=" + java.net.URLEncoder.encode(Key);
        requestUrl += "&Postcode=" + java.net.URLEncoder.encode(Postcode);
        requestUrl += "&UserName=" + java.net.URLEncoder.encode(UserName);
        
        //Get the data
        java.net.URL url = new java.net.URL(requestUrl);
        java.io.InputStream stream = url.openStream();
        javax.xml.parsers.DocumentBuilder docBuilder = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder();
        org.w3c.dom.Document dataDoc = docBuilder.parse(stream);
        
        //Get references to the schema and data
        org.w3c.dom.NodeList schemaNodes = dataDoc.getElementsByTagName("Column");
        org.w3c.dom.NodeList dataNotes = dataDoc.getElementsByTagName("Row");
        
        //Check for an error
        if (schemaNodes.getLength()==4 && schemaNodes.item(0).getAttributes().getNamedItem("Name").getNodeValue().equals("Error"))
        {
            throw new Exception(dataNotes.item(0).getAttributes().getNamedItem("Description").getNodeValue());
        };
        
        //Work though the items in the response
        java.util.Hashtable[] results = new java.util.Hashtable[dataNotes.getLength()];
        for (int rowCounter=0; rowCounter<dataNotes.getLength(); rowCounter++)
        {
            java.util.Hashtable rowData = new java.util.Hashtable();
            for (int colCounter=0; colCounter<schemaNodes.getLength(); colCounter++)
            {
                key = (String)schemaNodes.item(colCounter).getAttributes().getNamedItem("Name").getNodeValue();
                if(dataNotes.item(rowCounter).getAttributes().getNamedItem(key)==null)
                {
                    value="";
                }
                else
                {
                    value = (String)dataNotes.item(rowCounter).getAttributes().getNamedItem(key).getNodeValue();
                };
                rowData.put (key, value);
            }
            results[rowCounter] = rowData;
        }
        
        //Return the results
        return results;
    }
    
    /**
     * This method is used to retrieve a list of address id and brief descriptive information for the address to then
     * be selected and retrieved using the retrieve method.
     *
     * @param postcode The postcode that you want to use to search for addresses.
     * @return A List of PostcodeLookupDto objects, with an id field used for retrieving address.
     */
    List<PostcodeLookup> search(String postcode) {
        log.info("*** PostcodeLookupService.search ");
        List<PostcodeLookup> output = new ArrayList<PostcodeLookup>();
        Hashtable[] results = null;
        try {
            results = this.PostcodeAnywhere_Interactive_Find_v1_10(this.key, postcode, "English", "None", this.username);
            results.each { it ->
                PostcodeLookup item = new PostcodeLookup()
                item.id = it.get("Id")
                item.streetAddress = it.get("StreetAddress")
                item.place =  it.get("Place")
                output.add(item)
            }
        } catch (UnknownHostException e) {
            throw e;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return output;
    }
    
    /**
     * This method is used to retrieve a full address for a supplied ID. The ID is found by using the search method.
     *
     * @param id The ID for the address to retrieve
     * @return An Address object populated with the data from the Postcode Lookup Service.
     */
    Address retrieve(String id){
        log.info("*** PostcodeLookupService.getById");
        Address output = null
        try {
            Hashtable[] results = this.PostcodeAnywhere_Interactive_RetrieveById_v1_30(this.key, id, "English", this.username);
            output = new Address()
            
            output.line1 = results[0].get("Line1")
            output.line2 = results[0].get("Line2")
            output.line3 = results[0].get("Line3")
            output.line4 = results[0].get("Line4")
            output.line5 = results[0].get("Line5")
            output.buildingName = results[0].get("BuildingName")
            output.subBuilding = results[0].get("SubBuilding")
            output.town = results[0].get("PostTown")
            output.county = results[0].get("County")
            output.postcode = results[0].get("Postcode")
            log.info("*** PostcodeLookupService.getById123");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            
        }
        return output;
    }
    
    public void add(final Address address){
        this.addressRepository.add(address);
        
    }
    
    
}
