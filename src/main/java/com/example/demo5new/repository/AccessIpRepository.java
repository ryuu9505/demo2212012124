package com.example.demo5new.repository;

import com.example.demo5new.domain.AccessIp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessIpRepository extends JpaRepository<AccessIp, Long> {

    boolean existsByIpAddress(String IpAddress);
}
