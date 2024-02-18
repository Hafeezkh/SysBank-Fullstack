package com.Project.BankingApp.Repository;


import com.Project.BankingApp.Model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfer,Long> {
}
