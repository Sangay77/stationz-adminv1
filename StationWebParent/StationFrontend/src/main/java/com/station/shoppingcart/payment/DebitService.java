package com.station.shoppingcart.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class DebitService {

    private final SourceStringHelper sourceStringHelper;
    private final TransactionRepository transactionRepository;
    private final PGPKIImpl pgpki;
    private final callApiHelper apiHelper;
    private static final Logger logger = LoggerFactory.getLogger(DebitService.class);

    public DebitService(SourceStringHelper sourceStringHelper, TransactionRepository transactionRepository, PGPKIImpl pgpki, callApiHelper apiHelper) {
        this.sourceStringHelper = sourceStringHelper;
        this.transactionRepository = transactionRepository;
        this.pgpki = pgpki;
        this.apiHelper = apiHelper;
    }

//    public Map<String, String> debit(DebitRequestDTO debitRequestDTO) {
//        String bfsTxnId = debitRequestDTO.getBfs_bfsTxnId();
//        Optional<TransactionMaster> transactionOptional = transactionRepository.findByBfsTxnId(bfsTxnId);
//
//        return transactionOptional.map(transaction -> {
//            logger.info("Transaction found: {}", transaction);
//
//            transaction.setBfs_remitterOtp(debitRequestDTO.getBfs_remitterOtp());
//
//            String sourceString = sourceStringHelper.constructDebitSourceString(transaction);
//
//            try {
//                String checkSum = pgpki.signData(sourceString);
//                transaction.setBfs_checkSum(checkSum);
//                logger.info("DebitRequest Set[{}]", transaction);
//
//                Map<String, String> responseMap = apiHelper.callRMADR(transaction);
//                logger.info("===RMA RESPONSE {}", responseMap);
//
//                transactionRepository.save(transaction);
//
//                return "Debit request processed successfully. Response: " + responseMap;
//            } catch (Exception e) {
//                logger.error("Error processing debit request: {}", e.getMessage(), e);
//                throw new RuntimeException("Error processing debit request", e);
//            }
//        }).orElseGet(() -> {
//            logger.info("No transaction found for transaction ID: {}", bfsTxnId);
//            return "No transaction found for transaction ID: " + bfsTxnId;
//        });
//    }

    public Map<String, String> debit(DebitRequestDTO debitRequestDTO) {
        String bfsTxnId = debitRequestDTO.getBfs_bfsTxnId();
        Optional<TransactionMaster> transactionOptional = transactionRepository.findByBfsTxnId(bfsTxnId);

        return transactionOptional.map(transaction -> {
            logger.info("Transaction found: {}", transaction);

            transaction.setBfs_remitterOtp(debitRequestDTO.getBfs_remitterOtp());

            String sourceString = sourceStringHelper.constructDebitSourceString(transaction);

            try {
                String checkSum = pgpki.signData(sourceString);
                transaction.setBfs_checkSum(checkSum);
                logger.info("DebitRequest Set[{}]", transaction);

                Map<String, String> responseMap = apiHelper.callRMADR(transaction);
                logger.info("===RMA RESPONSE {}", responseMap);

                transactionRepository.save(transaction);

                String debitAuthCode = responseMap.get("bfs_debitAuthCode");
                if ("00".equals(debitAuthCode)) {
                    responseMap.put("status", "success");
                } else {
                    // include status=failed and the exact debit auth code
                    responseMap.put("status", "failed");
                    responseMap.put("failedCode", debitAuthCode != null ? debitAuthCode : "unknown");
                }

                return responseMap;
            } catch (Exception e) {
                logger.error("Error processing debit request: {}", e.getMessage(), e);
                throw new RuntimeException("Error processing debit request", e);
            }
        }).orElseGet(() -> {
            logger.info("No transaction found for transaction ID: {}", bfsTxnId);
            return Map.of(
                    "status", "failed",
                    "message", "No transaction found for transaction ID: " + bfsTxnId,
                    "failedCode", "not_found"
            );
        });
    }

}