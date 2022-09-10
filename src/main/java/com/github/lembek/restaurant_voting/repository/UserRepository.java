package com.github.lembek.restaurant_voting.repository;

import com.github.lembek.restaurant_voting.model.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@CacheConfig(cacheNames = "users")
public interface UserRepository extends BaseRepository<User> {

    @Cacheable
    @Query("SELECT u FROM User u WHERE u.email = LOWER(:email)")
    Optional<User> findByEmailIgnoreCase(String email);
}
