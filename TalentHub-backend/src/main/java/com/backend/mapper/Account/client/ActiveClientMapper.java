package com.backend.mapper.Account.client;

import com.backend.dto.response.account.client.ActiveClientDTOResponse;
import com.backend.dto.response.account.client.ClientDetailDTOResponse;
import com.backend.dto.response.account.client.CompanyDTOResponse;
import com.backend.dto.response.job.ClientReviewDTOResponse;
import com.backend.entity.child.account.client.Client;
import com.backend.entity.child.account.client.ClientReview;
import com.backend.entity.child.job.FreelancerJob;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.OptionalDouble;

@Mapper(componentModel = "spring")
public interface ActiveClientMapper {

    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "fromPrice", source = "client.fromPrice")
    @Mapping(target = "toPrice", source = "client.toPrice")
    @Mapping(target = "jobsCount", expression = "java(client.getJobs() != null ? client.getJobs().size() : 0)")
    @Mapping(target = "firstName", source = "client.user.firstName")
    @Mapping(target = "lastName", source = "client.user.lastName")
    @Mapping(target = "province", source = "client.user.province")
    @Mapping(target = "ward", source = "client.user.ward")
    @Mapping(target = "title", source = "client.user.title")
    @Mapping(target = "introduction", source = "client.user.introduction")
    @Mapping(target = "image", source = "client.user.image")
    @Mapping(target = "email", source = "client.user.account.email")
    @Mapping(target = "averageRating", expression = "java(calculateAverageRating(reviews))")
    @Mapping(target = "companies", source = "companyDTOs")
    ActiveClientDTOResponse toActiveClientResponse(Client client, List<ClientReview> reviews, List<CompanyDTOResponse> companyDTOs);

    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "fromPrice", source = "client.fromPrice")
    @Mapping(target = "toPrice", source = "client.toPrice")
    @Mapping(target = "jobsCount", expression = "java(client.getJobs() != null ? client.getJobs().size() : 0)")
    @Mapping(target = "userId", source = "client.user.id")
    @Mapping(target = "firstName", source = "client.user.firstName")
    @Mapping(target = "lastName", source = "client.user.lastName")
    @Mapping(target = "province", source = "client.user.province")
    @Mapping(target = "ward", source = "client.user.ward")
    @Mapping(target = "title", source = "client.user.title")
    @Mapping(target = "introduction", source = "client.user.introduction")
    @Mapping(target = "image", source = "client.user.image")
    @Mapping(target = "email", source = "client.user.account.email")
    @Mapping(target = "averageRating", expression = "java(calculateAverageRating(reviews))")
    @Mapping(target = "companies", source = "companyDTOs")
    @Mapping(target = "reviews", source = "reviewDTOs")
    ClientDetailDTOResponse toClientDetailResponse(Client client, List<ClientReview> reviews,
                                                   List<CompanyDTOResponse> companyDTOs,
                                                   List<ClientReviewDTOResponse> reviewDTOs);

    @Mapping(target = "id", source = "review.id")
    @Mapping(target = "rating", source = "review.rating")
    @Mapping(target = "note", source = "review.note")
    @Mapping(target = "reviewerName", source = "reviewerName")
    ClientReviewDTOResponse toClientReviewResponse(ClientReview review, String reviewerName);

    default ClientReviewDTOResponse toClientReviewResponseWithProjectDetails(
            ClientReview review,
            String reviewerName,
            String projectTitle,
            FreelancerJob freelancerJob,
            String freelancerAvatar) {

        return ClientReviewDTOResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .note(review.getNote())
                .reviewerName(reviewerName)
                .projectTitle(projectTitle)
                .projectStartDate(freelancerJob.getAppliedDate())
                .projectDuration(freelancerJob.getJob().getDuration())
                .projectBudget(freelancerJob.getJob().getFromPrice())
                .freelancerAvatar(freelancerAvatar)
                .build();
    }

    @Named("calculateAverageRating")
    default Float calculateAverageRating(List<ClientReview> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0f;
        }

        OptionalDouble average = reviews.stream()
                .filter(review -> review.getRating() != null)
                .mapToDouble(review -> review.getRating())
                .average();

        return average.isPresent() ? (float) average.getAsDouble() : 0.0f;
    }
}