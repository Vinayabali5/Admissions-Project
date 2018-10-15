package uk.ac.reigate

import javax.inject.Inject

import org.apache.log4j.Logger
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.system.ApplicationPidFileWriter
import org.springframework.boot.system.EmbeddedServerPortFileWriter
import org.springframework.boot.web.support.SpringBootServletInitializer
import org.springframework.context.annotation.ComponentScan
import org.springframework.core.env.Environment

/**
 * The AdmissionApp is the main launcher for this SpringBoot application.
 *
 * @author Michael Horgan
 *
 */
@SpringBootApplication
@ComponentScan(value="uk.ac.reigate")
class AdmissionsApp extends SpringBootServletInitializer {
    
    private static final Logger LOGGER = Logger.getLogger(AdmissionsApp.class);
    
    @Inject
    private Environment env;
    
    static main(args) {
        SpringApplication ctx = new SpringApplication(AdmissionsApp.class)
        ctx.addListeners(new ApplicationPidFileWriter());
        ctx.addListeners(new EmbeddedServerPortFileWriter());
        Environment env = ctx.run(args).getEnvironment();
        
        LOGGER.info("\n" +
                "----------------------------------------------------------------------------------------------------------\n" +
                "Application '" + env.getProperty("app.name") + "' is running! \n" +
                "----------------------------------------------------------------------------------------------------------\n" +
                "Profile:\n" +
                "Run Profile: \t\t" + env.getProperty("spring.profiles.active") + "\n" +
                "Active Profiles: \t" + env.getActiveProfiles() + "\n" +
                "----------------------------------------------------------------------------------------------------------\n" +
                "Access URLs:\n" +
                "Local: \t\t\t\thttp://127.0.0.1:" + env.getProperty("server.port") + "\n" +
                "External: \t\t\thttp://" + InetAddress.getLocalHost().getHostAddress() + ":" + env.getProperty("server.port") + "\n" +
                "----------------------------------------------------------------------------------------------------------\n" +
                "Database:\n" +
                "Database URL: \t\t" + env.getProperty("spring.datasource.url") + "\n" +
                "Show SQL: \t\t\t" + env.getProperty("spring.jpa.show_sql") + "\n" +
                "----------------------------------------------------------------------------------------------------------"
                )
    }
    
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(applicationClass);
    }
    
    private static Class<AdmissionsApp> applicationClass = AdmissionsApp.class;
}
