package accounts.mappers;

import accounts.dto.converter.ConverterJavaResponse;
import com.google.protobuf.ByteString;
import io.swagger.client.model.Currency;
import net.devh.boot.grpc.example.ConverterServiceOuterClass;
import net.devh.boot.grpc.example.WideUsedTypes;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;
import java.math.BigInteger;

@Mapper(componentModel = "spring", imports = ConverterServiceOuterClass.ConvertRequest.class)
public interface GRPCMapper {
    @Mapping(target = "amount", expression = "java(mapBDecimalToBigDecimal(amount))")
    ConverterServiceOuterClass.ConvertRequest dataToConvertRequest(String from, String to, BigDecimal amount);
    @Mapping(target = "amount", ignore = true)
    @Mapping(target = "currency", ignore = true)
    ConverterJavaResponse convertResponseToConverterJavaResponse(ConverterServiceOuterClass.ConvertResponse response);
    default WideUsedTypes.BDecimal mapBDecimalToBigDecimal(BigDecimal amount) {
        return WideUsedTypes.BDecimal
                .newBuilder()
                .setUnscaledValue(amount.unscaledValue().toString())
                .setScale(amount.scale())
                .build();
    }
    @AfterMapping
    default void mapBDecimalToBigDecimal(ConverterServiceOuterClass.ConvertResponse source, @MappingTarget ConverterJavaResponse target) {
        BigDecimal value = new BigDecimal(new BigInteger(source.getAmount().getUnscaledValue()), source.getAmount().getScale());

        target.setAmount(value);
        target.setCurrency(Currency.fromValue(source.getCurrency().name()));
    }
}
