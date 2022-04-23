package com.cleverpy.moviesAPI.services.productionCountry;

import com.cleverpy.moviesAPI.dto.ProductionCountryDto;
import com.cleverpy.moviesAPI.entities.ProductionCountry;
import com.cleverpy.moviesAPI.repositories.ProductionCountryRepository;
import com.cleverpy.moviesAPI.security.payload.MessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductionCountryServiceImplTest {

    @Mock
    private ProductionCountryRepository repository;

    @InjectMocks
    private ProductionCountryServiceImpl underTest;

    @BeforeEach
    void setUp() {
        repository = mock(ProductionCountryRepository.class);
        underTest = new ProductionCountryServiceImpl(repository);
    }

    @Test
    void create() {
        final ProductionCountryDto dto = new ProductionCountryDto("br", "Brasil");

        final ResponseEntity<?> expected = underTest.create(dto);

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(ProductionCountry.class);
        assertThat(expected.getBody()).asString().contains("name=Brasil");
        assertThat(expected.getBody()).asString().contains("iso=br");
    }

    @Test
    void shouldNotCreateDuplicatedName() {
        final ProductionCountryDto dto = new ProductionCountryDto("br", "Brasil");

        when(repository.existsByName(dto.getName())).thenReturn(true);
        final ResponseEntity<?> expected = underTest.create(dto);

        assertThat(expected.getStatusCodeValue()).isEqualTo(400);
        assertThat(expected.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(expected.getBody()).asString().contains("The production company " + dto.getName() + " is already registered");
    }

    @Test
    void shouldNotCreateDuplicatedIso() {
        final ProductionCountryDto dto = new ProductionCountryDto("br", "Brasil");

        when(repository.existsByIso(dto.getIso())).thenReturn(true);
        final ResponseEntity<?> expected = underTest.create(dto);

        assertThat(expected.getStatusCodeValue()).isEqualTo(400);
        assertThat(expected.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(expected.getBody()).asString().contains("The iso_3166_1 " + dto.getIso() + " is already registered");
    }

    @Test
    void getById() {
        final ProductionCountry country = new ProductionCountry(1L, "br", "Brasil");

        when(repository.findById(1L)).thenReturn(Optional.of(country));
        final ResponseEntity<?> expected = underTest.getById(1L);

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(ProductionCountry.class);
        assertThat(expected.getBody()).asString().contains("id=1");
        assertThat(expected.getBody()).asString().contains("name=Brasil");
        assertThat(expected.getBody()).asString().contains("iso=br");
    }

    @Test
    void getAll() {
        final ProductionCountry country1 = new ProductionCountry(1L, "br", "Brasil");
        final ProductionCountry country2 = new ProductionCountry(2L, "us", "USA");
        final ProductionCountry country3 = new ProductionCountry(3L, "ch", "China");

        List<ProductionCountry> countries = new ArrayList<>();
        countries.add(country1);
        countries.add(country2);
        countries.add(country3);

        final Pageable page = PageRequest.of(0, 10);
        final Page<ProductionCountry> result = new PageImpl<>(countries);

        when(repository.findAll(page)).thenReturn(result);
        ResponseEntity<?> expected = underTest.getAll(0);

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(Page.class);
    }

    @Test
    void getAllNull() {
        final Pageable page = PageRequest.of(0, 10);

        when(repository.findAll(page)).thenReturn(null);
        ResponseEntity<?> expected = underTest.getAll(0);

        assertThat(expected.getStatusCodeValue()).isEqualTo(204);
    }

    @Test
    void update() {
        final ProductionCountry country = new ProductionCountry(1L, "br", "Brasil");
        final ProductionCountryDto dto = new ProductionCountryDto("sp", "Spain");

        when(repository.findById(1L)).thenReturn(Optional.of(country));
        final ResponseEntity<?> expected = underTest.update(1L, dto);

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(ProductionCountry.class);
        assertThat(expected.getBody()).asString().contains("id=1");
        assertThat(expected.getBody()).asString().contains("name=Spain");
        assertThat(expected.getBody()).asString().contains("iso=sp");
    }

    @Test
    void shouldNotUpdateDuplicatedName() {
        final ProductionCountryDto dto = new ProductionCountryDto("sp", "Spain");
        final ProductionCountry country = new ProductionCountry(1L, "br", "Brasil");

        when(repository.findById(1L)).thenReturn(Optional.of(country));
        when(repository.existsByName(dto.getName())).thenReturn(true);
        final ResponseEntity<?> expected = underTest.update(1L, dto);

        assertThat(expected.getStatusCodeValue()).isEqualTo(400);
        assertThat(expected.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(expected.getBody()).asString().contains("The name " + dto.getName() + " is already registered");
    }

    @Test
    void shouldNotUpdateDuplicatedIso() {
        final ProductionCountryDto dto = new ProductionCountryDto("sp", "Spain");
        final ProductionCountry country = new ProductionCountry(1L, "br", "Brasil");

        when(repository.findById(1L)).thenReturn(Optional.of(country));
        when(repository.existsByIso(dto.getIso())).thenReturn(true);
        final ResponseEntity<?> expected = underTest.update(1L, dto);

        assertThat(expected.getStatusCodeValue()).isEqualTo(400);
        assertThat(expected.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(expected.getBody()).asString().contains("The iso_3166_1 " + dto.getIso() + " is already registered");
    }

    @Test
    void delete() {
        final ProductionCountry country = new ProductionCountry(1L, "br", "Brasil");

        when(repository.findById(1L)).thenReturn(Optional.of(country));
        final ResponseEntity<?> expected = underTest.delete(1L);

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(expected.getBody()).asString().contains("Production Country " + country.getId() + " deleted with success");
    }
}