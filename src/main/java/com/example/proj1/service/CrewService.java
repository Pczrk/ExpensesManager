package com.example.proj1.service;

import com.example.proj1.exceptions.CrewCoreException;
import com.example.proj1.exceptions.NullArgumentException;
import com.example.proj1.model.*;
import com.example.proj1.repository.CrewRepository;
import com.example.proj1.repository.entity.Crew;
import com.example.proj1.repository.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class    CrewService {

    private final CrewRepository crewRepository;

    private final SecureRandom secureRandom;
    private final UserService userService;
    private final MemberService memberService;

    private String generateCrewAccessKey() {
        byte[] randomBytes = new byte[8];
        secureRandom.nextBytes(randomBytes);
        String accessKey = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes).substring(0,8);
        return crewRepository.existsByAccessKey(accessKey) ? generateCrewAccessKey():accessKey;
    }

    protected Crew getCrewEntity(Long crewId){
        Optional<Crew> c = crewRepository.findCrewByCrewId(crewId);
        if (c.isEmpty())
            throw new CrewCoreException(HttpStatus.NOT_FOUND,"Given crew wasn't found");
        return c.get();
    }

    @Transactional
    public long createCrew(CrewCreateDto crewDto, long userId) {
        if (crewDto == null || crewDto.getName() == null)
            throw new NullArgumentException();

        User user = userService.getUserEntity(userId);

        if (crewRepository.existsByName(crewDto.getName()))
            throw new CrewCoreException(HttpStatus.CONFLICT, "Crew with given name already exists");

        Crew crew = crewRepository.save(Crew.builder()
                .name(crewDto.getName())
                .leader(user)
                .accessKey(generateCrewAccessKey())
                .build());

        memberService.createMemberEntity(user,crew);

        return crew.getCrewId();
    }

    @Transactional
    public void joinCrew(long userId, String accessKey) {
        User user = userService.getAuthenticatedUserEntity(userId);

        Optional<Crew> c = crewRepository.findCrewByAccessKey(accessKey);
        if (c.isEmpty())
            throw new CrewCoreException(HttpStatus.CONFLICT,"This key is invalid");

        memberService.assertUserIsNotMember(user,c.get());
        memberService.createMemberEntity(user,c.get());
    }

    public CrewDto getCrew(Long crewId, Long userId) {
        Optional<Crew> c = crewRepository.findCrewByCrewId(crewId);
        if (c.isEmpty())
            throw new CrewCoreException(HttpStatus.NOT_FOUND,"Crew with given crewId not found");

        memberService.assertUserIsMember(userId, crewId);

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
