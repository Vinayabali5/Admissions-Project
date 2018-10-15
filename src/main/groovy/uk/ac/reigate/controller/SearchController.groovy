package uk.ac.reigate.controller

import java.util.logging.Logger;

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping


@Controller
class SearchController {
    
    private final static Logger LOGGER = Logger.getLogger(SearchController.class.getName());
    
    /**
     * This methods is used to load the application search feature.
     *
     * @return returns the view admissions/search
     */
    @RequestMapping('/application/search')
    String search() {
        LOGGER.info("** Return view: admissions/search")
        return 'admissions/search'
    }
}
