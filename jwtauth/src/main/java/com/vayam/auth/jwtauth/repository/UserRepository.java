package com.vayam.auth.jwtauth.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vayam.auth.jwtauth.model.User;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    @Query(value = """
        SELECT * FROM (
            SELECT u.*, ROWNUM AS rn FROM (
                SELECT * FROM users_data ORDER BY id
            ) u WHERE ROWNUM <= :pageSize + (:page * :pageSize)
        ) WHERE rn > (:page * :pageSize)
        """,
            nativeQuery = true)
    List<User> findUsersWithPagination(@Param("page") int page, @Param("pageSize") int pageSize);
}