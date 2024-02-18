package com.Project.BankingApp.Controller;

import com.Project.BankingApp.Model.Transfer;
import com.Project.BankingApp.Model.User;
import com.Project.BankingApp.Repository.TransferRepository;
import com.Project.BankingApp.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {
    private static final Logger logger = LoggerFactory.getLogger(TransferController.class);

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public Transfer transferMoney(@RequestParam Long senderId,
                                  @RequestParam Long receiverId,
                                  @RequestParam Double amount) {
        try {
            // Get sender and receiver details from the transfer object
            User sender = userRepository.findById(senderId).orElse(null);
            User receiver = userRepository.findById(receiverId).orElse(null);

            if (sender != null && receiver != null) {
               double transferAmount=amount;

                if (sender.getCurrentBalance() >= transferAmount) {
                    sender.setCurrentBalance(sender.getCurrentBalance() - transferAmount);
                    userRepository.save(sender);
                } else {
                    throw new RuntimeException("Insufficient balance for the sender");
                }

                receiver.setCurrentBalance(receiver.getCurrentBalance() + transferAmount);
                userRepository.save(receiver);

                Transfer transfer = new Transfer();
                transfer.setSender(sender);
                transfer.setReceiver(receiver);
                transfer.setAmount(transferAmount);


                    // Save the transfer details
                    return transferRepository.save(transfer);
                } else {
                throw new RuntimeException("Sender or receiver not found");
                }
        } catch (Exception e) {
            logger.error("Error transferring money", e);
            throw new RuntimeException("Error transferring money. Please try again.");

        }

    }

    @GetMapping
    public List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }

    @GetMapping("/{transferId}")
    public Transfer getTransferById(@PathVariable Long transferId) {
        return transferRepository.findById(transferId).orElse(null);
    }
}
