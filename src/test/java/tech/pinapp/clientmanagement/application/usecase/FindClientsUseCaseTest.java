package tech.pinapp.clientmanagement.application.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import tech.pinapp.clientmanagement.application.dto.ClientFilter;
import tech.pinapp.clientmanagement.application.dto.ClientListResponse;
import tech.pinapp.clientmanagement.application.dto.ClientResponse;
import tech.pinapp.clientmanagement.application.mapper.ClientMapper;
import tech.pinapp.clientmanagement.domain.model.Client;
import tech.pinapp.clientmanagement.domain.model.ClientSpecification;
import tech.pinapp.clientmanagement.domain.repository.ClientRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindClientsUseCaseTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientMapper clientMapper;
    @Mock
    private ClientSpecification clientSpecification;

    @InjectMocks
    private FindClientsUseCase findClientsUseCase;

    private ClientFilter filter;
    private Pageable pageable;
    private Specification<Client> mockSpec;

    @BeforeEach
    void setUp() {
        filter = ClientFilter.builder().firstName("John").age(30).build();
        pageable = PageRequest.of(0, 10);
        mockSpec = mock(Specification.class); // Mock the Specification object

        // Common mock for clientSpecification
        when(clientSpecification.fromFilter(any(ClientFilter.class))).thenReturn(mockSpec);
    }

    @Test
    @DisplayName("Should return a paginated list of clients when clients are found")
    void execute_whenClientsFound_shouldReturnClientListResponse() {
        // Arrange
        Client client1 = Client.builder().id(1L).firstName("John").lastName("Doe").age(30).dateOfBirth(LocalDate.of(1993, 1, 1)).build();
        Client client2 = Client.builder().id(2L).firstName("Jane").lastName("Smith").age(30).dateOfBirth(LocalDate.of(1993, 2, 2)).build();
        List<Client> clients = Arrays.asList(client1, client2);
        Page<Client> clientPage = new PageImpl<>(clients, pageable, 2);
        LocalDate estimatedDeathDate1 = LocalDate.of(2078, 1, 1); // 1993 + 85
        LocalDate estimatedDeathDate2 = LocalDate.of(2078, 2, 2); // 1993 + 85

        ClientResponse clientResponse1 = ClientResponse.builder().id(1L).firstName("John").lastName("Doe").age(30).dateOfBirth(LocalDate.of(1993, 1, 1)).estimatedDeathDate(estimatedDeathDate1).build();
        ClientResponse clientResponse2 = ClientResponse.builder().id(2L).firstName("Jane").lastName("Smith").age(30).dateOfBirth(LocalDate.of(1993, 2, 2)).estimatedDeathDate(estimatedDeathDate2).build();

        when(clientRepository.findAll(mockSpec, pageable)).thenReturn(clientPage);
        when(clientMapper.toClientResponse(client1)).thenReturn(clientResponse1);
        when(clientMapper.toClientResponse(client2)).thenReturn(clientResponse2);

        // Act
        ClientListResponse result = findClientsUseCase.execute(filter, pageable);


        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getClients()).hasSize(2);
        assertThat(result.getClients().get(0).getFirstName()).isEqualTo("John");
        assertThat(result.getClients().get(0).getEstimatedDeathDate()).isEqualTo(estimatedDeathDate1);
        assertThat(result.getCurrentPage()).isEqualTo(0);
        assertThat(result.getTotalItems()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(1);

        // Verify interactions
        verify(clientSpecification, times(1)).fromFilter(filter);
        verify(clientRepository, times(1)).findAll(mockSpec, pageable);
        verify(clientMapper, times(1)).toClientResponse(client1);
        verify(clientMapper, times(1)).toClientResponse(client2);
    }

    @Test
    @DisplayName("Should return an empty paginated list when no clients are found")
    void execute_whenNoClientsFound_shouldReturnEmptyClientListResponse() {
        // Arrange
        Page<Client> emptyClientPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(clientRepository.findAll(mockSpec, pageable)).thenReturn(emptyClientPage);

        // Act
        ClientListResponse result = findClientsUseCase.execute(filter, pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getClients()).isEmpty();
        assertThat(result.getCurrentPage()).isEqualTo(0);
        assertThat(result.getTotalItems()).isEqualTo(0);
        assertThat(result.getTotalPages()).isEqualTo(0);

        // Verify interactions
        verify(clientSpecification, times(1)).fromFilter(filter);
        verify(clientRepository, times(1)).findAll(mockSpec, pageable);
    }

    @Test
    @DisplayName("Should handle null filter gracefully and return clients")
    void execute_whenNullFilter_shouldReturnClientListResponse() {
        // Arrange
        filter = null; // Test with null filter
        Specification<Client> allClientsSpec = mock(Specification.class);
        when(clientSpecification.fromFilter(null)).thenReturn(allClientsSpec); // Expect null filter

        Client client1 = Client.builder().id(1L).firstName("John").lastName("Doe").age(30).dateOfBirth(LocalDate.of(1993, 1, 1)).build();
        LocalDate estimatedDeathDate1 = LocalDate.of(2078, 1, 1); // 1993 + 85
        List<Client> clients = Collections.singletonList(client1);
        Page<Client> clientPage = new PageImpl<>(clients, pageable, 1);
        ClientResponse clientResponse1 = ClientResponse.builder().id(1L).firstName("John").lastName("Doe").age(30).dateOfBirth(LocalDate.of(1993, 1, 1)).estimatedDeathDate(estimatedDeathDate1).build();

        when(clientRepository.findAll(allClientsSpec, pageable)).thenReturn(clientPage);
        when(clientMapper.toClientResponse(client1)).thenReturn(clientResponse1);

        // Act
        ClientListResponse result = findClientsUseCase.execute(filter, pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getClients()).hasSize(1);
        assertThat(result.getClients().get(0).getFirstName()).isEqualTo("John");
        assertThat(result.getClients().get(0).getEstimatedDeathDate()).isEqualTo(estimatedDeathDate1);

        // Verify interactions
        verify(clientSpecification, times(1)).fromFilter(null);
        verify(clientRepository, times(1)).findAll(allClientsSpec, pageable);
        verify(clientMapper, times(1)).toClientResponse(client1);
    }
}