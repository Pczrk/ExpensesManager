package com.example.proj1.repository;

import com.example.proj1.repository.entity.Crew;
import com.example.proj1.repository.entity.Member;
import com.example.proj1.repository.entity.MemberId;
import com.example.proj1.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, MemberId> {
    Optional<Member> getMemberByUserAndCrew(User user, Crew crew);

    @Query(value = "select * from members m where m.user_id = :user_id and m.crew_id = :crew_id",nativeQuery = true)
    Optional<Member> getMemberByUserIdAndCrewId(@Param("user_id") Long userId,
                                                @Param("crew_id") Long crewId);
}