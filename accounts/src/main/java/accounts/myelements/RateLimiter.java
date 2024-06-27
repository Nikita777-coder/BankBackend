package accounts.myelements;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiter {
    private final Map<Integer, Bucket> customerBuckets = new ConcurrentHashMap<>();
    public Bucket getCustomerBucket(int customerId) {
        return customerBuckets.computeIfAbsent(customerId, id -> getBucket());
    }
    public void deleteCustomerBucket(int customerId) {
        customerBuckets.remove(customerId);
    }
    private Bucket getBucket() {
        Bandwidth limit = Bandwidth.simple(5, Duration.ofMinutes(1));

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}
