package com.example.tabelog.entity;

import java.sql.Timestamp;

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
@Table(name = "shops")
@Data
public class Shop {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "shop_id")
	private Integer shopId;

	@Column(name = "name")
	private String name;

	@Column(name = "image_name")
	private String imageName;

	@Column(name = "description")
	private String description;

	@Column(name = "open_time")
	private String openTime;

	@Column(name = "price")
	private Integer price;

	@Column(name = "postal_code")
	private String postalCode;

	@Column(name = "address")
	private String address;

	@Column(name = "holiday")
	private String holiday;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;


	@Column(name = "created_at", insertable = false, updatable = false)
	private Timestamp createdAt;

	@Column(name = "updated_at", insertable = false, updatable = false)
	private Timestamp updatedAt;
}