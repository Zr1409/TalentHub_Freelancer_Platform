package com.backend.dto.request.account.freelancer;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.backend.entity.child.account.freelancer.Freelancer;
import com.backend.entity.child.account.freelancer.Project;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTORequest {

    @NotBlank(message = "Title cannot be blank")
    private String title;

    private String tech;

    private String description;

    private String link;

    private String image;

    private Long freelancerId;

    public Project toEntity() {
        Project project = new Project();
        project.setTitle(this.title);
        project.setTech(this.tech);
        project.setDescription(this.description);
        project.setLink(this.link);
        project.setImage(this.image);

        if (this.freelancerId != null) {
            Freelancer freelancer = new Freelancer();
            freelancer.setId(this.freelancerId);
            project.setFreelancer(freelancer);
        }

        return project;
    }
}