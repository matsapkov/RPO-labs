package ru.bmstu.matsapkov.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bmstu.matsapkov.backend.models.Artist;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {}