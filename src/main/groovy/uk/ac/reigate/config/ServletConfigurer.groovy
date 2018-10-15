package uk.ac.reigate.config

import javax.inject.Inject
import javax.servlet.DispatcherType
import javax.servlet.FilterRegistration
import javax.servlet.ServletContext
import javax.servlet.ServletException

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer
import org.springframework.boot.context.embedded.MimeMappings
import org.springframework.boot.web.servlet.ServletContextInitializer
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

import uk.ac.reigate.web.filter.CachingHttpHeadersFilter

@Configuration
//@AutoConfigureAfter(CacheConfiguration.class)
class ServletConfigurer implements ServletContextInitializer, EmbeddedServletContainerCustomizer {
    
    private final Logger log = LoggerFactory.getLogger(ServletConfigurer.class);
    
    @Inject
    private Environment env;
    
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        log.info("Web application configuration, using profiles: {}", Arrays.toString(env.getActiveProfiles()));
        EnumSet<DispatcherType> disps = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
        if (env.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION) || env.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION_RC) || env.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION_CC)) {
            //	    initCachingHttpHeadersFilter(servletContext, disps);
            //	    initStaticResourcesProductionFilter(servletContext, disps);
            //	    initGzipFilter(servletContext, disps);
        }
        //        if (env.acceptsProfiles(Constants.SPRING_PROFILE_DEVELOPMENT)) {
        //            initH2Console(servletContext);
        //        }
        log.info("Web application fully configured");
    }
    
    /**
     * Set up Mime types.
     */
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
        // IE issue, see https://github.com/jhipster/generator-jhipster/pull/711
        mappings.add("html", "text/html;charset=utf-8");
        // CloudFoundry issue, see https://github.com/cloudfoundry/gorouter/issues/64
        mappings.add("json", "text/html;charset=utf-8");
        container.setMimeMappings(mappings);
    }
    
    /**
     * Initializes the caching HTTP Headers Filter.
     */
    private void initCachingHttpHeadersFilter(ServletContext servletContext,
            EnumSet<DispatcherType> disps) {
        log.debug("Registering Caching HTTP Headers Filter");
        FilterRegistration.Dynamic cachingHttpHeadersFilter =
                servletContext.addFilter("cachingHttpHeadersFilter",
                new CachingHttpHeadersFilter(env));
        
        //cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/assets/*");
        //cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/scripts/*");
        cachingHttpHeadersFilter.setAsyncSupported(true);
    }
    
    //    /**
    //     * Initializes H2 console
    //     */
    //    private void initH2Console(ServletContext servletContext) {
    //        log.debug("Initialize H2 console");
    //        ServletRegistration.Dynamic h2ConsoleServlet = servletContext.addServlet("H2Console", new org.h2.server.web.WebServlet());
    //        h2ConsoleServlet.addMapping("/console/*");
    //        h2ConsoleServlet.setInitParameter("-properties", "src/main/resources");
    //        h2ConsoleServlet.setLoadOnStartup(1);
    //    }
    
}
