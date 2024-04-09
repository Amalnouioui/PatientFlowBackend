package com.core.Parameterization.Controllers;

import com.core.Parameterization.Entities.*;
import com.core.Parameterization.Entities.Enumeration.*;
import com.core.Parameterization.Respositories.CareUnitRepository;
import com.core.Parameterization.Respositories.RoomRepository;
import com.core.Parameterization.Services.CareUnitService;
import com.core.Parameterization.Services.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BedControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    private RoomRepository roomRepository;
    private CareUnitRepository careUnitRepository;
    private CareUnitService careUnitService;
    private RoomService roomService;

    private Bed bed;
    private  Room room;
    private  Equipment equipment;
    @BeforeEach
    void setUp() {
        bed = new Bed();
        room=new Room();
        equipment=new Equipment();

        restTemplate = new TestRestTemplate();
        bed.setBedKey(bed.getBedKey());
        bed.setBedNumber(101);
        bed.setBedDescription("new bed  ");
        bed.setBedType(BedType.Double);
        bed.setBedStatue(BedStatus.En_Maintenance);
        bed.setPoids(80);
        bed.setPhysicalState(BedPhysicalCondition.Bon_Etat);
        long currentTimeMillis = System.currentTimeMillis();
        bed.setExpirationDate(new Timestamp(currentTimeMillis));
        bed.setBedPurchaseDate(new Timestamp(currentTimeMillis));
        bed.setRoomBed(this.room);
        List<Long> equipmentIds = Arrays.asList(1L, 3L);
        bed.setEquipmentList(equipmentIds);
    }
    @Test
    void createBed() {
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/parameterization/bed", this.bed, String.class);
        assertEquals(HttpStatus.OK, ((ResponseEntity<?>) response).getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("bed Created", response.getBody());
    }


    @Test
    void getAllBeds() {
        ResponseEntity<List> response = restTemplate.getForEntity("http://localhost:" + port + "/parameterization/bed", List.class);
        System.out.println(response);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void deleteBed() {
        restTemplate.delete("http://localhost:" + port + "/parameterization/bed/1");
        ResponseEntity<Room> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/parameterization/bed/1", Room.class);
        assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());


    }


    @Test
    void getBedByItsType() {
        // Appel de la méthode du contrôleur pour récupérer les unités de soins avec le statut "ACTIVE"
        ResponseEntity<List> response = restTemplate.getForEntity("http://localhost:" + port + "/parameterization/bed/bedType/Simple", List.class);
        System.out.println(response);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void getBedByItsStatue() {
        // Appel de la méthode du contrôleur pour récupérer les unités de soins avec le statut "ACTIVE"
        ResponseEntity<List> response = restTemplate.getForEntity("http://localhost:" + port + "/parameterization/bed/bedStatue/Reserve", List.class);
        System.out.println(response);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void getBedByItsphysical_state() {
        // Appel de la méthode du contrôleur pour récupérer les unités de soins avec le statut "ACTIVE"
        ResponseEntity<List> response = restTemplate.getForEntity("http://localhost:" + port + "/parameterization/bed/BedState/Bon_Etat", List.class);
        System.out.println(response);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

}