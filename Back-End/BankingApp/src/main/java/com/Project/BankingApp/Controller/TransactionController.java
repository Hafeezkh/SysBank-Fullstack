package com.Project.BankingApp.Controller;

import com.Project.BankingApp.Exception.UserNotFoundException;
import com.Project.BankingApp.Model.Transfer;
import com.Project.BankingApp.Model.TransferHistory;
import com.Project.BankingApp.Model.User;
import com.Project.BankingApp.Repository.TransferHistoryRepository;
import com.Project.BankingApp.Repository.TransferRepository;
import com.Project.BankingApp.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private TransferHistoryRepository transferHistoryRepository;

    @Transactional
    @PostMapping("/{userId}")
    public ResponseEntity<String> performTransaction(
            @PathVariable Long userId,
            @RequestParam Long receiverId,
            @RequestParam Double amount) {
        try {
            // Fetch the user initiating the transaction
            User sender = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

            // Fetch the receiver using the provided receiverId
            User receiver = userRepository.findById(receiverId).orElse(null);

            if (receiver != null && !sender.getId().equals(receiver.getId())) {
                if (amount>0){

                if (sender.getCurrentBalance() >= amount) {
                    // Update sender's balance
                    sender.setCurrentBalance(sender.getCurrentBalance() - amount);
                    userRepository.save(sender);

                    // Update receiver's balance
                    receiver.setCurrentBalance(receiver.getCurrentBalance() + amount);
                    userRepository.save(receiver);

                    // Record the transaction in the Transfer table
                    Transfer transfer = new Transfer();
                    transfer.setSender(sender);
                    transfer.setReceiver(receiver);
                    transfer.setAmount(amount);
                    transferRepository.save(transfer);

                    // Record the transaction in the TransferHistory table (optional)
                    TransferHistory transferHistory = TransferHistory.builder()
                            .senderName(sender.getName())
                            .receiverName(receiver.getName())
                            .sentAmount(BigDecimal.valueOf(amount))
                            .senderBalance(BigDecimal.valueOf(sender.getCurrentBalance()))
                            .timestamp(LocalDateTime.now())
                            .build();

                    transferHistoryRepository.save(transferHistory);

                    return ResponseEntity.ok("Transaction successful");
                } else {
                    return ResponseEntity.badRequest().body("Insufficient balance for the transaction");
                }
            } else {
                return ResponseEntity.badRequest().body("Invalid amount");
            }
        } else {
                return ResponseEntity.badRequest().body("Invalid receiver");
            }
        }catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error performing transaction. Please try again.");
        }
    }
}