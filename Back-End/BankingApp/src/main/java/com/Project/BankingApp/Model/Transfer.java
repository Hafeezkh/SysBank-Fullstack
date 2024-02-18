package com.Project.BankingApp.Model;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name="receiver_id")
    private User receiver;

    private double amount;

}
