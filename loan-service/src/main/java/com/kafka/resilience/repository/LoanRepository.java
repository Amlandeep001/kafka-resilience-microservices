package com.kafka.resilience.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kafka.resilience.entity.LoanDO;

public interface LoanRepository extends JpaRepository<LoanDO, Long>
{
}
