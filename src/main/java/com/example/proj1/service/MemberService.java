package com.example.proj1.service;

import com.example.proj1.model.MemberDto;
import com.example.proj1.repository.MemberRepository;
import com.example.proj1.repository.entity.Member;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    public List<MemberDto> getAll() {
        return memberRepository.findAll().stream()
                .map(c -> MemberDto.builder()
                        .userId(c.getUserId())
                        .crewId(c.getCrewId())
                        .nickname(c.getNickname())
                        .build())
                .collect(Collectors.toList());
    }

    /*public long addMember(MemberDto member) {
        Member m = Member.builder()
                .userId(member.getUserId())
                .crewId(member.getCrewId())
                .nickname(member.getNickname())
                .build();
        return memberRepository.save(m).getId();
    }*/
}
