package ca.mcgill.ecse321.boardr.integration;

import ca.mcgill.ecse321.boardr.dto.BoardGame.BoardGameCreationDTO;
import ca.mcgill.ecse321.boardr.dto.BoardGame.BoardGameResponseDTO;
import ca.mcgill.ecse321.boardr.repo.BoardGameRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the BoardGame API.
 * This class tests the creation, retrieval, and deletion of board games.
 * 
 * @author Jione Ban
 * @date 2025-03-19
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BoardGameIntegrationTest {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private BoardGameRepository boardGameRepository;

    final String boardGameName1 = "Catan";
    final String boardGameDescription1 = "A fun game";
    final String boardGameName2 = "Dominion";
    final String boardGameDescription2 = "A deck building game";

    /**
     * Clears the database before and after each test.
     */
    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        boardGameRepository.deleteAll();
    }

    /**
     * Tests the creation of a board game.
     * Verifies that the board game is created successfully and the response contains the correct data.
     */
    @Test
    public void testCreateBoardGame() {
        // arrange
        BoardGameCreationDTO requestDTO = new BoardGameCreationDTO(boardGameName1, boardGameDescription1);

        // act
        ResponseEntity<BoardGameResponseDTO> response = client.postForEntity("/boardgames", requestDTO, BoardGameResponseDTO.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        BoardGameResponseDTO createdGame = response.getBody();
        assertNotNull(createdGame);
        assertEquals(boardGameName1, createdGame.getName());
        assertEquals(boardGameDescription1, createdGame.getDescription());
        assertTrue(createdGame.getGameId() > 0);
    }

    /**
     * Tests the retrieval of all board games.
     * Verifies that the correct board games are returned and their data matches the expected values.
     */
    @Test
    public void testGetBoardGames() {
        // arrange
        BoardGameCreationDTO requestDTO = new BoardGameCreationDTO(boardGameName1, boardGameDescription1);
        ResponseEntity<BoardGameResponseDTO> response = client.postForEntity("/boardgames", requestDTO, BoardGameResponseDTO.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        requestDTO = new BoardGameCreationDTO(boardGameName2, boardGameDescription2);
        response = client.postForEntity("/boardgames", requestDTO, BoardGameResponseDTO.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // act
        ResponseEntity<BoardGameResponseDTO[]> responseEntity = client.getForEntity("/boardgames", BoardGameResponseDTO[].class);

        // assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        List<BoardGameResponseDTO> responseList = Arrays.asList(responseEntity.getBody());

        assertTrue(responseList.size() >= 2);
        List<String> gameNames = responseList.stream().map(BoardGameResponseDTO::getName).toList();
        assertTrue(gameNames.contains(boardGameName1));
        assertTrue(gameNames.contains(boardGameName2));
    }

    /**
     * Tests the retrieval of a specific board game by its ID.
     * Verifies that the correct board game is returned and its data matches the expected values.
     */
    @Test
    public void testGetSpecificBoardGames() {
        // arrange
        BoardGameCreationDTO requestDTO = new BoardGameCreationDTO(boardGameName1, boardGameDescription1);
        ResponseEntity<BoardGameResponseDTO> response = client.postForEntity("/boardgames", requestDTO, BoardGameResponseDTO.class);
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        BoardGameResponseDTO createdGame = response.getBody();
        assertNotNull(createdGame);
        int idToFetch = createdGame.getGameId();

        // act
        response = client.getForEntity("/boardgames/{id}", BoardGameResponseDTO.class, idToFetch);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BoardGameResponseDTO fetchedGame = response.getBody();
        assertNotNull(fetchedGame);
        assertEquals(boardGameName1, fetchedGame.getName());
        assertEquals(boardGameDescription1, fetchedGame.getDescription());
        assertEquals(idToFetch, fetchedGame.getGameId());
    }

    /**
     * Tests the deletion of a board game by its ID.
     * Verifies that the board game is deleted successfully and the response status is OK.
     */
    @Test
    public void testDeleteBoardGame() {
        // arrange
        BoardGameCreationDTO requestDTO = new BoardGameCreationDTO(boardGameName1, boardGameDescription1);
        ResponseEntity<BoardGameResponseDTO> response = client.postForEntity("/boardgames", requestDTO, BoardGameResponseDTO.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        int idToFetch = response.getBody().getGameId();

        // act
        ResponseEntity<Void> deleteResponse = client.exchange("/boardgames/{id}", HttpMethod.DELETE, null, Void.class, idToFetch);

        // assert
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
    }
}
