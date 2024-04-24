package com.core.Parameterization.Config;

import com.core.Parameterization.Entities.Bed;
import com.core.Parameterization.Entities.Enumeration.OccupantType;
import com.core.Parameterization.Services.BedService;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
public class MyWebSocketHandler extends TextWebSocketHandler {

    private final BedService bedService;

    public MyWebSocketHandler(BedService bedService) {
        this.bedService = bedService;
    }



   /* @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        String[] parts = payload.split(",");
        if (parts.length != 1) {
            // Gérer les cas où le message n'est pas au format attendu
            session.sendMessage(new TextMessage("Le message n'est pas au format attendu."));
            return;
        }
        try {
            String patientIdString = parts[0].trim().replaceAll("\"", ""); // Supprimer les guillemets autour de l'ID du patient
            System.out.println("ID du patient : " + patientIdString); // Débogage
            Integer patientId = Integer.parseInt(patientIdString);

            // Vérifier si le patient est attribué à un lit
            Bed assignedBed = bedService.isPatientAssignedToBed(patientId);

            if (assignedBed != null) {
                // Envoyer l'ID du patient au client WebSocket
                session.sendMessage(new TextMessage("ID du patient : " + patientId));
            } else {
                // Envoyer un message indiquant que le patient n'est pas attribué à un lit
                session.sendMessage(new TextMessage("Le patient n'est pas attribué à un lit."));
            }
        } catch (NumberFormatException e) {
            // Gérer le cas où l'ID du patient n'est pas un entier valide
            session.sendMessage(new TextMessage("L'ID du patient n'est pas un entier valide."));
        }
    }
*/


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        String[] parts = payload.split(",");
        if (parts.length != 1) {
            // Gérer les cas où le message n'est pas au format attendu
            session.sendMessage(new TextMessage("Le message n'est pas au format attendu."));
            return;
        }
        try {
            String patientIdString = parts[0].trim().replaceAll("\"", ""); // Supprimer les guillemets autour de l'ID du patient
            System.out.println("ID du patient : " + patientIdString); // Débogage
            Integer patientId = Integer.parseInt(patientIdString);

            // Vérifier si le patient est attribué à un lit
            Bed assignedBed = bedService.isPatientAssignedToBed(patientId);

            if (assignedBed != null) {
                // Envoyer les détails du lit au client WebSocket
                String responseMessage = "ID du patient : " + patientId + ", Lit : " + assignedBed.toString();
                session.sendMessage(new TextMessage(responseMessage));
            } else {
                // Envoyer un message indiquant que le patient n'est pas attribué à un lit
                session.sendMessage(new TextMessage("Le patient n'est pas attribué à un lit."));
            }
        } catch (NumberFormatException e) {
            // Gérer le cas où l'ID du patient n'est pas un entier valide
            session.sendMessage(new TextMessage("L'ID du patient n'est pas un entier valide."));
        }
    }

}