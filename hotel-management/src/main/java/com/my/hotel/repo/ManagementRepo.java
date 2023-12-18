package com.my.hotel.repo;

import com.my.hotel.entity.Management;
import com.my.hotel.entity.key.ManagementId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagementRepo extends JpaRepository<Management, ManagementId> {

}
