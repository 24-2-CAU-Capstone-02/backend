package com.capstone.backend.repository;

import com.capstone.backend.entity.Member;
import com.capstone.backend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    Optional<Member> findBySessionToken(String sessionToken);

    Optional<Member> findByRoomAndUsername(Room room, String username);

    List<Member> findAllByRoom(Room room);
}
