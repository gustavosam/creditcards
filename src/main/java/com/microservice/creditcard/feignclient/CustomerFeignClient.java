package com.microservice.creditcard.feignclient;

import com.microservice.creditcard.documents.CustomersComplementary;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-customers", url ="localhost:8080")
public interface CustomerFeignClient {

    @GetMapping("/customer/{customerDocument}")
    CustomersComplementary getCustomerById(@PathVariable String customerDocument);
}
