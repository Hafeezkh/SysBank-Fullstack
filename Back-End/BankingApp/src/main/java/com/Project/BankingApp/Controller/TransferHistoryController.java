package com.Project.BankingApp.Controller;

import com.Project.BankingApp.Model.TransferHistory;
import com.Project.BankingApp.Repository.TransferHistoryRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/transfer-history")
public class TransferHistoryController {

    @Autowired
    private TransferHistoryRepository transferHistoryRepository;

    @GetMapping
    public List<TransferHistory> getAllTransferHistories() {
        return transferHistoryRepository.findAll();
    }

    @GetMapping("/{historyId}")
    public ResponseEntity<TransferHistory> getTransferHistoryById(@PathVariable Long historyId) {
        return transferHistoryRepository.findById(historyId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TransferHistory> createTransferHistory(@Valid @RequestBody TransferHistory transferHistory) {
        TransferHistory savedHistory = transferHistoryRepository.save(transferHistory);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{historyId}")
                .buildAndExpand(savedHistory.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedHistory);
    }
}
