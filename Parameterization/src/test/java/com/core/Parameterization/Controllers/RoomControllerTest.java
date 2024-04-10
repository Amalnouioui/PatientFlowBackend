package com.core.Parameterization.Controllers;

import com.core.Parameterization.Entities.Bed;
import com.core.Parameterization.Entities.CareUnit;
import com.core.Parameterization.Entities.Enumeration.*;
import com.core.Parameterization.Entities.Room;
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
import org.springframework.http.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoomControllerTest {

        @LocalServerPort
        private int port;
        @Autowired
        private TestRestTemplate restTemplate;
        private Room room;
        private CareUnit careUnit;
        private  Bed bed;
        @BeforeEach
        void setUp() {

            restTemplate = new TestRestTemplate();
            room = new Room();
            bed=new Bed();
            careUnit=new CareUnit();
            room.setRoomKey(room.getRoomKey());
            room.setRoomName(208);
            room.setRoomResponsible("mr abdelakoui");
            room.setRoomStatue(RoomStatus.Reserve);
            room.setRoomType(RoomType.Double);
            room.setRoomCapacity(8);
            room.setCleaningState(CleaningState.Nettoye);
            //Careunit
            careUnit.setCareunitKey(18);
            careUnit.setCareunitName("care4"); // Correction du nom de l'unité de soins
            careUnit.setCareunitDescription("neww careunit for test");
            careUnit.setCareunitResponsable("Doc imed");
            careUnit.setCareunitCapacity(5);
            careUnit.setCareuniType(UnitType.Intermediaires);
            careUnit.setCareunitStatue(UnitStatus.InACTIVE);
            long currentTimeMillis = System.currentTimeMillis();
            careUnit.setCareUnit_StartTime(new Timestamp(currentTimeMillis));
            careUnit.setCareUnit_EndTime(new Timestamp(currentTimeMillis));
            // Initialisation des identifiants d'équipements et de services
            careUnit.setEquipmentList(new ArrayList<>());
         //   careUnit.setServiceList(new ArrayList<>());
            List<Long> equipmentIds = Arrays.asList(1L, 3L);
            List<Long> serviceIds = Arrays.asList(1L, 2L, 3L);
            careUnit.setEquipmentList(equipmentIds);
           // careUnit.setServiceList(serviceIds);

            //bed
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
            List<Bed>bedList=new ArrayList<>();
            bedList.add(bed);
            room.setRoomBed(bedList);
            List<Room>roomList=new ArrayList<>();
            roomList.add(room);
        //    careUnit.setRooms(roomList);
            room.setCareunitRoom(this.careUnit);



        }


        @Test
        void createRoomTest() {
            // Créer une chambre à ajouter à l'unité de soins
            CareUnit careunitToAdd = new CareUnit();
            careunitToAdd.setCareunitKey(18);
            careunitToAdd.setCareunitName("care4"); // Correction du nom de l'unité de soins
            careunitToAdd.setCareunitDescription("neww careunit for test");
            careunitToAdd.setCareunitResponsable("Doc imed");
            careunitToAdd.setCareunitCapacity(5);
            careunitToAdd.setCareuniType(UnitType.Intensifs);
            careunitToAdd.setCareunitStatue(UnitStatus.InACTIVE);
            long currentTimeMillis = System.currentTimeMillis();
            careunitToAdd.setCareUnit_StartTime(new Timestamp(currentTimeMillis));
            careunitToAdd.setCareUnit_EndTime(new Timestamp(currentTimeMillis));
            // Initialisation des identifiants d'équipements et de services
            careunitToAdd.setEquipmentList(new ArrayList<>());
            //areunitToAdd.setServiceList(new ArrayList<>());
            List<Long> equipmentIds = Arrays.asList(1L, 3L);
            List<Long> serviceIds = Arrays.asList(1L, 2L, 3L);
            careunitToAdd.setEquipmentList(equipmentIds);
           // careunitToAdd.setServiceList(serviceIds);

            room.setCareunitRoom(careunitToAdd);



            // Créer une requête HTTP avec les données de la chambre à ajouter
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<CareUnit> requestEntity = new HttpEntity<>(careunitToAdd, headers);

            // Envoyer une requête POST pour ajouter la chambre à l'unité de soins
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "http://localhost:" + port + "/parameterization/room",
                    requestEntity,
                    String.class);

            // Vérifier si la requête a abouti avec succès (statut 201 CREATED)
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals("added successfully", response.getBody());

        }

        @Test
        void getAllRoomTest() {
            ResponseEntity<List> response = restTemplate.getForEntity("http://localhost:" + port + "/parameterization/room", List.class);
            System.out.println(response);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertFalse(response.getBody().isEmpty());
        }

        @Test
        void getRoombyStatueTest() {
            ResponseEntity<List> response = restTemplate.getForEntity("http://localhost:" + port + "/parameterization/room/roomStatue/Disponible", List.class);
            System.out.println(response);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertFalse(response.getBody().isEmpty());
        }
    @Test
    void getRoomByType() {
        ResponseEntity<List> response = restTemplate.getForEntity("http://localhost:" + port + "/parameterization/room/roomType/Simple", List.class);
        System.out.println(response);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }
    @Test
    void getRoomByName(){
        //System.out.println(this.room.getRoomName());
        // Appel de la méthode du contrôleur pour récupérer le patient avec l'identifiant 1
        ResponseEntity<Room> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/parameterization/room/roomName/10" , Room.class);
        // Vérification que la réponse est correcte et que le careunit est non nul

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(10, responseEntity.getBody().getRoomName());
    }

    @Test
    void getBedFromRoom(){
            // Envoyer une requête GET et récupérer la liste des chambres directement
    List<Bed> beds = restTemplate.getForObject(
            "http://localhost:" + port + "/parameterization/room/1/getBed",
            List.class);

    // Vérifier si la liste des chambres n'est pas vide
    assertNotNull(beds);
    assertFalse(beds.isEmpty());

}
@Test
    void addBedtoRoom(){
    // Créer une chambre à ajouter à l'unité de soins
    Bed bedToAdd = new Bed();
    bedToAdd.setBedKey(103);
    bedToAdd.setBedNumber(840);
    bedToAdd.setBedDescription("doc flen");
    bedToAdd.setPoids(20);
    bedToAdd.setBedCleaningStatus(BedCleaningStatus.A_Nettoyer);
    bedToAdd.setBedType(BedType.Medicalise);
    bedToAdd.setBedStatue(BedStatus.Disponible);
    bedToAdd.setBedDescription("test bed");
    bedToAdd.setPhysicalState(BedPhysicalCondition.Bon_Etat);
    long millis = System.currentTimeMillis();
    bedToAdd.setExpirationDate(new Timestamp(millis));
    bedToAdd.setBedPurchaseDate(new Timestamp(millis));

    List<Bed>beds=new ArrayList<>();
    beds.add(bedToAdd);
    room.setRoomBed(beds);



    // Créer une requête HTTP avec les données de la chambre à ajouter
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Bed> requestEntity = new HttpEntity<>(bedToAdd, headers);

    // Envoyer une requête POST pour ajouter la chambre à l'unité de soins
    ResponseEntity<String> response = restTemplate.postForEntity(
            "http://localhost:" + port + "/parameterization/room/4/addBed",
            requestEntity,
            String.class);

    // Vérifier si la requête a abouti avec succès (statut 201 CREATED)
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals("Bed Added to room Successfully", response.getBody());
}

    @Test
    void testdeleteBedfromRoom() {
        // Supprimer une salle de l'unité de soins
        restTemplate.delete("http://localhost:" + port + "/parameterization/room/2/deleteBed/2");

        // Vérifier si la salle a été supprimée avec succès
        ResponseEntity<Bed> responseEntity = restTemplate.getForEntity(
                "http://localhost:" + port + "/parameterization/bed/2",
                Bed.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

}

