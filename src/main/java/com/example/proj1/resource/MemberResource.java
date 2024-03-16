package com.example.proj1.resource;

import com.example.proj1.model.MemberDto;
import com.example.proj1.model.MemberRequest;
import com.example.proj1.model.MemberResponse;
import com.example.proj1.service.MemberService;
import jakarta.servlet.Servlet;
import lombok.AllArgsConstructor;
import org.apache.coyote.Request;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberResource {

    private MemberService memberService;

    @GetMapping("")
    public ResponseEntity<MemberResponse> getMembers(){
        List<MemberDto> members = memberService.getAll();
        return ResponseEntity.ok(new MemberResponse(members));
    }

    /*@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addMember(@RequestBody MemberRequest request){

        long id = memberService.addMember(request.getMember());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }*/

}
