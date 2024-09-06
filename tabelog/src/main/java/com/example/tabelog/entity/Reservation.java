package com.example.tabelog.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "reservations")
@Data
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Integer reservationId;
    
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop; 
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;     
    
    @Column(name = "reservation_datetime")
    private LocalDateTime reservationDatetime;
    
    @Column(name = "number_of_people")
    private Integer numberOfPeople; 
    
    @Column(name = "amount")
    private Integer amount;     
    
    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;
    
    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt;    
}