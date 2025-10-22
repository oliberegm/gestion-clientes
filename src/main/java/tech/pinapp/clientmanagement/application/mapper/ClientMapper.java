package tech.pinapp.clientmanagement.application.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tech.pinapp.clientmanagement.application.dto.ClientCreationRequest;
import tech.pinapp.clientmanagement.application.dto.ClientResponse;
import tech.pinapp.clientmanagement.application.dto.ClientUpdateRequest;
import tech.pinapp.clientmanagement.application.usecase.CalculatedEstimatedDeathDateUseCase;
import tech.pinapp.clientmanagement.domain.model.Client;

/**
 * Mapper for the entity {@link Client} and its DTO {@link ClientResponse}.
 */
@Mapper(componentModel = "spring",
    uses = {CalculatedEstimatedDeathDateUseCase.class},
    builder = @Builder(disableBuilder = true))
public interface ClientMapper {

    @Mapping(source = "dateOfBirth", target = "estimatedDeathDate", qualifiedByName = "calculateEstimatedDeathDate")
    @Mapping(target = "dateOfBirth")
    ClientResponse toClientResponse(Client client);

    @Mapping(target = "id", source = "existingClient.id")
    @Mapping(target = "firstName", source = "request.firstName")
    @Mapping(target = "lastName", source = "request.lastName")
    @Mapping(target = "age", source = "request.age")
    @Mapping(target = "dateOfBirth", source = "request.dateOfBirth")
    Client toClientEntity(ClientUpdateRequest request, Client existingClient);

    @Mapping(target = "id", ignore = true)
    Client toClientEntity(ClientCreationRequest request);
}