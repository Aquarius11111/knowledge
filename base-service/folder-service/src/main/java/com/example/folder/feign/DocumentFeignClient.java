package com.example.folder.feign;

import com.example.common.R;
import com.example.domain.Document;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "document-service" ,url="http://192.168.40.47:9001")
public interface DocumentFeignClient {

    @GetMapping("/document/{id}}")
    R<Document> getDocumentById(@PathVariable("id") Integer id);

}
