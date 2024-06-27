package accounts.services;

import accounts.dto.converter.ConverterJavaResponse;
import accounts.mappers.GRPCMapper;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.example.ConverterServiceGrpc;
import net.devh.boot.grpc.example.ConverterServiceOuterClass;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GRPCService {
    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final GRPCMapper grpcMapper;
    @GrpcClient("converterService")
    private ConverterServiceGrpc.ConverterServiceBlockingStub grpcConverterServiceStub;
    public ConverterJavaResponse convert(ConverterServiceOuterClass.ConvertRequest convertRequest) {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("convert-service");

        try {
            return circuitBreaker.executeCallable(() -> {
                ConverterServiceOuterClass.ConvertResponse convertResponse = grpcConverterServiceStub.convert(convertRequest);
                return grpcMapper.convertResponseToConverterJavaResponse(convertResponse);
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
