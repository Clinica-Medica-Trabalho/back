package com.bcc.soccer.service;

import com.bcc.soccer.dto.StadiumDTO;
import com.bcc.soccer.entity.Stadium;
import com.bcc.soccer.entity.Team;
import com.bcc.soccer.exception.ObjectNotFoundException;
import com.bcc.soccer.repository.StadiumRepository;
import com.bcc.soccer.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StadiumService {

    private final StadiumRepository stadiumRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public StadiumService(StadiumRepository stadiumRepository, TeamRepository teamRepository) {
        this.stadiumRepository = stadiumRepository;
        this.teamRepository = teamRepository;
    }

    public StadiumDTO createStadium(StadiumDTO stadiumDTO) {
        Stadium stadium = new Stadium(stadiumDTO);
        Team team = teamRepository.findByName(stadiumDTO.getTeamName())
                .orElseThrow(() -> new ObjectNotFoundException("Team not exists."));
        stadium.setTeam(team);
        return new StadiumDTO(stadiumRepository.save(stadium));
    }

    public List<StadiumDTO> findAllStadiums() {
        return stadiumRepository.findAll().stream()
                .map(StadiumDTO::new)
                .collect(Collectors.toList());
    }

    public StadiumDTO updateStadium(int id, StadiumDTO stadiumDTO) {
        Stadium dbStadium = stadiumRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Stadium not found."));
        Team dbTeam = teamRepository.findByName(stadiumDTO.getTeamName())
                        .orElseThrow(() -> new ObjectNotFoundException("Team not exists."));
        dbStadium.setId(id);
        dbStadium.setName(stadiumDTO.getName());
        dbStadium.setCapacity(stadiumDTO.getCapacity());
        dbStadium.setTeam(dbTeam);
        return new StadiumDTO(stadiumRepository.save(dbStadium));
    }

    public void deleteStadium(int id) {
        if(!stadiumRepository.existsById(id)) throw new ObjectNotFoundException("Stadium already not found.");
        stadiumRepository.deleteById(id);
    }
}
