package com.ptit.thesis.smartrecruit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ptit.thesis.smartrecruit.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserFirebaseUid(String userFirebaseUid);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUserName(String userName);

    /**
     * Tìm phần số lớn nhất trong các username có định dạng userName + số
     * Mục đích để tạo username mới không trùng lặp bằng cách tăng phần số lên 1
     * Sử dụng khi user đăng ký bằng email có tên quá dài
     * Tối ưu bằng index trên cột user_name khi sử dụng Like, rồi sau đó duyệt trâu bằng regex
     * @param userName
     * @return
     */
    @Query(value = "SELECT MAX(CAST(SUBSTRING(user_name, LENGTH(:userName) + 1) AS UNSIGNED)) " +
            "FROM users " +
            "WHERE user_name LIKE CONCAT(:userName, '%') " +
            "AND user_name REGEXP CONCAT('^', :userName, '[0-9]+$')",
        nativeQuery = true)
    Optional<Integer> findUserNameMaxSuffix(@Param("userName") String userName);
}
