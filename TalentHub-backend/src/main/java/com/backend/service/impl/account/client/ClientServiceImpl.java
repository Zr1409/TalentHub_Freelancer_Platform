package com.backend.service.impl.account.client;

import com.backend.dto.response.account.client.*;
import com.backend.repository.*;
import lombok.RequiredArgsConstructor;
import com.backend.dto.request.account.client.ClientDTORequest;
import com.backend.dto.request.account.client.UpdatePriceAndTypeDTORequest;
import com.backend.dto.response.job.ClientReviewDTOResponse;
import com.backend.entity.child.account.Account;
import com.backend.entity.child.account.client.Client;
import com.backend.entity.child.account.User;
import com.backend.entity.child.account.client.ClientReview;
import com.backend.entity.child.account.client.Company;
import com.backend.entity.child.account.freelancer.Freelancer;
import com.backend.entity.child.job.Category;
import com.backend.entity.child.job.FreelancerJob;
import com.backend.enums.StatusAccount;
import com.backend.exception.BadRequestException;
import com.backend.exception.NotFoundException;
import com.backend.mapper.Account.client.ActiveClientMapper;
import com.backend.mapper.Account.client.ClientMapper;
import com.backend.mapper.Account.client.UpdatePriceAndTypeMapper;
import com.backend.service.impl.notify.NotifyService;
import com.backend.service.intf.account.client.ClientService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;
    private final UpdatePriceAndTypeMapper updatePriceAndTypeMapper;
    private final CompanyRepository companyRepository;
    private final ClientReviewRepository clientReviewRepository;
    private final ActiveClientMapper activeClientMapper;
    private final FreelancerJobRepository freelancerJobRepository;
    private final NotifyService notifyService;
    private final FreelancerRepository freelancerRepository;

    @Override
    public ClientDTOResponse create(ClientDTORequest clientDTORequest) {
        User user = userRepository.findById(clientDTORequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Client client = new Client();
        client.setFromPrice(clientDTORequest.getFromPrice());
        client.setToPrice(clientDTORequest.getToPrice());
        client.setTypePrice(clientDTORequest.getTypePrice());
        client.setUser(user);

        Client savedClient = clientRepository.save(client);
        return clientMapper.toResponseDto(savedClient);
    }

    @Override
    public Optional<ClientDTOResponse> getById(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isPresent()) {
            return Optional.of(clientMapper.toResponseDto(client.get()));
        }
        return Optional.empty();
    }


    @Override
    public List<ClientDTOResponse> getAll() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream().map(clientMapper::toResponseDto).toList();
    }

    @Override
    public Boolean deleteById(Long id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Client findById(Long clientId) {
        if (clientId == null) {
            throw new BadRequestException("Client id is null");
        }

        return clientRepository.findById(clientId)
                .orElseThrow(() -> new BadRequestException("Client not found"));
    }

    @Override
    public UpdatePriceAndTypeDTOResponse updatePriceAndType(UpdatePriceAndTypeDTORequest updatePriceAndTypeDTORequest) {

        clientRepository.updatePrice(
                updatePriceAndTypeDTORequest.getClientId(),
                updatePriceAndTypeDTORequest.getFromPrice(),
                updatePriceAndTypeDTORequest.getToPrice(),
                updatePriceAndTypeDTORequest.getTypePrice()
        );

        Client updatedClient = clientRepository.findById(updatePriceAndTypeDTORequest.getClientId())
                .orElseThrow(() -> new BadRequestException("Client not found after update"));

        return updatePriceAndTypeMapper.toResponseDto(updatedClient);
    }

    @Override
    public List<CompanyDTOResponse> getCompaniesByClientId(Long clientId) {
        // First verify client exists
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Client not found with id: " + clientId));

        // Get companies associated with this client
        List<Company> companies = companyRepository.findByClientId(clientId);

        // Convert to DTOs
        return companies.stream()
                .map(company -> new CompanyDTOResponse(
                        company.getId(),
                        company.getCompanyName(),
                        company.getCompanyImage(),
                        company.getAddress(),
                        company.getPhoneContact(),
                        company.getIndustry(),
                        company.getClient().getId()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ActiveClientDTOResponse> getAllActiveClients() {
        List<Client> activeClients = clientRepository.findByUser_Account_StatusNot(StatusAccount.LOCKED);

        List<ActiveClientDTOResponse> response = new ArrayList<>();

        for (Client client : activeClients) {
            List<ClientReview> reviews = getClientReviews(client.getId());

            List<Company> companies = companyRepository.findByClientId(client.getId());
            List<CompanyDTOResponse> companyDTOs = companies.stream()
                    .map(company -> new CompanyDTOResponse(
                            company.getId(),
                            company.getCompanyName(),
                            company.getCompanyImage(),
                            company.getAddress(),
                            company.getPhoneContact(),
                            company.getIndustry(),
                            company.getClient() != null ? company.getClient().getId() : null
                    ))
                    .collect(Collectors.toList());

            ActiveClientDTOResponse dto = activeClientMapper.toActiveClientResponse(
                    client,
                    reviews,
                    companyDTOs
            );

            response.add(dto);
        }

        return response;
    }

    @Override
    public ClientDetailDTOResponse getClientDetail(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Client not found with id: " + clientId));

        List<ClientReview> reviews = getClientReviews(clientId);

        List<Company> companies = companyRepository.findByClientId(clientId);
        List<CompanyDTOResponse> companyDTOs = companies.stream()
                .map(company -> new CompanyDTOResponse(
                        company.getId(),
                        company.getCompanyName(),
                        company.getCompanyImage(),
                        company.getAddress(),
                        company.getPhoneContact(),
                        company.getIndustry(),
                        company.getClient() != null ? company.getClient().getId() : null
                ))
                .collect(Collectors.toList());

        List<ClientReviewDTOResponse> reviewDTOs = new ArrayList<>();

        for (ClientReview review : reviews) {
            FreelancerJob freelancerJob = freelancerJobRepository.findByClientReviewId(review.getId());
            if (freelancerJob != null) {
                String reviewerName = getReviewerName(review.getId());
                String projectTitle = freelancerJob.getJob() != null ? freelancerJob.getJob().getTitle() : "Unknown Project";
                String freelancerAvatar = null;
                if (freelancerJob.getFreelancer() != null &&
                        freelancerJob.getFreelancer().getUser() != null) {
                    freelancerAvatar = freelancerJob.getFreelancer().getUser().getImage();
                }
                ClientReviewDTOResponse reviewDTO = activeClientMapper.toClientReviewResponseWithProjectDetails(
                        review,
                        reviewerName,
                        projectTitle,
                        freelancerJob,
                        freelancerAvatar
                );

                reviewDTOs.add(reviewDTO);
            } else {
                String reviewerName = "Unknown Reviewer";
                reviewDTOs.add(activeClientMapper.toClientReviewResponse(review, reviewerName));
            }
        }

        return activeClientMapper.toClientDetailResponse(
                client,
                reviews,
                companyDTOs,
                reviewDTOs
        );
    }

    private List<ClientReview> getClientReviews(Long clientId) {
        return clientReviewRepository.findByClientId(clientId);
    }

    private String getReviewerName(Long reviewId) {
        FreelancerJob job = freelancerJobRepository.findByClientReviewId(reviewId);
        if (job != null && job.getFreelancer() != null && job.getFreelancer().getUser() != null) {
            User user = job.getFreelancer().getUser();
            return user.getFirstName() + " " + user.getLastName();
        }
        return "Unknown Reviewer";
    }

    public boolean checkValidClient(Long id) {
        Client client = clientRepository.findById(id).get();
        User user = client.getUser();
        Account account = user.getAccount();

        if (account.getStatus().equals(StatusAccount.LOCKED)) {
            return false;
        }

        return true;
    }

    public void findClientsByDiamondPackageAndCategory(Long freelancerId) {
        Freelancer freelancer = freelancerRepository.findById(freelancerId).get();
        Category category = freelancer.getCategory();
        if (category != null) {
            clientRepository.findClientsByDiamondPackageAndCategoryId(category.getId()).stream().forEach(el -> {
                notifyService.sendNotification(el.getUser().getId(), "Có 1 ứng viên tiềm năng phù hợp với công việc của bạn!", "freelancers/" + freelancerId);
            });
        }
    }
}
