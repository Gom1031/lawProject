package lawproject.LawProject.Mapper;

import lawproject.LawProject.DTO.consultboardDTO;
import lawproject.LawProject.Entity.consultboardEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface consultboardMapper {

    consultboardMapper INSTANCE = Mappers.getMapper(consultboardMapper.class);

    @Mapping(source = "id", target = "id")
    consultboardEntity dtoToEntity(consultboardDTO dto);

    consultboardDTO entityToDto(consultboardEntity entity);
}