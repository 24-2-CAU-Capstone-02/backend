package com.capstone.backend.repository;

import com.capstone.backend.entity.MemberMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberMenuRepository extends JpaRepository<MemberMenu, Long> {
}
