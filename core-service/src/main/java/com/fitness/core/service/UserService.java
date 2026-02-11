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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final KafkaEventProducer eventProducer;  // 👈 ДОБАВЛЯЕМ!

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
        user.setLevelNum(1); // Начальный уровень
        user.setExperiencePoints(0);
        user.setStreakDays(0);
        user.setLastActivityAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        log.info("✅ Пользователь успешно создан с id: {}", savedUser.getId());

        // 🚀 1. ОТПРАВЛЯЕМ СОБЫТИЕ - Новый пользователь зарегистрирован!
        eventProducer.sendUserRegisteredEvent(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail()
        );

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

        user.setLastActivityAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);

        log.info("✅ Пользователь успешно обновлен с id: {}", id);
        return convertToDTO(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        log.info("Удаление пользователя с id: {}", id);

        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Пользователь не найден с id: " + id);
        }

        userRepository.deleteById(id);
        log.info("✅ Пользователь успешно удален с id: {}", id);
    }

    @Transactional
    public UserDTO addExperience(Long id, Integer points) {
        log.info("➕ Добавление {} очков опыта пользователю с id: {}", points, id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден с id: " + id));

        int oldLevel = user.getLevelNum();
        user.setExperiencePoints(user.getExperiencePoints() + points);

        // Простая логика уровня: каждый уровень требует 100 * уровень опыта
        int newLevel = 1;
        int exp = user.getExperiencePoints();
        while (exp >= newLevel * 100) {
            exp -= newLevel * 100;
            newLevel++;
        }

        if (newLevel > user.getLevelNum()) {
            log.info("🎉 Уровень повышен! {} → {}", user.getLevelNum(), newLevel);
            user.setLevelNum(newLevel);
        }

        user.setLastActivityAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        log.info("✅ Текущий опыт: {}, уровень: {}", updatedUser.getExperiencePoints(), updatedUser.getLevelNum());

        return convertToDTO(updatedUser);
    }

    @Transactional
    public UserDTO incrementStreak(Long id) {
        log.info("🔥 Увеличение стрика для пользователя с id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден с id: " + id));

        int oldStreak = user.getStreakDays();
        user.setStreakDays(oldStreak + 1);
        user.setLastActivityAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        log.info("✅ Стрик пользователя {}: {} → {} дней", id, oldStreak, updatedUser.getStreakDays());

        // 🚀 2. ОТПРАВЛЯЕМ СОБЫТИЕ - Стрик обновлен!
        eventProducer.sendStreakUpdatedEvent(
                updatedUser.getId(),
                updatedUser.getStreakDays(),
                updatedUser.getStreakDays() // longest streak (пока так)
        );

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