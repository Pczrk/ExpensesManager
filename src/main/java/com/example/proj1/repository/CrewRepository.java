package com.example.proj1.repository;

import com.example.proj1.repository.entity.Crew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrewRepository extends JpaRepository<Crew, Long> {
    boolean existsByAccessKey(String accessKey);
    Optional<Crew> findCrewByCrewId(Long crewId);
    Optional<Crew> findCrewByAccessKey(String accessKey);
    @Query(value = "select c.* from crews c join members m on c.crew_id = m.crew_id where m.user_id = :user_id",nativeQuery = true)
    List<Crew> findAllByUserId(@Param("user_id") Long userId);
}