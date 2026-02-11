package com.fitness.core.service;

import com.fitness.core.dto.CreateUserRequest;
import com.fitness.core.dto.UpdateUserRequest;
import com.fitness.core.dto.UserDTO;
import com.fitness.core.exception.ResourceNotFoundException;
import com.fitness.core.model.User;
import com.fitness.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public List<UserDTO> getAllUsers() {
        log.info("Получение всех пользователей");
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        log.info("Получение пользователя по id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден с id: " + id));
        return convertToDTO(user);
    }

    public UserDTO getUserByUsername(String username) {
        log.info("Получение пользователя по имени пользователя: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден с именем пользователя: " + username));
        return convertToDTO(user);
    }

    @Transactional
    public UserDTO createUser(CreateUserRequest request) {
        log.info("Создание нового пользователя с именем пользователя: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Имя пользователя уже существует: " + request.getUsername());
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email уже существует: " + request.getEmail());
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // TODO: Хэшировать пароль
        user.setFullName(request.getFullName());
        user.setBirthDate(request.getBirthDate());
        user.setWeightKg(request.getWeightKg());
        user.setHeightCm(request.getHeightCm());
        user.setLastActivityAt(java.time.LocalDateTime.now());

        User savedUser = userRepository.save(user);
        log.info("Пользователь успешно создан с id: {}", savedUser.getId());

        return convertToDTO(savedUser);
    }

    @Transactional
    public UserDTO updateUser(Long id, UpdateUserRequest request) {
        log.info("Обновление пользователя с id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден с id: " + id));

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getBirthDate() != null) {
            user.setBirthDate(request.getBirthDate());
        }
        if (request.getWeightKg() != null) {
            user.setWeightKg(request.getWeightKg());
        }
        if (request.getHeightCm() != null) {
            user.setHeightCm(request.getHeightCm());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }

        user.setLastActivityAt(java.time.LocalDateTime.now());
        User updatedUser = userRepository.save(user);

        log.info("Пользователь успешно обновлен с id: {}", id);
        return convertToDTO(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        log.info("Удаление пользователя с id: {}", id);

        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Пользователь не найден с id: " + id);
        }

        userRepository.deleteById(id);
        log.info("Пользователь успешно удален с id: {}", id);
    }

    @Transactional
    public UserDTO addExperience(Long id, Integer points) {
        log.info("Добавление {} очков опыта пользователю с id: {}", points, id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден с id: " + id));

        user.setExperiencePoints(user.getExperiencePoints() + points);
        user.setLastActivityAt(java.time.LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    @Transactional
    public UserDTO incrementStreak(Long id) {
        log.info("Увеличение стрика для пользователя с id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден с id: " + id));

        user.setStreakDays(user.getStreakDays() + 1);
        user.setLastActivityAt(java.time.LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setBirthDate(user.getBirthDate());
        dto.setWeightKg(user.getWeightKg());
        dto.setHeightCm(user.getHeightCm());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setLevelNum(user.getLevelNum());
        dto.setExperiencePoints(user.getExperiencePoints());
        dto.setStreakDays(user.getStreakDays());
        dto.setLastActivityAt(user.getLastActivityAt());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}