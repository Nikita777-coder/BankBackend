package prog.rates.services;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.example.ConverterServiceGrpc;
import net.devh.boot.grpc.example.ConverterServiceOuterClass;
import net.devh.boot.grpc.server.service.GrpcService;
import prog.rates.dto.RatesRequest;
import prog.rates.mappers.GRPCMapper;

@GrpcService
@RequiredArgsConstructor
public class RatesGRPCService extends ConverterServiceGrpc.ConverterServiceImplBase {
    private final RatesService ratesService;
    private final GRPCMapper grpcMapper;
    @Override
    public void convert(ConverterServiceOuterClass.ConvertRequest convertRequest, StreamObserver<ConverterServiceOuterClass.ConvertResponse> convertResponse) {
        RatesRequest request = grpcMapper.convertRequestToRatesRequest(convertRequest);
        ConverterServiceOuterClass.ConvertResponse response = grpcMapper.ratesResponseToConvertResponse(ratesService.convert(request));

        convertResponse.onNext(response);
        convertResponse.onCompleted();
    }
}
