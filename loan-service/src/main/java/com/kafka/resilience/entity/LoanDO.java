package com.kafka.resilience.entity;

import com.kafka.resilience.constants.LoanStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDO
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long loanId;

	int userId;
	double amount;

	@Enumerated(EnumType.STRING)
	LoanStatus status; // PENDING, APPROVED, REJECTED

	String loanTransactionId;
}
