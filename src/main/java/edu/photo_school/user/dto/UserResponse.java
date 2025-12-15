package edu.photo_school.user.dto;

import java.time.Instant;
import java.util.Set;

public record UserResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        Instant createdAt,
        Set<String> roles
) {
}
