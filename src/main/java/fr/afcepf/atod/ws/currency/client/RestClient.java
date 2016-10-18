package fr.afcepf.atod.ws.currency.client;
import javax.ws.rs.core.MediaType;
import org.apache.http.client.utils.URIBuilder;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.json.JSONObject;

/**
 * Internal Rest Client to get actualized rates.
 * @author nikko
 *
 */
public class RestClient {
    /**
     * api key.
     */
    private static final String API_ID = "28619a8ba8244432bf38a24aff4bee65";
    /**
     * api host.
     */
    private static final String SERVICE_HOST = "openexchangerates.org";
    /**
     * api for rates.
     */
    private static final String SERVICE1 = "/api/latest.json";
    /**
     * api for currencies code and names.
     */
    private static final String SERVICE2 = "/api/currencies.json";
    /**
     * Http Success Code.
     */
    private static final Integer HTTP_SUCCESS = 200;
    /**
     * use api for rates.
     * @return dlfkjhkj
     */
    public String getLatestRates() {
        String output = "";
        try {
            URIBuilder builder = new URIBuilder();
            builder.setScheme("https").setHost(SERVICE_HOST)
            .setPath(SERVICE1)
            .setParameter("app_id", API_ID);
            ClientRequest request = new ClientRequest(builder.build().toString());
            request.accept(MediaType.APPLICATION_JSON);
            ClientResponse<String> response = request.get(String.class);

            if (response.getStatus() != HTTP_SUCCESS) {
                throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
            }
            output = response.getEntity();
            response.close();
        } catch (Exception paramE) {
            paramE.printStackTrace();
        }
        return output;
    }
    /**
     * use api for currencies code and names.
     * @return String currencies list in json
     */
    public String getCurrencies() {
        String output = "";
        try {
            URIBuilder builder = new URIBuilder();
            builder.setScheme("https").setHost(SERVICE_HOST)
            .setPath(SERVICE2);
            ClientRequest request = new ClientRequest(builder.build().toString());
            request.accept(MediaType.APPLICATION_JSON);
            ClientResponse<String> response = request.get(String.class);
            if (response.getStatus() != HTTP_SUCCESS) {
                throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
            }
            output = response.getEntity();
            response.close();
        } catch (Exception paramE) {
            paramE.printStackTrace();
        }
        return output;
    }
}
