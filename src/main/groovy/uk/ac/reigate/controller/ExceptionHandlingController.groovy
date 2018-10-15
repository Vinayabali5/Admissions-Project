package uk.ac.reigate.controller

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.ModelAndView

@ControllerAdvice
public class ExceptionHandlingController {
    
    private final static Logger LOGGER = Logger.getLogger(ExceptionHandlingController.class.getName());
    
    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest req, Exception exception) {
        LOGGER.error("Request: " + req.getRequestURL() + " raised " + exception);
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", exception);
        mav.addObject("message", exception.getMessage());
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error");
        return mav;
    }
}
