package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import static org.assertj.core.api.Assertions.assertThat;


//@SpringBootTest
//@AutoConfigureTestDatabase
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
//	private final UserDbStorage userStorage;
	@Test
	void contextLoads() {
	}
//	@Test
//	public void testFindUserById() {
//		User user = userStorage.findUser(1);
//		assertThat(user).hasFieldOrPropertyWithValue("id", 1);
//	}
}