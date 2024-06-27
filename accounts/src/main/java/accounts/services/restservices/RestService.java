package accounts.services.restservices;

import java.util.Map;

public interface RestService {
    <T> T get(String uri, Class<T> typeKey);
    <T> T get(String uri, Map<String, Object> params, Class<T> typeKey);
}
