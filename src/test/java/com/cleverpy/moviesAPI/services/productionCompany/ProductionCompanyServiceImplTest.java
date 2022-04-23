package com.cleverpy.moviesAPI.services.productionCompany;

import com.cleverpy.moviesAPI.dto.ProductionCompanyDto;
import com.cleverpy.moviesAPI.entities.ProductionCompany;
import com.cleverpy.moviesAPI.repositories.ProductionCompanyRepository;
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
class ProductionCompanyServiceImplTest {

    @Mock
    private ProductionCompanyRepository repository;

    @InjectMocks
    private ProductionCompanyServiceImpl underTest;

    @BeforeEach
    void setUp() {
        repository = mock(ProductionCompanyRepository.class);
        underTest = new ProductionCompanyServiceImpl(repository);
    }

    @Test
    void create() {
        final ProductionCompanyDto dto = new ProductionCompanyDto("Pixar", "logo.png", "USA");

        final ResponseEntity<?> expected = underTest.create(dto);

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(ProductionCompany.class);
        assertThat(expected.getBody()).asString().contains("name=Pixar");
        assertThat(expected.getBody()).asString().contains("logoPath=logo.png");
        assertThat(expected.getBody()).asString().contains("originCountry=USA");
    }

    @Test
    void shouldNotCreateDuplicatedName() {
        final ProductionCompanyDto dto = new ProductionCompanyDto("Pixar", "logo.png", "USA");

        when(repository.existsByName(dto.getName())).thenReturn(true);
        final ResponseEntity<?> expected = underTest.create(dto);

        assertThat(expected.getStatusCodeValue()).isEqualTo(400);
        assertThat(expected.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(expected.getBody()).asString().contains("The production company " + dto.getName() + " is already registered");
    }

    @Test
    void shouldNotCreateDuplicatedLogo() {
        final ProductionCompanyDto dto = new ProductionCompanyDto("Pixar", "logo.png", "USA");

        when(repository.existsByLogoPath(dto.getLogo_path())).thenReturn(true);
        final ResponseEntity<?> expected = underTest.create(dto);

        assertThat(expected.getStatusCodeValue()).isEqualTo(400);
        assertThat(expected.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(expected.getBody()).asString().contains("The logo path " + dto.getLogo_path() + " is already registered");
    }

    @Test
    void getById() {
        final ProductionCompany company = new ProductionCompany(1L, "Pixar", "logo.png", "USA");

        when(repository.findById(1L)).thenReturn(Optional.of(company));
        final ResponseEntity<?> expected = underTest.getById(1L);

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(ProductionCompany.class);
        assertThat(expected.getBody()).asString().contains("id=1");
        assertThat(expected.getBody()).asString().contains("name=Pixar");
        assertThat(expected.getBody()).asString().contains("logoPath=logo.png");
        assertThat(expected.getBody()).asString().contains("originCountry=USA");
    }

    @Test
    void getAll() {
        final ProductionCompany company1 = new ProductionCompany(1L, "Pixar", "logo.jpg", "USA");
        final ProductionCompany company2 = new ProductionCompany(2L, "Dreamworks", "logo.png", "USA");
        final ProductionCompany company3 = new ProductionCompany(3L, "Marvel", "logo.jpeg", "USA");

        List<ProductionCompany> companies = new ArrayList<>();
        companies.add(company1);
        companies.add(company2);
        companies.add(company3);

        final Pageable page = PageRequest.of(0, 10);
        final Page<ProductionCompany> result = new PageImpl<>(companies);

        when(repository.findAll(page)).thenReturn(result);
        ResponseEntity<?> expected = underTest.getAll(0);

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(Page.class);
    }

    @Test
    void getAllNull(){
        final Pageable page = PageRequest.of(0, 10);

        when(repository.findAll(page)).thenReturn(null);
        ResponseEntity<?> expected = underTest.getAll(0);

        assertThat(expected.getStatusCodeValue()).isEqualTo(204);
    }

    @Test
    void update() {
        final ProductionCompany company = new ProductionCompany(1L, "Dreamworks", "logo.png", "USA");
        final ProductionCompanyDto dto = new ProductionCompanyDto("Pixar", "logo.jpg", "Canada");

        when(repository.findById(1L)).thenReturn(Optional.of(company));
        final ResponseEntity<?> expected = underTest.update(1L, dto);

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(ProductionCompany.class);
        assertThat(expected.getBody()).asString().contains("id=1");
        assertThat(expected.getBody()).asString().contains("name=Pixar");
        assertThat(expected.getBody()).asString().contains("logoPath=logo.jpg");
        assertThat(expected.getBody()).asString().contains("originCountry=Canada");
    }

    @Test
    void shouldNotUpdateDuplicatedName() {
        final ProductionCompanyDto dto = new ProductionCompanyDto("Dreamworks", "logo.png", "Canada");
        final ProductionCompany company = new ProductionCompany(1L, "Pixar", "logo.jpg", "USA");

        when(repository.findById(1L)).thenReturn(Optional.of(company));
        when(repository.existsByName(dto.getName())).thenReturn(true);
        final ResponseEntity<?> expected = underTest.update(1L, dto);

        assertThat(expected.getStatusCodeValue()).isEqualTo(400);
        assertThat(expected.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(expected.getBody()).asString().contains("The production company " + dto.getName() + " is already registered");
    }

    @Test
    void shouldNotUpdateDuplicatedLogo(){
        final ProductionCompanyDto dto = new ProductionCompanyDto("Dreamworks", "logo.png", "USA");
        final ProductionCompany company = new ProductionCompany(1L, "Pixar", "logo.jpg", "USA");

        when(repository.findById(1L)).thenReturn(Optional.of(company));
        when(repository.existsByLogoPath(dto.getLogo_path())).thenReturn(true);
        final ResponseEntity<?> expected = underTest.update(1L, dto);

        assertThat(expected.getStatusCodeValue()).isEqualTo(400);
        assertThat(expected.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(expected.getBody()).asString().contains("The logo path " + dto.getLogo_path() + " is already registered");
    }

    @Test
    void delete() {
        final ProductionCompany company = new ProductionCompany(1L, "Dreamworks", "logo.png", "USA");

        when(repository.findById(1L)).thenReturn(Optional.of(company));
        final ResponseEntity<?> expected = underTest.delete(1L);

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(expected.getBody()).asString().contains("Production Company " + company.getId() + " deleted with success");
    }
}