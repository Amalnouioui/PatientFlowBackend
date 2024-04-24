package com.core.Parameterization.Controllers;

import com.core.Parameterization.Entities.*;
import com.core.Parameterization.Entities.Enumeration.*;
import com.core.Parameterization.Respositories.CareUnitRepository;
import com.core.Parameterization.Services.CareUnitService;
import com.core.Parameterization.Services.RoomService;
import com.mysql.cj.protocol.Message;
import jakarta.ws.rs.HttpMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.core.ParameterizedTypeReference;

import org.springframework.http.ResponseEntity;
import org.springframework.core.ParameterizedTypeReference;

import java.net.URI;
import java.sql.Timestamp;
import java.util.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CareUnitControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

   @Autowired
   private CareUnitController careUnitController;

    @MockBean
    private CareUnitService careUnitService;
    private CareUnit careUnit;
    private Room room;
    private Bed bed;
    private Equipment equipment;
    @BeforeEach
    void setUp() {
        careUnit = new CareUnit();
        room = new Room();
        bed = new Bed();
        equipment = new Equipment();
        restTemplate = new TestRestTemplate();
        careUnit.setCareunitKey(careUnit.getCareunitKey());
        careUnit.setCareunitName("care3"); // Correction du nom de l'unité de soins
        careUnit.setCareunitDescription("neww careunit for test");
        careUnit.setCareunitResponsable("Doc ahmed");
        careUnit.setCareunitCapacity(5);
        careUnit.setCareuniType(UnitType.Cherugie);
        careUnit.setCareunitStatue(UnitStatus.InACTIVE);
        long currentTimeMillis = System.currentTimeMillis();
        careUnit.setCareUnit_StartTime(new Timestamp(currentTimeMillis));
        careUnit.setCareUnit_EndTime(new Timestamp(currentTimeMillis));
        // Initialisation des identifiants d'équipements et de services
        careUnit.setEquipmentList(new ArrayList<>());
       // careUnit.setServiceList(new ArrayList<>());
        List<Long> equipmentIds = Arrays.asList(1L, 3L);
        List<Long> serviceIds = Arrays.asList(1L, 2L, 3L);

        careUnit.setEquipmentList(equipmentIds);
      //  careUnit.setServiceList(serviceIds);
       // careUnit.setRooms(roomList);

        room.setRoomName(103);
        room.setRoomResponsible("doc flen");
        room.setRoomCapacity(20);
        room.setCleaningState(CleaningState.A_Nettoyer);
        room.setRoomType(RoomType.Double);
        room.setRoomKey(100);

        bed.setBedKey(10);
        bed.setBedNumber(101);
        bed.setBedDescription("new bed  ");
        bed.setBedType(BedType.Simple);
        bed.setBedStatue(BedStatus.En_Maintenance);
        bed.setPoids(80);
        bed.setPhysicalState(BedPhysicalCondition.Bon_Etat);
        long millis = System.currentTimeMillis();
        bed.setExpirationDate(new Timestamp(millis));
        bed.setBedPurchaseDate(new Timestamp(millis));
        List<Room>roomList=new ArrayList<>();
        roomList.add(room);
        careUnit.setRooms(roomList);
        List<Bed>bedList=new ArrayList<>();
        bedList.add(bed);
        room.setRoomBed(bedList);
        equipment.setEquipmentName("laboratoiree");


    }


    @Test
    void createCareUnit() {
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/parameterization/careunit", this.careUnit, String.class);
        //ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/parameterization/careunit", this.careUnit, String.class);
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Careunit added successfuly", response.getBody().toString());


    }


    @Test
    void getAllCareUnit() {
        ResponseEntity<List> response = restTemplate.getForEntity("http://localhost:" + port + "/parameterization/careunit", List.class);
        System.out.println(response);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void getCareUnitById() {
        System.out.println(this.careUnit.getCareunitKey());
        // Appel de la méthode du contrôleur pour récupérer le patient avec l'identifiant 1
        ResponseEntity<CareUnit> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/parameterization/careunit/5" , CareUnit.class);
        // Vérification que la réponse est correcte et que le careunit est non nul
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("amaal test", responseEntity.getBody().getCareunitName().trim());
    }

    @Test
    void getCareUnitByName() {
        System.out.println(this.careUnit.getCareunitName());
        // Appel de la méthode du contrôleur pour récupérer le patient avec l'identifiant 1
        ResponseEntity<CareUnit> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/parameterization/careunit/careunitName/amaal test" , CareUnit.class);
        // Vérification que la réponse est correcte et que le careunit est non nul

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertThat(responseEntity.getBody().getCareunitName()).isEqualTo("amaal test");
    }
   @Test
    void getCareUnitStatue() {
       System.out.println(this.careUnit.getCareunitStatue());
       // Appel de la méthode du contrôleur pour récupérer les unités de soins avec le statut "ACTIVE"
       ResponseEntity<List> response = restTemplate.getForEntity("http://localhost:" + port + "/parameterization/careunit/careunitStatue/InACTIVE", List.class);
       System.out.println(response);

       assertEquals(HttpStatus.OK, response.getStatusCode());
       assertNotNull(response.getBody());
       assertFalse(response.getBody().isEmpty());


   }


    @Test
    void deleteCareUnitAndAssociatedEntities() {
        restTemplate.delete("http://localhost:" + port + "/parameterization/careunit/1");
        ResponseEntity<CareUnit> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/parameterization/careunit/1", CareUnit.class);
        assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());


    }


    @Test
    void getRoomsIncareunit() {
        // Envoyer une requête GET et récupérer la liste des chambres directement
        List<Room> rooms = restTemplate.getForObject(
                "http://localhost:" + port + "/parameterization/careunit/8/GetRoom",
                List.class);

        // Vérifier si la liste des chambres n'est pas vide
        assertNotNull(rooms);
        assertFalse(rooms.isEmpty());
    }
    @Test
    void addRoomsToCareUnit() {
        // Créer une chambre à ajouter à l'unité de soins
        Room roomToAdd = new Room();
        Bed bed =new Bed();
        // Initialisez les propriétés de la chambre


        roomToAdd.setRoomName(103);
        roomToAdd.setRoomResponsible("doc flen");
        roomToAdd.setRoomCapacity(20);
        roomToAdd.setCleaningState(CleaningState.Nettoye);
        roomToAdd.setRoomType(RoomType.Double);
        roomToAdd.setRoomKey(108);
        roomToAdd.setRoomStatue(RoomStatus.Reserve);
        bed.setBedKey(10);
        bed.setBedNumber(101);
        bed.setBedDescription("new bed  ");
        bed.setBedType(BedType.Simple);
        bed.setBedStatue(BedStatus.En_Maintenance);
        bed.setPoids(80);
        bed.setPhysicalState(BedPhysicalCondition.Bon_Etat);
        long millis = System.currentTimeMillis();
        bed.setExpirationDate(new Timestamp(millis));
        bed.setBedPurchaseDate(new Timestamp(millis));
        List<Room>roomList=new ArrayList<>();
        roomList.add(roomToAdd);
        careUnit.setRooms(roomList);
        List<Bed>bedList=new ArrayList<>();
        bedList.add(bed);
        roomToAdd.setRoomBed(bedList);


        // Créer une requête HTTP avec les données de la chambre à ajouter
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Room> requestEntity = new HttpEntity<>(roomToAdd, headers);

        // Envoyer une requête POST pour ajouter la chambre à l'unité de soins
        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/parameterization/careunit/8/AddRoom",
                requestEntity,
                String.class);

        // Vérifier si la requête a abouti avec succès (statut 201 CREATED)
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Room Added to Careunit Successfully", response.getBody());
    }

 @Test
 void testDeleteRoomFromCareUnit() {
     // Supprimer une salle de l'unité de soins
     restTemplate.delete("http://localhost:" + port + "/parameterization/careunit/8/deleteRoom/4");

     // Vérifier si la salle a été supprimée avec succès
     ResponseEntity<Room> responseEntity = restTemplate.getForEntity(
             "http://localhost:" + port + "/parameterization/room/4",
             Room.class);

     assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
 }



}