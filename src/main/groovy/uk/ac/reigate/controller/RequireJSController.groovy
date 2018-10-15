package uk.ac.reigate.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.webjars.RequireJS

@Controller
class RequireJSController {
    
    @ResponseBody
    @RequestMapping(value = "/webjarsjs", produces = "application/javascript")
    public String webjarjs() {
        return RequireJS.getSetupJavaScript("/webjars/");
    }
}
