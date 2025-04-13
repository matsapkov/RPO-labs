package ru.bmstu.matsapkov.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bmstu.matsapkov.backend.models.Country;

@Repository
public interface CountryRepository  extends JpaRepository<Country, Long>
{

}
