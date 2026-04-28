package com.backend.config.swagger;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class GroupApiDocs {

    @Bean
    public GroupedOpenApi apiAuth() {
        return GroupedOpenApi.builder()
                .group("Auth APIs")
                .pathsToMatch("/api/v1/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiAccount() {
        return GroupedOpenApi.builder()
                .group("Account APIs")
                .pathsToMatch("/api/v1/account/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiFreelancer() {
        return GroupedOpenApi.builder()
                .group("Freelancer APIs")
                .pathsToMatch("/api/v1/freelancers/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiJob() {
        return GroupedOpenApi.builder()
                .group("Job APIs")
                .pathsToMatch("/api/v1/jobs/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiFreelancerSkill() {
        return GroupedOpenApi.builder()
                .group("FreelancerSkill APIs")
                .pathsToMatch("/api/v1/freelancer-skills/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiSkills() {
        return GroupedOpenApi.builder()
                .group("Skills APIs")
                .pathsToMatch("/api/v1/jobs/skills/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiCategory() {
        return GroupedOpenApi.builder()
                .group("Category APIs")
                .pathsToMatch("/api/v1/categories/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiClient() {
        return GroupedOpenApi.builder()
                .group("Client APIs")
                .pathsToMatch("/api/v1/clients/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiCompany() {
        return GroupedOpenApi.builder()
                .group("Company APIs")
                .pathsToMatch("/api/v1/companies/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiDegree() {
        return GroupedOpenApi.builder()
                .group("Degree APIs")
                .pathsToMatch("/api/v1/degrees/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiEducation() {
        return GroupedOpenApi.builder()
                .group("Education APIs")
                .pathsToMatch("/api/v1/educations/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiMajor() {
        return GroupedOpenApi.builder()
                .group("Major APIs")
                .pathsToMatch("/api/v1/majors/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiSchool() {
        return GroupedOpenApi.builder()
                .group("School APIs")
                .pathsToMatch("/api/v1/schools/**")
                .build();
    }

    @Bean
    public GroupedOpenApi soldPackageAPis() {
        return GroupedOpenApi.builder()
                .group("Sold Packages APIs")
                .pathsToMatch("/api/v1/clients/soldpackages/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiJobSkill() {
        return GroupedOpenApi.builder()
                .group("JobSkill APIs")
                .pathsToMatch("/api/v1/jobSkills/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiBankAccount() {
        return GroupedOpenApi.builder()
                .group("BankAccount APIs")
                .pathsToMatch("/api/v1/bank-accounts/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiEWallet() {
        return GroupedOpenApi.builder()
                .group("EWallet APIs")
                .pathsToMatch("/api/v1/e-wallets/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiPayments() {
        return GroupedOpenApi.builder()
                .group("Payments APIs")
                .pathsToMatch("/api/v1/payments/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiTransactions() {
        return GroupedOpenApi.builder()
                .group("Transactions APIs")
                .pathsToMatch("/api/v1/transactions/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiUsers() {
        return GroupedOpenApi.builder()
                .group("Users APIs")
                .pathsToMatch("/api/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiChat() {
        return GroupedOpenApi.builder()
                .group("Chat APIs")
                .pathsToMatch("/api/chat/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiAppointment() {
        return GroupedOpenApi.builder()
                .group("Appointments APIs")
                .pathsToMatch("/api/v1/appointments/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiImages() {
        return GroupedOpenApi.builder()
                .group("Image APIs")
                .pathsToMatch("/api/images/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiExperience() {
        return GroupedOpenApi.builder()
                .group("Experiences APIs")
                .pathsToMatch("/api/v1/experiences/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiReportedJob() {
        return GroupedOpenApi.builder()
                .group("Reported Jobs APIs")
                .pathsToMatch("/api/v1/reported-jobs/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiVoucherPackage() {
        return GroupedOpenApi.builder()
                .group("Voucher Packages APIs")
                .pathsToMatch("/api/v1/voucher-packages/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiProjects() {
        return GroupedOpenApi.builder()
                .group("Projects APIs")
                .pathsToMatch("/api/v1/projects/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiRevenues() {
        return GroupedOpenApi.builder()
                .group("Revenues APIs")
                .pathsToMatch("/api/v1/revenues/**")
                .build();
    }

    @Bean
    public GroupedOpenApi chatBot() {
        return GroupedOpenApi.builder()
                .group("ChatBot APIs")
                .pathsToMatch("/api/chatbot/**")
                .build();
    }
}
