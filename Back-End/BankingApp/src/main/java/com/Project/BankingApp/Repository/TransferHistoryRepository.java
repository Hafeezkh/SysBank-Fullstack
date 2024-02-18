package com.Project.BankingApp.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.Project.BankingApp.Model.TransferHistory;


public interface TransferHistoryRepository extends JpaRepository<TransferHistory, Long> {
    // Add custom queries or methods if needed

}

