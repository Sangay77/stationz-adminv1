package com.station.shoppingcart.payment;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class EnquiryService {

    private final SourceStringHelper sourceStringHelper;
    private final TransactionRepository transactionRepository;
    private final PGPKIImpl pgpki;
    private final callApiHelper apiHelper;
    private static final Logger logger = LoggerFactory.getLogger(EnquiryService.class);

    public EnquiryService(SourceStringHelper sourceStringHelper, TransactionRepository transactionRepository, PGPKIImpl pgpki, callApiHelper apiHelper) {
        this.sourceStringHelper = sourceStringHelper;
        this.transactionRepository = transactionRepository;
        this.pgpki = pgpki;
        this.apiHelper = apiHelper;
    }

    public Map<String, String> accountEnquiry(AccountInquiryDTO accountInquiryDTO) {

        String bfs_txn_id = accountInquiryDTO.getBfs_bfsTxnId();
        Optional<TransactionMaster> transactionOptional = transactionRepository.findByBfsTxnId(bfs_txn_id);

        return transactionOptional.map(transaction -> {
            logger.info("Transaction found: {}", transaction);

            transaction.setAccountNumber(accountInquiryDTO.getBfs_remitterAccNo());
            transaction.setBfs_remitterBankId(accountInquiryDTO.getBfs_remitterBankId());

            String sourceString = sourceStringHelper.constructEnquirySourceString(transaction);

            try {
                String checkSum = pgpki.signData(sourceString);
                transaction.setBfs_checkSum(checkSum);
                logger.info("AuthoriseRequestDTO Set[{}]", transaction);

                transactionRepository.save(transaction);

                Map<String, String> stringStringMap = apiHelper.callRMAAE(transaction);

                for (Map.Entry<String, String> entry : stringStringMap.entrySet()) {
                    System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
                }

                logger.info("===RMA RESPONSE {}", stringStringMap);

                return stringStringMap;
            } catch (Exception e) {
                logger.error("Error processing transaction: {}", e.getMessage(), e);
                throw new RuntimeException("Error processing transaction", e);
            }
        }).orElseGet(() -> {
            logger.info("No transaction found for transaction id: {}", bfs_txn_id);
            return Map.of("error", "No transaction found for transaction id: " + bfs_txn_id);
        });
    }
}