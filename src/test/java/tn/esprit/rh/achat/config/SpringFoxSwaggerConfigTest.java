package tn.esprit.rh.achat.config;

import org.junit.jupiter.api.Test;
import springfox.documentation.spring.web.plugins.Docket;
import tn.esprit.rh.achat.util.SpringFoxSwaggerConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SpringFoxSwaggerConfigTest {

    @Test
    void testDocketCreation() {
        SpringFoxSwaggerConfig config = new SpringFoxSwaggerConfig();
        Docket docket = config.api();
        assertNotNull(docket);
    }
}
