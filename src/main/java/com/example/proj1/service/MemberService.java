package com.example.proj1.service;

import com.example.proj1.exceptions.CrewCoreException;
import com.example.proj1.repository.MemberRepository;
import com.example.proj1.repository.entity.Crew;
import com.example.proj1.repository.entity.Member;
import com.example.proj1.repository.entity.MemberId;
import com.example.proj1.repository.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    protected void createMemberEntity(User user, Crew crew){
        memberRepository.save(Member.builder()
                .id(MemberId.builder()
                        .crewId(crew.getCrewId())
                        .userId(user.getUserId())
                        .build())
                .crew(crew)
                .user(user)
                .build());
    }

    protected void assertUserIsMember(Long userId, Long crewId){
        Optional<Member> m = memberRepository.findMemberByUserIdAndCrewId(userId,crewId);
        if (m.isEmpty())
            throw new CrewCoreException(HttpStatus.UNAUTHORIZED,"User unauthorized to access crew information");
    }

    protected void assertUserIsNotMember(User user, Crew crew){
        Optional<Member> m = memberRepository.findMemberByUserAndCrew(user, crew);
        if (m.isPresent())
            throw new CrewCoreException(HttpStatus.CONFLICT,"User is already member of this crew");
    }

}
