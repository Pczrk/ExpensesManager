package com.example.proj1.service;

import com.example.proj1.exceptions.CrewCoreException;
import com.example.proj1.exceptions.MemberCoreException;
import com.example.proj1.exceptions.NullArgumentException;
import com.example.proj1.exceptions.TripCoreException;
import com.example.proj1.model.TripDto;
import com.example.proj1.repository.CrewRepository;
import com.example.proj1.repository.MemberRepository;
import com.example.proj1.repository.TripRepository;
import com.example.proj1.repository.entity.Crew;
import com.example.proj1.repository.entity.Trip;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.example.proj1.repository.entity.Member;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TripService {

    private final MemberRepository memberRepository;
    private final TripRepository tripRepository;
    private final CrewRepository crewRepository;

    public long createTrip(TripDto trip, Long userId) {
        if (trip == null || trip.getTripId() == null || trip.getTripDate() == null || trip.getCrewId() == null)
            throw new NullArgumentException();
        Optional<Crew> c = crewRepository.findCrewByCrewId(trip.getCrewId());
        if (c.isEmpty())
            throw new CrewCoreException(HttpStatus.NOT_FOUND,"Given crew wasn't found");

        Optional<Member> m = memberRepository.getMemberByUserIdAndCrewId(userId, trip.getCrewId());
        if (m.isEmpty())
            throw new MemberCoreException(HttpStatus.UNAUTHORIZED,"Current user isn't in given crew");

        return tripRepository.save(Trip.builder()
                        .tripId(trip.getTripId())
                        .tripDate(trip.getTripDate())
                        .crew(c.get())
                .build()).getTripId();
    }

    public TripDto getTrip(Long tripId, Long userId) {
        Optional<Trip> trip = tripRepository.findTripByTripId(tripId);
        if (trip.isEmpty())
            throw new TripCoreException(HttpStatus.NOT_FOUND,"Given trip wasn't found");

        Optional<Member> m = memberRepository.getMemberByUserIdAndCrewId(userId, trip.get().getTripId());
        if (m.isEmpty())
            throw new MemberCoreException(HttpStatus.UNAUTHORIZED,"Current user isn't in given crew");

        return TripDto.builder()
                .tripId(trip.get().getTripId())
                .crewId(trip.get().getCrew().getCrewId())
                .tripDate(trip.get().getTripDate())
                .build();
    }
}
