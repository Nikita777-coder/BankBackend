package prog.rates.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RatesControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
//    @Test
//    void getRateAmount() {
//        ResponseEntity<ProgramRatesResponse> response = restTemplate.getForEntity("/convert?from=USD&to=EUR&amount=200", ProgramRatesResponse.class);
//        assertTrue(HttpStatus.OK == response.getStatusCode() || HttpStatus.INTERNAL_SERVER_ERROR == response.getStatusCode());
//    }
}