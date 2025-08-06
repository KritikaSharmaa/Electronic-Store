package com.electronic.store.Electronic_Store;

import com.electronic.store.Electronic_Store.Entities.User;
import com.electronic.store.Electronic_Store.Repositories.userRepository;
import com.electronic.store.Electronic_Store.Security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ElectronicStoreApplicationTests {

	@Autowired
	userRepository userRepository;

	@Autowired
	JwtUtil jwtUtil;

	@Test
	void contextLoads() {
	}

	@Test
	void testToken(){
		User user = userRepository.findByEmail("Kriti@gmail.com").orElse(null);
		if(user!=null){
			String token = jwtUtil.generateToken(user);
			System.out.println("Token: "+ token);

			System.out.println("UserName: "+ jwtUtil.extractUsername(token));

			System.out.println("Expiration date: "+ jwtUtil.getExpirationDate(token));

			System.out.println("is Token expired: "+ jwtUtil.isTokenExpired(token));
		}else System.out.println("user not found");
	}

}
