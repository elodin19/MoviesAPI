package com.cleverpy.moviesAPI.services.genre;

import com.cleverpy.moviesAPI.dto.GenreDto;
import com.cleverpy.moviesAPI.entities.Genre;
import com.cleverpy.moviesAPI.repositories.GenreRepository;
import com.cleverpy.moviesAPI.security.payload.MessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreServiceImplTest {

    @Mock
    private GenreRepository repository;

    @InjectMocks
    private GenreServiceImpl underTest;

    @BeforeEach
    void setUp() {
        repository = mock(GenreRepository.class);
        underTest = new GenreServiceImpl(repository);
    }

    @Test
    void create() {
        final GenreDto dto = new GenreDto("Action");

        final ResponseEntity<?> expected = underTest.create(dto);

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(Genre.class);
        assertThat(expected.getBody()).asString().contains("name=Action");
    }

    @Test
    void shouldNotCreateDuplicated() {
        final GenreDto dto = new GenreDto("Action");

        when(repository.existsByName(dto.getName())).thenReturn(true);
        final ResponseEntity<?> expected = underTest.create(dto);

        assertThat(expected.getStatusCodeValue()).isEqualTo(400);
        assertThat(expected.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(expected.getBody()).asString().contains("The genre " + dto.getName() + " is already registered");

    }

    @Test
    void getById() {
        final Genre genre = new Genre(1L, "Action");

        when(repository.findById(1L)).thenReturn(Optional.of(genre));
        final ResponseEntity<?> expected = underTest.getById(1L);

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(Genre.class);
        assertThat(expected.getBody()).asString().contains("id=1");
        assertThat(expected.getBody()).asString().contains("name=Action");
    }

    @Test
    void getAll() {
        final Genre genre1 = new Genre(1L, "Action");
        final Genre genre2 = new Genre(2L, "Comedy");
        final Genre genre3 = new Genre(3L, "Horror");

        List<Genre> genres = new ArrayList<>();
        genres.add(genre1);
        genres.add(genre2);
        genres.add(genre3);

        final Pageable page = PageRequest.of(0, 10);
        final Page<Genre> result = new PageImpl<>(genres);

        when(repository.findAll(page)).thenReturn(result);
        ResponseEntity<?> expected = underTest.getAll(0);

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(Page.class);
    }

    @Test
    void update() {
        final Genre genre = new Genre(1L, "Action");
        final GenreDto dto = new GenreDto("Comedy");

        when(repository.findById(1L)).thenReturn(Optional.of(genre));
        final ResponseEntity<?> expected = underTest.update(1L, dto);

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(Genre.class);
        assertThat(expected.getBody()).asString().contains("id=1");
        assertThat(expected.getBody()).asString().contains("name=Comedy");
    }

    @Test
    void shouldNotUpdateDuplicated(){
        final GenreDto dto = new GenreDto("Comedy");

        when(repository.existsByName(dto.getName())).thenReturn(true);
        final ResponseEntity<?> expected = underTest.update(1L, dto);

        assertThat(expected.getStatusCodeValue()).isEqualTo(400);
        assertThat(expected.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(expected.getBody()).asString().contains("The genre " + dto.getName() + " is already registered");
    }

    @Test
    void delete() {
        final Genre genre = new Genre(1L, "Action");

        when(repository.findById(1L)).thenReturn(Optional.of(genre));
        final ResponseEntity<?> expected = underTest.delete(1L);

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(expected.getBody()).asString().contains("Genre " + genre.getId() + " deleted with success");

    }
}