package com.example.checkscam.service.mapper;

import com.example.checkscam.dto.request.ReportRequestDto;
import com.example.checkscam.dto.response.ReportResponseDto;
import com.example.checkscam.entity.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    @Mapping(target = "id", source = "id", conditionExpression = "java(eqaPlanningRequestDto.getId() != null)")
    Report requestToEntity(ReportRequestDto reportRequestDto);

    @Mapping(source = "id", target = "id", ignore = true)
    void setValue(ReportRequestDto dto, Report entity);

    ReportResponseDto entityToResponse(Report report);
}
