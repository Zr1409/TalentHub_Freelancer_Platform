package com.backend.service.impl.job;

import lombok.RequiredArgsConstructor;
import com.backend.dto.request.job.CategoryDTORequest;
import com.backend.dto.response.job.CategoryDTOResponse;
import com.backend.entity.child.account.freelancer.Freelancer;
import com.backend.entity.child.job.Category;
import com.backend.entity.child.job.Job;
import com.backend.repository.CategoryRepository;
import com.backend.repository.FreelancerRepository;
import com.backend.repository.JobRepository;
import com.backend.service.intf.job.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final FreelancerRepository freelancerRepository;
    private final JobRepository jobRepository;
    @Override
    public CategoryDTOResponse update(Long id, CategoryDTORequest categoryDTORequest) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setCategoryTitle(categoryDTORequest.getCategoryTitle());

            category = categoryRepository.save(category);

            Long quantityFreelancer = freelancerRepository.countByCategoryId(category.getId());
            Long quantityJob = jobRepository.countByCategoryId(category.getId());

            return CategoryDTOResponse.builder()
                    .id(category.getId())
                    .categoryTitle(category.getCategoryTitle())
                    .quantityFreelancer(quantityFreelancer)
                    .quantityJob(quantityJob)
                    .build();
        }

        return null;
    }

    @Override
    public CategoryDTOResponse create(CategoryDTORequest categoryDTORequest) {
        Category category = new Category();
        category.setCategoryTitle(categoryDTORequest.getCategoryTitle());

        category = categoryRepository.save(category);

        Long quantityFreelancer = freelancerRepository.countByCategoryId(category.getId());
        Long quantityJob = jobRepository.countByCategoryId(category.getId());

        return CategoryDTOResponse.builder()
                .id(category.getId())
                .categoryTitle(category.getCategoryTitle())
                .quantityFreelancer(quantityFreelancer)
                .quantityJob(quantityJob)
                .build();
    }

    @Override
    public Optional<CategoryDTOResponse> getById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isPresent()) {
            Long quantityFreelancer = freelancerRepository.countByCategoryId(id);
            Long quantityJob = jobRepository.countByCategoryId(id);

            return Optional.of(CategoryDTOResponse.builder()
                    .id(category.get().getId())
                    .categoryTitle(category.get().getCategoryTitle())
                    .quantityFreelancer(quantityFreelancer)
                    .quantityJob(quantityJob)
                    .build());
        }

        return Optional.empty();
    }

    @Override
    public List<CategoryDTOResponse> getAll() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(c -> {
                    Long quantityFreelancer = freelancerRepository.countByCategoryId(c.getId());
                    Long quantityJob = jobRepository.countByCategoryId(c.getId());

                    return CategoryDTOResponse.builder()
                            .id(c.getId())
                            .categoryTitle(c.getCategoryTitle())
                            .quantityFreelancer(quantityFreelancer)
                            .quantityJob(quantityJob)
                            .build();
                })
                .collect(Collectors.toList());
    }
    private final FreelancerRepository freeelancerRepository;
    @Override
    public Boolean deleteById(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);

        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();

            List<Job> jobs = jobRepository.findByCategory(category);
            for (Job job : jobs) {
                job.setCategory(null);
                jobRepository.save(job);
            }

            List<Freelancer> freelancers = freelancerRepository.findByCategory(category);
            for (Freelancer freelancer : freelancers) {
                freelancer.setCategory(null);
                freelancerRepository.save(freelancer);
            }

            categoryRepository.deleteById(id);
            return true;
        }

        return false;
    }

}
