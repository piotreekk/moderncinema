package pl.piotrek.cinema.util.converter;

import pl.piotrek.cinema.api.dto.SeanceDTO;
import pl.piotrek.cinema.model.fx.SeanceFx;

public class SeanceConverter {
    public SeanceDTO SeanceToSeanceDto(){
        return null;
    }

    public static SeanceFx seanceDtoToSeance(SeanceDTO seanceDTO){
        SeanceFx seanceFx = new SeanceFx();
        seanceFx.setId(seanceDTO.getId());
        seanceFx.setMovieTitle(seanceDTO.getMovie().getTitle());
        seanceFx.setPosterImage(seanceDTO.getMovie().getPosterPath());
        seanceFx.setAllSeatsCount(seanceDTO.getAuditorium().getRows()*seanceDTO.getAuditorium().getCols());
        seanceFx.setDate(seanceDTO.getDate());
        seanceFx.setStartTime(seanceDTO.getStartTime());

        return seanceFx;
    }
}
