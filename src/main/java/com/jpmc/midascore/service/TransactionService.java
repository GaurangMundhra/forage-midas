package com.jpmc.midascore.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jpmc.midascore.dto.TransactionDto;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;

@Service
public class TransactionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRecordRepository transactionRecordRepository;

    @Autowired
    private IncentiveService incentiveService;

    @Transactional
    @KafkaListener(topics = "transactions", groupId = "midas-core-group")
    public void processTransaction(TransactionDto transactionDto) {
        Optional<UserRecord> senderOpt = userRepository.findByName(transactionDto.getSenderName());
        Optional<UserRecord> recipientOpt = userRepository.findByName(transactionDto.getRecipientName());

        if (senderOpt.isEmpty() || recipientOpt.isEmpty()) {
            System.out.println("Invalid sender or recipient: " + transactionDto);
            return;
        }

        UserRecord sender = senderOpt.get();
        UserRecord recipient = recipientOpt.get();

        if (sender.getBalance() < transactionDto.getAmount()) {
            System.out.println("Insufficient balance for transaction: " + transactionDto);
            return;
        }

        // ✅ Deduct from sender, add to recipient
        sender.setBalance(sender.getBalance() - transactionDto.getAmount());
        recipient.setBalance(recipient.getBalance() + transactionDto.getAmount());

        userRepository.save(sender);
        userRepository.save(recipient);

        // ✅ Call Incentive API after transaction
        double incentiveAmount = incentiveService.getIncentive(transactionDto).getAmount();
        recipient.setBalance(recipient.getBalance() + incentiveAmount); // ✅ Add incentive to recipient's balance
        userRepository.save(recipient);

        // ✅ Save transaction with incentive
        TransactionRecord transaction = new TransactionRecord(sender, recipient, transactionDto.getAmount(), incentiveAmount);
        transactionRecordRepository.save(transaction);

        System.out.println("Transaction processed with incentive: " + transactionDto);
    }
}
