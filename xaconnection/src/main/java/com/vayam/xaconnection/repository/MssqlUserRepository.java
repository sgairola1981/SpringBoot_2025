package com.vayam.xaconnection.repository;



import com.vayam.xaconnection.model.MssqlUser;
import com.vayam.xaconnection.model.OracleUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MssqlUserRepository extends JpaRepository<MssqlUser, Long> {
    interface OracleUserRepository extends JpaRepository<OracleUser, Long> {
    }
}
