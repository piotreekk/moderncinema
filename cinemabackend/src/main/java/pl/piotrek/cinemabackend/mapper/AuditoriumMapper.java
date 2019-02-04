package pl.piotrek.cinemabackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.piotrek.cinema.api.dto.AuditoriumDTO;
import pl.piotrek.cinemabackend.model.Auditorium;


@Mapper(componentModel = "spring")
public interface AuditoriumMapper {
    AuditoriumMapper INSTANCE = Mappers.getMapper(AuditoriumMapper.class);

    AuditoriumDTO auditoriumToAuditoriumDto(Auditorium auditorium);
    Auditorium auditoriumDtoToAuditorium(AuditoriumDTO auditoriumDTO);
}
