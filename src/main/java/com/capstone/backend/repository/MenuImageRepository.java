package com.capstone.backend.repository;

import com.capstone.backend.entity.MenuImage;
import com.capstone.backend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuImageRepository extends JpaRepository<MenuImage, Long> {
    List<MenuImage> findAllByRoom(Room room);
}
