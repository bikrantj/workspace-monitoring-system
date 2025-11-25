package com.bikrantj.shared.responses;

import com.bikrantj.shared.dto.User;

public record LoginResponse(
        String token,
        User user
) {
}