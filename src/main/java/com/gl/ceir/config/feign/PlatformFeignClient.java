package com.gl.ceir.config.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Component
@Service
@FeignClient(name = "platformFeignClient", url = "http://64.227.146.191:9509/eirs/fileAttachmentNotification")
public interface PlatformFeignClient {
    @PostMapping(value = "/fileAttachmentNotification", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String uploadMultipart(@RequestPart("file") MultipartFile file,
                           @RequestParam String email,
                           @RequestParam String subject,
                           @RequestParam String message,
                           @RequestParam String msgLang,
                           @RequestParam String txnId);

}
