package com.example.proj1.service;

import com.example.proj1.exceptions.NullArgumentException;
import com.example.proj1.exceptions.TripCoreException;
import com.example.proj1.model.TripDto;
import com.example.proj1.repository.TripRepository;
import com.example.proj1.repository.entity.Crew;
import com.example.proj1.repository.entity.Trip;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;

    private final CrewService crewService;
    private final MemberService memberService;

    protected Trip getTripEntity(Long tripId){
        Optional<Trip> trip = tripRepository.findTripByTripId(tripId);
        if(trip.isEmpty())
            throw new TripCoreException(HttpStatus.NOT_FOUND, "Trip with given tripId can't be found in database");
        return trip.get();
    }

    public long createTrip(TripDto tripDto, Long userId) {
        if (tripDto == null || tripDto.getName() == null || tripDto.getCrewId() == null) //tripDate might be null
            throw new NullArgumentException();

        Crew crew = crewService.getCrewEntity(tripDto.getCrewId());
        memberService.assertUserIsMember(userId, crew.getCrewId());

        return tripRepository.save(Trip.builder()
                        .name(tripDto.getName())
                        .tripDate(tripDto.getTripDate())
                        .crew(crew)
                .build()).getTripId();
    }

    public TripDto getTrip(Long tripId, Long userId) {
        Optional<Trip> trip = tripRepository.findTripByTripId(tripId);
        if (trip.isEmpty())
            throw new TripCoreException(HttpStatus.NOT_FOUND,"Given trip wasn't found");

        memberService.assertUserIsMember(userId, trip.get().getCrew().getCrewId());

        return TripDto.builder()
                .tripId(trip.get().getTripId())
                .crewId(trip.get().getCrew().getCrewId())
                .name(trip.get().getName())
                .tripDate(trip.get().getTripDate())
                .build();
    }
}
