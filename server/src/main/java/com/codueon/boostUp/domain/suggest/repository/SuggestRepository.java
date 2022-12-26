package com.codueon.boostUp.domain.suggest.repository;

import com.codueon.boostUp.domain.suggest.entity.Suggest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuggestRepository extends JpaRepository<Suggest, Long> {

}
