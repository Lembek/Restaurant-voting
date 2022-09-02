package com.github.lembek.RestaurantVoting.config;

import com.github.lembek.RestaurantVoting.repository.MenuRepository;
import com.github.lembek.RestaurantVoting.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.github.lembek.RestaurantVoting.PopulateTestData.*;

@Component
@AllArgsConstructor
public class PopulateDbRunner implements CommandLineRunner {

    private final MenuRepository menuRepository;
    private final VoteRepository voteRepository;

    @Override
    public void run(String... args) throws Exception {
        menuRepository.saveAll(Arrays.asList(firstMenu1, firstMenu2, secondMenu1));
        voteRepository.saveAll(Arrays.asList(vote1, vote2));
    }
}
