package com.vayam.xaconnection.repository;
import com.vayam.xaconnection.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
