package com.gl.ceir.config.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.util.stream.Collectors.joining;


@Service
public class CallHttpServiceForMail {

    @Value("${platFormAttachmentUrlPath}")
    String platFormAttachmentUrlPath;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private RestTemplate restTemplate = null;

    public void callEmailApi(Map<String, String> emailDto) {

        logger.info("Setting for calling the API");

        long start = System.currentTimeMillis();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            List<Charset> acceptCharset = new ArrayList<>();
            List<Locale.LanguageRange> languageRanges = new ArrayList<>();
            acceptCharset.add(StandardCharsets.UTF_8);
//            languageRanges.add()
//            headers.setAcceptLanguage(languageRanges);
            headers.setAcceptCharset(acceptCharset);
            SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
            clientHttpRequestFactory.setConnectTimeout(100000);
            clientHttpRequestFactory.setReadTimeout(100000);
            restTemplate = new RestTemplate();
            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            List<MediaType> list = new ArrayList<>();
            list.add(MediaType.ALL);
            converter.setSupportedMediaTypes(list);
            restTemplate.getMessageConverters().add(converter);
            String url = platFormAttachmentUrlPath + "?";

            Map<String, String> requestParams = new HashMap<>();
            requestParams.put("message", emailDto.get("message"));
            requestParams.put("subject", emailDto.get("subject"));
            requestParams.put("email", emailDto.get("email"));
            requestParams.put("msgLang", "en");
            requestParams.put("txnId", emailDto.get("txnId"));

            String encodedURL = requestParams.keySet().stream()
                    .map(key -> {
                        return key + "=" + requestParams.get(key);
                    })
                    .collect(joining("&", url, ""));
            logger.info("Url is {}", encodedURL);

            MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
            Resource file1 = new FileSystemResource(new File(emailDto.get("file")));
            multipartBodyBuilder.part("file", file1, MediaType.MULTIPART_FORM_DATA);
            System.out.println(multipartBodyBuilder);
            MultiValueMap<String, HttpEntity<?>> multipartBody = multipartBodyBuilder.build();
            HttpEntity<MultiValueMap<String, HttpEntity<?>>> httpEntity = new HttpEntity<>(multipartBody, headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    encodedURL, HttpMethod.POST, httpEntity, String.class);
//            System.out.println("responseEntity");
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                logger.info("Email api called successfully");
            }
        } catch (ResourceAccessException resourceAccessException) {
            logger.error("Error while Sending Alert resourceAccessException:{} Request:{}", resourceAccessException.getMessage(), "", resourceAccessException);
        } catch (Exception e) {
            logger.error("Error while Sending Alert Error:{} Request:{}", e.getMessage(), e);
        }


    }


    private String encodeValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }
}
