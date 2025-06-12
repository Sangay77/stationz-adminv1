package com.station.shoppingcart.payment;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AuthoriseService {

    @Autowired
    private RestTemplate restTemplate;


    String url = "http://uatbfssecure.rma.org.bt:8080/BFSSecure/nvpapi";

    @Value("${beneficiary.benf_id}")
    private String benf_id;


    private static final Logger logger = LoggerFactory.getLogger(AuthoriseService.class);

    private final TransactionRepository transactionRepo;
    private final SourceStringHelper sourceStringHelper;
    private final PGPKIImpl pgpki;
    private final manualPKIImpl mpki;


    public AuthoriseService(TransactionRepository transactionRepo, SourceStringHelper sourceStringHelper, PGPKIImpl pgpki, manualPKIImpl mpki) {
        this.transactionRepo = transactionRepo;
        this.sourceStringHelper = sourceStringHelper;
        this.pgpki = pgpki;
        this.mpki = mpki;
    }

    public Map<String, String> authService(AuthoriseRequestDTO authRequest) throws Exception {

        String account_number = authRequest.getAccountNumber();
        String amount = authRequest.getTxnAmount();
        String bfs_paymentDesc = authRequest.getBfs_paymentDesc();

        TransactionMaster authObject = new TransactionMaster();
        authObject.setBfs_txnAmount(amount);
        authObject.setAccountNumber(account_number);
        authObject.setBfs_orderNo("NOKO" + System.currentTimeMillis());
        authObject.setBfs_benfTxnTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        authObject.setBfs_paymentDesc(bfs_paymentDesc);
        authObject.setBfs_remitterEmail("stenzin@rma.org.bt");
        authObject.setBfs_msgType("AR");
        authObject.setBfs_benfId(benf_id);
        authObject.setBfs_benfBankCode("01");
        authObject.setBfs_txnCurrency("BTN");
        authObject.setBfs_version("1.0");
        authObject.setBfs_paymentDesc(authRequest.getBfs_paymentDesc());
        String sourceString = sourceStringHelper.constructAuthorizationSourceString(authObject);
        String checksum = pgpki.signData(sourceString);
        authObject.setBfs_checkSum(checksum);
        logger.info("AuthoriseRequestDTO Set[{}]", authObject.toString());
        transactionRepo.save(authObject);

        MultiValueMap<String, String> params = getStringStringMultiValueMap(authObject, checksum);

        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new org.springframework.http.HttpEntity<>(params, headers),
                String.class
        );

        Map<String, String> responseMap = Arrays.stream(
                        Objects.requireNonNull(response.getBody()).split("&"))
                .map(kv -> kv.split("="))
                .filter(kv -> kv.length == 2)
                .collect(Collectors.toMap(
                        kv -> kv[0],
                        kv -> decodeValue(kv[1])
                ));
        String txnIdFromResponse = responseMap.get("bfs_bfsTxnId");
        if (txnIdFromResponse != null) {
            authObject.setBfs_bfsTxnId(txnIdFromResponse);
            transactionRepo.save(authObject);
            logger.info("Transaction updated with bfs_txnId: {}", txnIdFromResponse);
        } else {
            logger.warn("bfs_txnId not found in response: {}", responseMap);
        }
        return responseMap;
    }


    private static String decodeValue(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8); // Decode using UTF-8
    }

    private MultiValueMap<String, String> getStringStringMultiValueMap(TransactionMaster authObject, String checksum) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("bfs_msgType", "AR");
        params.add("bfs_benfId", authObject.getBfs_benfId());
        params.add("bfs_benfTxnTime", authObject.getBfs_benfTxnTime());
        params.add("bfs_orderNo", authObject.getBfs_orderNo());
        params.add("bfs_benfBankCode", "01");
        params.add("bfs_paymentDesc", authObject.getBfs_paymentDesc());
        params.add("bfs_remitterEmail", authObject.getBfs_remitterEmail());
        params.add("bfs_txnCurrency", authObject.getBfs_txnCurrency());
        params.add("bfs_txnAmount", authObject.getBfs_txnAmount());
        params.add("bfs_version", authObject.getBfs_version());
        params.add("bfs_checkSum", checksum);
        return params;
    }
}
