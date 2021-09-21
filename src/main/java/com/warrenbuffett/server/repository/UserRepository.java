package com.warrenbuffett.server.repository;

import com.warrenbuffett.server.domain.UserIncomeMain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserIncomeMain,Long>{

}