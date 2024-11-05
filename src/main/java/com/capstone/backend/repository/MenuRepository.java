package com.capstone.backend.repository;

import com.capstone.backend.entity.Menu;
import com.capstone.backend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllByRoom(Room room);
}
