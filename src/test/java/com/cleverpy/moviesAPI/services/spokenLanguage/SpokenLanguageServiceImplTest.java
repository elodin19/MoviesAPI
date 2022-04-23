package com.cleverpy.moviesAPI.services.spokenLanguage;

import com.cleverpy.moviesAPI.dto.SpokenLanguageDto;
import com.cleverpy.moviesAPI.entities.SpokenLanguage;
import com.cleverpy.moviesAPI.repositories.SpokenLanguageRepository;
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
class SpokenLanguageServiceImplTest {

    @Mock
    private SpokenLanguageRepository repository;

    @InjectMocks
    private SpokenLanguageServiceImpl underTest;

    @BeforeEach
    void setUp() {
        repository = mock(SpokenLanguageRepository.class);
        underTest = new SpokenLanguageServiceImpl(repository);
    }

    @Test
    void create() {
        final SpokenLanguageDto dto = new SpokenLanguageDto("Spanish", "es", "Spanish");

        final ResponseEntity<?> expected = underTest.create(dto);

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(SpokenLanguage.class);
        assertThat(expected.getBody()).asString().contains("english_name=Spanish");
        assertThat(expected.getBody()).asString().contains("iso=es");
        assertThat(expected.getBody()).asString().contains("name=Spanish");
    }

    @Test
    void getById() {
        final SpokenLanguage language = new SpokenLanguage(1L, "Spanish", "es", "Spanish");

        when(repository.findById(1L)).thenReturn(Optional.of(language));
        final ResponseEntity<?> expected = underTest.getById(1L);

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(SpokenLanguage.class);
        assertThat(expected.getBody()).asString().contains("id=1");
        assertThat(expected.getBody()).asString().contains("english_name=Spanish");
        assertThat(expected.getBody()).asString().contains("iso=es");
        assertThat(expected.getBody()).asString().contains("name=Spanish");
    }

    @Test
    void getAll() {
        final SpokenLanguage language1 = new SpokenLanguage(1L, "Spanish", "es", "Spanish");
        final SpokenLanguage language2 = new SpokenLanguage(2L, "Chinese", "ch", "Chinese");
        final SpokenLanguage language3 = new SpokenLanguage(3L, "Japanese", "jp", "Japanese");

        List<SpokenLanguage> languages = new ArrayList<>();
        languages.add(language1);
        languages.add(language2);
        languages.add(language3);

        final Pageable page = PageRequest.of(0, 10);
        final Page<SpokenLanguage> result = new PageImpl<>(languages);

        when(repository.findAll(page)).thenReturn(result);
        ResponseEntity<?> expected = underTest.getAll(0);

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(Page.class);
    }

    @Test
    void update() {
        final SpokenLanguage language = new SpokenLanguage(1L, "Spanish", "es", "Spanish");
        final SpokenLanguageDto dto = new SpokenLanguageDto("English", "en", "English");

        when(repository.findById(1L)).thenReturn(Optional.of(language));
        final ResponseEntity<?> expected = underTest.update(1L, dto);

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(SpokenLanguage.class);
        assertThat(expected.getBody()).asString().contains("id=1");
        assertThat(expected.getBody()).asString().contains("english_name=English");
        assertThat(expected.getBody()).asString().contains("iso=en");
        assertThat(expected.getBody()).asString().contains("name=English");
    }

    @Test
    void delete() {
        final SpokenLanguage language = new SpokenLanguage(1L, "Spanish", "es", "Spanish");

        when(repository.findById(1L)).thenReturn(Optional.of(language));
        final ResponseEntity<?> expected = underTest.delete(1L);

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(expected.getBody()).asString().contains("Spoken Language " + language.getId() + " deleted with success");

    }
}