package com.codueon.boostUp.domain.lesson.repository;

import com.codueon.boostUp.domain.lesson.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
