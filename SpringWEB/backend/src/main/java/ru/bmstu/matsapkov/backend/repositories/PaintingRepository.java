package ru.bmstu.matsapkov.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bmstu.matsapkov.backend.models.Painting;

public interface PaintingRepository extends JpaRepository<Painting, Integer> {
}