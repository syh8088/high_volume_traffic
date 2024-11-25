package com.accessor_queuing_system_main.repository;


import com.accessor_queuing_system_main.domain.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {


}
