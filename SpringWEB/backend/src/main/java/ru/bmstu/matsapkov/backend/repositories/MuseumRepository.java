package ru.bmstu.matsapkov.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bmstu.matsapkov.backend.models.Museum;

public interface MuseumRepository extends JpaRepository<Museum, Integer> {
}