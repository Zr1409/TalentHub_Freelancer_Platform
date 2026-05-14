package com.backend.controller.job.category;

import lombok.RequiredArgsConstructor;
import com.backend.dto.ResponseObject;
import com.backend.dto.request.job.CategoryDTORequest;
import com.backend.dto.response.job.CategoryDTOResponse;
import com.backend.service.intf.job.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseObject<CategoryDTOResponse> createCategory(@RequestBody @Valid CategoryDTORequest categoryDTORequest) {
        CategoryDTOResponse createdCategory = categoryService.create(categoryDTORequest);
        return ResponseObject.<CategoryDTOResponse>builder()
                .message("Category created successfully")
                .status(HttpStatus.CREATED.value())
                .data(createdCategory)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseObject<CategoryDTOResponse> updateCategory(@PathVariable Long id,
                                                              @RequestBody @Valid CategoryDTORequest categoryDTORequest) {
        CategoryDTOResponse updatedCategory = categoryService.update(id, categoryDTORequest);

        if (updatedCategory == null) {
            return ResponseObject.<CategoryDTOResponse>builder()
                    .message("Category not found")
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }

        return ResponseObject.<CategoryDTOResponse>builder()
                .message("Category updated successfully")
                .status(HttpStatus.OK.value())
                .data(updatedCategory)
                .build();
    }


    @GetMapping("/{id}")
    public ResponseObject<CategoryDTOResponse> getCategoryById(@PathVariable Long id) {
        Optional<CategoryDTOResponse> category = categoryService.getById(id);
        return category.map(c -> ResponseObject.<CategoryDTOResponse>builder()
                        .message("Category fetched successfully by id: " + id)
                        .status(HttpStatus.OK.value())
                        .data(c)
                        .build())
                .orElse(ResponseObject.<CategoryDTOResponse>builder()
                        .message("Category not found with id: " + id)
                        .status(HttpStatus.NOT_FOUND.value())
                        .build());
    }

    @GetMapping
    public ResponseObject<List<CategoryDTOResponse>> getAllCategories() {
        List<CategoryDTOResponse> categories = categoryService.getAll();
        if (categories.isEmpty()) {
            return ResponseObject.<List<CategoryDTOResponse>>builder()
                    .message("No categories found")
                    .status(HttpStatus.NO_CONTENT.value())
                    .build();
        }
        return ResponseObject.<List<CategoryDTOResponse>>builder()
                .message("Categories fetched successfully")
                .status(HttpStatus.OK.value())
                .data(categories)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseObject<Void> deleteCategory(@PathVariable Long id) {
        Boolean isDeleted = categoryService.deleteById(id);
        if (isDeleted) {
            return ResponseObject.<Void>builder()
                    .message("Category deleted successfully")
                    .status(HttpStatus.NO_CONTENT.value())
                    .build();
        } else {
            return ResponseObject.<Void>builder()
                    .message("Category not found with id: " + id)
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }
    }
}
