package uk.ac.reigate.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping


@Controller
class SiteController {
    
    @RequestMapping('/')
    String homepage() {
        return 'pages/home'
    }
    
    @RequestMapping('/help')
    String help(Model model) {
        model.addAttribute('helpPage', new String('Hello this is the help page.'))
        return 'pages/help'
    }
    
    @RequestMapping('/reports')
    String reports(Model model) {
        return 'pages/reports'
    }
    
    @RequestMapping('/page/{pageName}')
    String page(@PathVariable String pageName, Model model) {
        // #TODO: add a web page handler that will take a page name and search for the content from the database
    }
}
