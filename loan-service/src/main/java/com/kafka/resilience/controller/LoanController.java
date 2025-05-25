package com.kafka.resilience.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kafka.resilience.entity.LoanDO;
import com.kafka.resilience.service.LoanService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
@Slf4j
public class LoanController
{
	private final LoanService loanService;

	@PostMapping("/process")
	public ResponseEntity<String> processLoan(@RequestBody LoanDO loanDO)
	{
		log.info("LoanController::processLoan received loan application request for userId: {}", loanDO.getUserId());
		String loanTransactionId = loanService.processLoanApplication(loanDO);
		String responseMessage = String.format("Loan application received, credit check in progress. Transaction ID: %s", loanTransactionId);
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(responseMessage);
	}

	@GetMapping("/status")
	public ResponseEntity<?> getLoanStatus(
			@RequestParam(required = false) Long loanId)
	{
		Optional<LoanDO> loanStatus = loanService.getLoanStatusById(loanId);
		return loanStatus.map(loan -> ResponseEntity.ok().body(loan.getStatus()))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
}
