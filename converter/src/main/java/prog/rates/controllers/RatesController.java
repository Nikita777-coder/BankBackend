package prog.rates.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prog.rates.dto.ConvertCurrencyResponse;
import prog.rates.mappers.RatesMapper;
import prog.rates.services.RatesService;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class RatesController {
    private final RatesService ratesService;
    private final RatesMapper ratesMapper;
    @GetMapping("/convert")
    public ResponseEntity<ConvertCurrencyResponse> getRateAmount(@RequestParam String from,
                                                                 @RequestParam String to,
                                                                 @RequestParam BigDecimal amount) {
        return new ResponseEntity<>(ratesService.convert(ratesMapper.dataToRatesRequest(from, to, amount)), HttpStatus.OK);
    }
}
