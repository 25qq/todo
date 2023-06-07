package com.example.todo.service;


import com.example.todo.data.entity.user.User;

public interface UsersAdminService {
    User getOrCreateUser(String email, String name);
}
