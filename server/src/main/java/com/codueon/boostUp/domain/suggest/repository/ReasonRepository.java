package com.codueon.boostUp.domain.suggest.repository;

import com.codueon.boostUp.domain.suggest.entity.Reason;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReasonRepository extends JpaRepository<Reason, Long> {
}
