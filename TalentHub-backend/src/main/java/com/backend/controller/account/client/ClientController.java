package com.backend.controller.account.client;

import com.backend.dto.response.account.client.*;
import lombok.RequiredArgsConstructor;
import com.backend.dto.ResponseObject;
import com.backend.dto.request.account.client.ClientDTORequest;
import com.backend.dto.request.account.client.UpdatePriceAndTypeDTORequest;
import com.backend.service.intf.account.client.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseObject<ClientDTOResponse> createClient(@Valid @RequestBody ClientDTORequest clientDTORequest) {
        ClientDTOResponse clientDTOResponse = clientService.create(clientDTORequest);
        return ResponseObject.<ClientDTOResponse>builder()
                .message("Client created successfully")
                .status(HttpStatus.CREATED.value())
                .data(clientDTOResponse)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseObject<ClientDTOResponse> getClientById(@PathVariable Long id) {
        return clientService.getById(id)
                .map(client -> ResponseObject.<ClientDTOResponse>builder()
                        .message("Client retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(client)
                        .build())
                .orElse(ResponseObject.<ClientDTOResponse>builder()
                        .message("Client not found")
                        .status(HttpStatus.NOT_FOUND.value())
                        .build());
    }

    @GetMapping
    public ResponseObject<List<ClientDTOResponse>> getAllClients() {
        List<ClientDTOResponse> clients = clientService.getAll();
        return ResponseObject.<List<ClientDTOResponse>>builder()
                .message("All clients retrieved successfully")
                .status(HttpStatus.OK.value())
                .data(clients)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseObject<Void> deleteClient(@PathVariable Long id) {
        boolean isDeleted = clientService.deleteById(id);
        if (isDeleted) {
            return ResponseObject.<Void>builder()
                    .message("Client deleted successfully")
                    .status(HttpStatus.NO_CONTENT.value())
                    .build();
        }
        return ResponseObject.<Void>builder()
                .message("Client not found")
                .status(HttpStatus.NOT_FOUND.value())
                .build();
    }

    @PutMapping("/updatePrice")
    public ResponseObject<UpdatePriceAndTypeDTOResponse> updatePriceAndType(@Valid @RequestBody UpdatePriceAndTypeDTORequest updatePriceAndTypeDTORequest) {
        UpdatePriceAndTypeDTOResponse response = clientService.updatePriceAndType(updatePriceAndTypeDTORequest);
        return ResponseObject.<UpdatePriceAndTypeDTOResponse>builder()
                .message("Client price and type updated successfully")
                .status(HttpStatus.OK.value())
                .data(response)
                .build();
    }


    @GetMapping("/{clientId}/companies")
    public ResponseObject<List<CompanyDTOResponse>> getCompaniesByClientId(@PathVariable Long clientId) {
        List<CompanyDTOResponse> companies = clientService.getCompaniesByClientId(clientId);
        return ResponseObject.<List<CompanyDTOResponse>>builder()
                .message("Companies for client " + clientId + " retrieved successfully")
                .status(HttpStatus.OK.value())
                .data(companies)
                .build();
    }


    @GetMapping("/active")
    public ResponseObject<List<ActiveClientDTOResponse>> getAllActiveClients() {
        List<ActiveClientDTOResponse> activeClients = clientService.getAllActiveClients();
        return ResponseObject.<List<ActiveClientDTOResponse>>builder()
                .message("Active clients retrieved successfully")
                .status(HttpStatus.OK.value())
                .data(activeClients)
                .build();
    }

    @GetMapping("/{clientId}/detail")
    public ResponseObject<ClientDetailDTOResponse> getClientDetail(@PathVariable Long clientId) {
        ClientDetailDTOResponse clientDetail = clientService.getClientDetail(clientId);
        return ResponseObject.<ClientDetailDTOResponse>builder()
                .message("Client detail retrieved successfully")
                .status(HttpStatus.OK.value())
                .data(clientDetail)
                .build();
    }

    @GetMapping("/notify-for-clients")
    public ResponseObject<String> getClients(
            @RequestParam Long freelancerId) {

        clientService.findClientsByDiamondPackageAndCategory(freelancerId);

        return ResponseObject.<String>builder()
                .message("Clients with diamond package")
                .status(HttpStatus.OK.value())
                .data("Notify successfully")
                .build();
    }
}
