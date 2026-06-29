package com.example.app.controller;

import com.example.app.model.LoginRequest;
import com.example.app.model.User;
import com.example.app.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/users")
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(safe(userRepository.create(user)));
    }

    @GetMapping("/users/{id}")
    public User get(@PathVariable("id") long id) {
        return safe(userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @PutMapping("/users/{id}")
    public User update(@PathVariable("id") long id, @RequestBody User user) {
        userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return safe(userRepository.update(id, user));
    }

    @PatchMapping("/users/{id}")
    public User patch(@PathVariable("id") long id, @RequestBody Map<String, Object> fields) {
        userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return safe(userRepository.patch(id, fields));
    }

    @DeleteMapping("/users/{id}")
    public Map<String, Object> delete(@PathVariable("id") long id) {
        boolean deleted = userRepository.delete(id);
        if (!deleted) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return Map.of("deleted", true, "id", id);
    }

    @PostMapping("/login")
    public User login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .filter(found -> found.getPassword().equals(request.getPassword()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
        return safe(user);
    }

    private User safe(User user) {
        user.setPassword(null);
        return user;
    }
}
