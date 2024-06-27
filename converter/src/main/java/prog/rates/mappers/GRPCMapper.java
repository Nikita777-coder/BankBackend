package prog.rates.mappers;

import com.google.protobuf.ByteString;
import io.swagger.client.model.Currency;
import net.devh.boot.grpc.example.ConverterServiceOuterClass;
import net.devh.boot.grpc.example.WideUsedTypes;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import prog.rates.dto.ConvertCurrencyResponse;
import prog.rates.dto.RatesRequest;

import java.math.BigDecimal;
import java.math.BigInteger;

@Mapper(componentModel = "spring", imports = ConverterServiceOuterClass.ConvertResponse.class)
public interface GRPCMapper {
    @Mapping(target = "amount", expression = "java(mapBDecimalToBigDecimal(response.getAmount()))")
    ConverterServiceOuterClass.ConvertResponse ratesResponseToConvertResponse(ConvertCurrencyResponse response);
    @Mapping(target = "amount", ignore = true)
    RatesRequest convertRequestToRatesRequest(ConverterServiceOuterClass.ConvertRequest request);

    default WideUsedTypes.BDecimal mapBDecimalToBigDecimal(BigDecimal amount) {
        return WideUsedTypes.BDecimal
                .newBuilder()
                .setUnscaledValue(amount.unscaledValue().toString())
                .setScale(amount.scale())
                .build();
    }
    @AfterMapping
    default void mapData(ConverterServiceOuterClass.ConvertRequest source, @MappingTarget RatesRequest target) {
        BigDecimal value = new BigDecimal(new BigInteger(source.getAmount().getUnscaledValue()), source.getAmount().getScale());

        target.setAmount(value);
        target.setCurrencyFrom(Currency.fromValue(source.getFrom()));
        target.setCurrencyTo(Currency.fromValue(source.getTo()));
    }
}
