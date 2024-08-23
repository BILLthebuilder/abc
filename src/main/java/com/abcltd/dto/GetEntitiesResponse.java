package com.abcltd.dto;

import java.util.List;

public record GetEntitiesResponse<T>(
        Status success, List<T> entities
) {
}
