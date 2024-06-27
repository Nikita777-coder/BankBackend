package accounts.myelements;

import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Map;

@Component
public class URICreator {
    public String buildUri(String path, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder(path);
        int count = 0;

        if (!params.isEmpty()) {
            sb.append('?');
        }

        try {
            for (var param : params.entrySet()) {
                count++;
                String name = param.getKey();
                String encodedValue = URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8");

                sb.append(String.format("%s=%s", name, encodedValue));

                if (count != params.size()) {
                    sb.append('&');
                }
            }
        } catch (UnsupportedEncodingException ignore) {

        }

        URI uri;

        try {
            uri = new URI(sb.toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return uri.toString();
    }
}
