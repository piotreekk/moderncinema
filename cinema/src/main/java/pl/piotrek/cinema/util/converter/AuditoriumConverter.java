package pl.piotrek.cinema.util.converter;

import pl.piotrek.cinema.api.dto.AuditoriumDTO;
import pl.piotrek.cinema.model.fx.AuditoriumFx;

public class AuditoriumConverter {
    public static AuditoriumFx auditoriumDtoToAuditorium(AuditoriumDTO a){
        AuditoriumFx auditoriumFx = new AuditoriumFx();
        auditoriumFx.setId(a.getId());
        auditoriumFx.setName(a.getName());
        auditoriumFx.setRows(a.getRows());
        auditoriumFx.setCols(a.getCols());

        return auditoriumFx;
    }

    public static AuditoriumDTO auditoriumToAuditoriumDto(AuditoriumFx auditoriumFx){
        AuditoriumDTO auditoriumDTO = new AuditoriumDTO();
        auditoriumDTO.setId(auditoriumFx.getId());
        auditoriumDTO.setName(auditoriumFx.getName());
        auditoriumDTO.setRows(auditoriumFx.getRows());
        auditoriumDTO.setCols(auditoriumFx.getCols());

        return auditoriumDTO;
    }
}
