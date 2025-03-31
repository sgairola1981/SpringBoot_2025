package com.vayam.xaconnection.repository;



import com.vayam.xaconnection.model.OracleUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OracleUserRepository extends JpaRepository<OracleUser, Long> {
}
