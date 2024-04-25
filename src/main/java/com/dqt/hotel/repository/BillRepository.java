package com.dqt.hotel.repository;

import com.dqt.hotel.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Integer> {

}
