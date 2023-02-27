package library.services;

import java.net.URISyntaxException;
import library.dto.BookDataApiResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Configuration
public class BookService {

  @Autowired
  @LoadBalanced
  protected RestTemplate restTemplate;

  protected String serviceUrl;

  @Value("${security.API_TOKEN}")
  public String TOKEN;

  public BookService(String serviceUrl) {
    this.serviceUrl =
      serviceUrl.startsWith("http") ? serviceUrl : "http://" + serviceUrl;
  }

  /**
   * @TODO
   * 
   * This needs to be refactored.
   * 1- intercept any api call to insert the authorization header
   * 2- design a way to handle the api error
   * 
   * @param id
   * @return
   * @throws RestClientException
   * @throws URISyntaxException
   */
  public ResponseEntity<BookDataApiResponseDto> detail(final String id)
    throws RestClientException, URISyntaxException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("User-Agent", "Caller Service");
    headers.add("Authorization", String.format("Bearer %s", TOKEN));

    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(
      headers
    );

    String url = String.format("%s/book/%s", serviceUrl, id);

    return restTemplate.exchange(
      url,
      HttpMethod.GET,
      entity,
      BookDataApiResponseDto.class
    );
  }
}
