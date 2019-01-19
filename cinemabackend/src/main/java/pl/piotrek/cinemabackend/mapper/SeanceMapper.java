package pl.piotrek.cinemabackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.piotrek.cinema.api.dto.SeanceDTO;
import pl.piotrek.cinemabackend.model.Seance;


@Mapper(componentModel = "spring")
public interface SeanceMapper {
    SeanceMapper INSTANCE = Mappers.getMapper(SeanceMapper.class);

    SeanceDTO seanceToSeanceDto(Seance seance);
    Seance seanceDtoToToSeance(SeanceDTO seanceDTO);
}
