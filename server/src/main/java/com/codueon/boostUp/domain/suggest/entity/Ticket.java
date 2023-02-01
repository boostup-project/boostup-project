package com.codueon.boostUp.domain.suggest.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TICKET_ID")
    private Long id;
    private String ticketName;
    private String ticketContent;
    private Integer ticketCost;
    private Integer validHours;
}
