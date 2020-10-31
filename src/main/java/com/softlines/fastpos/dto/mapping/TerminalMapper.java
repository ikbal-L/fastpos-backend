package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.Annex;
import com.softlines.fastpos.domain.Terminal;
import com.softlines.fastpos.dto.TerminalDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TerminalMapper {

    TerminalMapper INSTANCE = Mappers.getMapper(TerminalMapper.class);

    @Mapping(source = "annex", target = "annexId", qualifiedByName = "AnnexToId")
    TerminalDto toTerminalDto(Terminal Terminal);

    List<TerminalDto> toTerminalDTOs(List<Terminal> Terminal);

    @Mapping(source = "annexId", target = "annex", qualifiedByName = "IdToAnnex")
    Terminal toTerminal(TerminalDto TerminalDTO);

    @Named("AnnexToId")
    public static long AnnexToId(Annex annex) {
        return annex.getId();
    }

    @Named("IdToAnnex")
    public static Annex IdToAnnex(long id ) {
        Annex annex = new Annex();
        annex.setId(id);
        return annex;
    }

}
