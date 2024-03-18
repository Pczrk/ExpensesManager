package com.example.proj1.service;

import com.example.proj1.exceptions.CrewCoreException;
import com.example.proj1.exceptions.NullArgumentException;
import com.example.proj1.exceptions.UserCoreException;
import com.example.proj1.model.*;
import com.example.proj1.repository.CrewRepository;
import com.example.proj1.repository.UserRepository;
import com.example.proj1.repository.entity.Crew;
import com.example.proj1.repository.entity.Member;
import com.example.proj1.repository.MemberRepository;
import com.example.proj1.repository.entity.MemberId;
import com.example.proj1.repository.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrewService {

    private final CrewRepository crewRepository;
    private final UserRepository userRepository;
    private final SecureRandom secureRandom;
    private final MemberRepository memberRepository;

    public String generateCrewAccessKey() {
        byte[] randomBytes = new byte[8];
        secureRandom.nextBytes(randomBytes);
        String accessKey = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes).substring(0,8);
        return crewRepository.existsByAccessKey(accessKey) ? generateCrewAccessKey():accessKey;
    }

    @Transactional
    public long createCrew(CrewCreateDto crew, long userId) {
        if (crew == null || crew.getName() == null)
            throw new NullArgumentException();

        Optional<User> u = userRepository.findByUserId(userId);
        if (u.isEmpty())
            throw new UserCoreException(HttpStatus.NOT_FOUND,"User with given userId not found");

        Crew c = crewRepository.save(Crew.builder()
                .name(crew.getName())
                .leader(u.get())
                .accessKey(generateCrewAccessKey())
                .build());

        memberRepository.save(Member.builder()
                .id(MemberId.builder()
                        .crewId(c.getCrewId())
                        .userId(u.get().getUserId())
                        .build())
                .crew(c)
                .user(u.get())
                .build());

        return c.getCrewId();
    }

    @Transactional
    public void joinCrew(long userId, String accessKey) {
        Optional<User> u = userRepository.findByUserId(userId);
        if (u.isEmpty())
            throw new UserCoreException(HttpStatus.NOT_FOUND,"User with given userId not found");

        Optional<Crew> c = crewRepository.findCrewByAccessKey(accessKey);
        if (c.isEmpty())
            throw new CrewCoreException(HttpStatus.CONFLICT,"This key is invalid");

        Optional<Member> m = memberRepository.getMemberByUserAndCrew(u.get(), c.get());
        if (m.isPresent())
            throw new CrewCoreException(HttpStatus.CONFLICT,"This user is already member of this crew");

        memberRepository.save(Member.builder()
                .id(MemberId.builder()
                        .crewId(c.get().getCrewId())
                        .userId(u.get().getUserId())
                        .build())
                .crew(c.get())
                .user(u.get())
                .build());
    }

    public CrewDto getCrew(Long crewId, long userId) {
        Optional<Crew> c = crewRepository.findCrewByCrewId(crewId);
        if (c.isEmpty())
            throw new CrewCoreException(HttpStatus.NOT_FOUND,"Crew with given crewId not found");

        Optional<Member> m = memberRepository.getMemberByUserIdAndCrewId(userId,c.get().getCrewId());
        if (m.isEmpty())
            throw new CrewCoreException(HttpStatus.UNAUTHORIZED,"User unauthorized to view crew info");

        List<MemberDto> members = c.get().getMembers().stream()
                .map(member -> MemberDto.builder()
                        .user(UserDto.builder()
                                .userId(member.getUser().getUserId())
                                .username(member.getUser().getUsername())
                                .build())
                        .crewId(member.getCrew().getCrewId())
                        .nickname(member.getNickname())
                        .build())
                .toList();

        return CrewDto.builder()
                .crewId(c.get().getCrewId())
                .accessKey(c.get().getAccessKey())
                .name(c.get().getName())
                .leader(UserDto.builder()
                        .username(c.get().getLeader().getUsername())
                        .userId(c.get().getLeader().getUserId())
                        .build())
                .members(members)
                .build();
    }

    public List<CrewBriefDto> getUserCrews(Long userId) {
        List<Crew> crews = crewRepository.findAllByUserId(userId);

        return crews.stream().map(crew -> CrewBriefDto.builder()
                        .crewId(crew.getCrewId())
                        .name(crew.getName())
                        .accessKey(crew.getAccessKey())
                        .build())
                .toList();
    }
}
