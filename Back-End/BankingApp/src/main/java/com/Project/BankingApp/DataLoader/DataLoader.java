package com.Project.BankingApp.DataLoader;

import com.Project.BankingApp.Model.Transfer;
import com.Project.BankingApp.Model.TransferHistory;
import com.Project.BankingApp.Model.User;
import com.Project.BankingApp.Repository.TransferHistoryRepository;
import com.Project.BankingApp.Repository.TransferRepository;
import com.Project.BankingApp.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private TransferHistoryRepository transferHistoryRepository;

    @Override
    public void run(String... args) {
        // Check if users exist in the database
        if (userRepository.count() == 0) {
            addDefaultData();
        }
    }

    private void addDefaultData() {
        String[] names = {"John Doe", "Jane Smith", "Robert Johnson", "Emily Davis", "Michael Brown",
                "Olivia Miller", "William Wilson", "Sophia Lee", "James Jones", "Emma Garcia"};

        for (int i = 0; i < names.length; i++) {
            User user = new User();
            user.setName(names[i]);
            user.setEmail(names[i].toLowerCase().replaceAll("\\s", "") + getRandomNumber() + "@gmail.com");
            user.setCurrentBalance(40000.0);
            userRepository.save(user);
        }



    }



    private int getRandomNumber() {
        Random random = new Random();
        return random.nextInt(1000);
    }



}
