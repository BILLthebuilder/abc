package com.abcltd.service;

import com.abcltd.dto.GenericResponse;
import com.abcltd.dto.GetEntitiesResponse;
import com.abcltd.dto.Status;
import com.abcltd.model.ParentEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GenericService {

    public <D, E> ResponseEntity<GenericResponse> create(
            D request,
            Errors errors,
            Class<E> entityClass,
            JpaRepository<E, UUID> repository) {

        GenericResponse response;

        if (errors.hasFieldErrors()) {
            FieldError fieldError = errors.getFieldError();
            response = new GenericResponse(fieldError.getDefaultMessage(), Status.FAILED);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            E entity = entityClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(request, entity);
            repository.save(entity);
            response = new GenericResponse(entityClass.getSimpleName() + " " + "created successfully", Status.SUCCESS);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            log.error("Unable to create" + " " + entityClass.getSimpleName(), sw);
            response = new GenericResponse(ex.getMessage(), Status.FAILED);
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public <D, E> ResponseEntity<GenericResponse> update(
            UUID entityId,
            D request,
            Errors errors,
            Class<E> entityClass,
            JpaRepository<E, UUID> repository) {

        GenericResponse response;

        if (errors.hasFieldErrors()) {
            FieldError fieldError = errors.getFieldError();
            response = new GenericResponse(fieldError.getDefaultMessage(), Status.FAILED);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            Optional<E> entity = Optional.empty();
            if (entityId != null) {
                entity = repository.findById(entityId);
            } else {
                response = new GenericResponse(entityClass.getSimpleName() + " " + " not found", Status.SUCCESS);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            if (entity.isEmpty()) {
                response = new GenericResponse(entityClass.getSimpleName() + " " +"not found", Status.SUCCESS);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            BeanUtils.copyProperties(request, entity.get());
            repository.save(entity.get());

            response = new GenericResponse(entityClass.getSimpleName() + " "+"updated", Status.SUCCESS);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            log.error("Error has occurred", sw);
            response = new GenericResponse(ex.getMessage(), Status.FAILED);
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public <E extends ParentEntity> ResponseEntity<GetEntitiesResponse<E>> getAll(
            JpaRepository<E, UUID> repository,
            int page,
            int size,
            String sortBy,
            String sortOrder
    ) {

        GetEntitiesResponse<E> response;

        Slice<E> entities = null;
        List<E> filteredEntities = new ArrayList<>();
        Pageable pageable = createPageable(page, size,sortBy, sortOrder);
        try {
            entities = repository.findAll(pageable);
            filteredEntities = entities
                    .getContent()
                    .stream()
                    .filter(E::getStatus)
                    .collect(Collectors.toList());

            response = new GetEntitiesResponse<>(Status.SUCCESS, filteredEntities);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.error("Error has occurred", sw);
            response = new GetEntitiesResponse<>(Status.FAILED, null);
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public <E> ResponseEntity<Optional<E>> getOne(
            String id,
            JpaRepository<E, UUID> repository) {

        if (repositoryExists(id, repository)) {
            Optional<E> entity = repository.findById(UUID.fromString(id));
            if (entity.isPresent()) {
                return ResponseEntity.ok(entity);
            }
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    private <E> boolean repositoryExists(String id, JpaRepository<E, UUID> repository) {
        return repository.existsById(UUID.fromString(id));
    }

    public <E> ResponseEntity<GenericResponse> delete(
            String id,
            JpaRepository<E, UUID> repository) {

        GenericResponse response;

        try {
            Optional<E> entityOptional = repository.findById(UUID.fromString(id));
            if (entityOptional.isEmpty()) {
                response = new GenericResponse("Not found", Status.FAILED);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            repository.deleteById(UUID.fromString(id));
            response = new GenericResponse("Deleted successfully", Status.SUCCESS);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            log.error("Unable to delete entity: {}", sw);
            response = new GenericResponse("Deleting failed", Status.FAILED);
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    private Pageable createPageable(int page, int size, String sortBy, String sortOrder) {
        Sort sort = Sort.by(Sort.Order.asc("id"));

        if (sortBy != null && !sortBy.isEmpty()) {
            if ("desc".equalsIgnoreCase(sortOrder)) {
                sort = Sort.by(Sort.Order.desc(sortBy));
            } else {
                sort = Sort.by(Sort.Order.asc(sortBy));
            }

        }

        return PageRequest.of(page, size, sort);
    }
}
