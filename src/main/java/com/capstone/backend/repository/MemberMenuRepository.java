package com.capstone.backend.repository;

import com.capstone.backend.entity.Member;
import com.capstone.backend.entity.MemberMenu;
import com.capstone.backend.entity.Menu;
import com.capstone.backend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberMenuRepository extends JpaRepository<MemberMenu, Long> {
    List<MemberMenu> findAllByRoom(Room room);

    Optional<MemberMenu> findByMenuAndMember(Menu menu, Member member);
}
