package com.station.shoppingcart.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class callApiHelper {

    @Autowired
    private RestTemplate restTemplate;

    String url = "http://uatbfssecure.rma.org.bt:8080/BFSSecure/nvpapi";

    @Value("${beneficiary.benf_id}")
    private String benf_id;

    private static final Logger logger = LoggerFactory.getLogger(callApiHelper.class);


    public Map<String, String> callRMAAE(TransactionMaster transaction) throws Exception {


        MultiValueMap<String, String> params = getStringStringMultiValueMap(transaction);
        logger.info("++++Request+++++++: {}", params);

        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, new org.springframework.http.HttpEntity<>(params, headers), String.class);

        return Arrays.stream(Objects.requireNonNull(response.getBody())
                        .split("&"))
                .map(keyValue -> keyValue.split("="))
                .filter(keyValue -> keyValue.length == 2)
                .collect(Collectors.toMap(
                        keyvalue -> keyvalue[0],
                        keyvalue -> decodeValue(keyvalue[1])
                ));
    }

    public Map<String, String> callRMADR(TransactionMaster transaction) throws Exception {


        MultiValueMap<String, String> params = getDebitMap(transaction);
        logger.info("++++Debit Request+++++++: {}", params);

        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, new org.springframework.http.HttpEntity<>(params, headers), String.class);

        return Arrays.stream(Objects.requireNonNull(response.getBody())
                        .split("&"))
                .map(keyValue -> keyValue.split("="))
                .filter(keyValue -> keyValue.length == 2)
                .collect(Collectors.toMap(
                        keyvalue -> keyvalue[0],
                        keyvalue -> decodeValue(keyvalue[1])
                ));
    }


    private static String decodeValue(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8); // Decode using UTF-8
    }

    private MultiValueMap<String, String> getStringStringMultiValueMap(TransactionMaster transaction) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("bfs_msgType", "AE");
        params.add("bfs_benfId", transaction.getBfs_benfId());
        params.add("bfs_bfsTxnId", transaction.getBfs_bfsTxnId());
        params.add("bfs_checkSum", transaction.getBfs_checkSum());
        params.add("bfs_remitterAccNo",transaction.getAccountNumber());
        params.add("bfs_remitterBankId",transaction.getBfs_remitterBankId());
        return params;
    }

    private MultiValueMap<String, String> getDebitMap(TransactionMaster transaction) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("bfs_msgType", "DR");
        params.add("bfs_benfId", transaction.getBfs_benfId());
        params.add("bfs_bfsTxnId", transaction.getBfs_bfsTxnId());
        params.add("bfs_checkSum", transaction.getBfs_checkSum());
        params.add("bfs_remitterOtp",transaction.getBfs_remitterOtp());
        return params;
    }
}
