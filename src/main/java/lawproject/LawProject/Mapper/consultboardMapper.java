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
    @Mapping(source = "title", target = "title")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "writer", target = "writer")
    @Mapping(source = "date", target = "date")
    consultboardEntity dtoToEntity(consultboardDTO dto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "writer", target = "writer")
    @Mapping(source = "date", target = "date")
    consultboardDTO entityToDto(consultboardEntity entity);
}
