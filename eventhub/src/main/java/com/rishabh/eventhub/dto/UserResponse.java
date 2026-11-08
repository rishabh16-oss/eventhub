package com.rishabh.eventhub.dto;

import com.rishabh.eventhub.entity.Role;

public record UserResponse(Long id, String name, String email, Role role) {}