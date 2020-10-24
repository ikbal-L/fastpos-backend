package com.softlines.fastpos;

import com.softlines.fastpos.jwtsecurity.jwtcontroller.UserController;
import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securityrepository.JWTuserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.softlines.fastpos.jwtsecurity.securityfilters.JWTAuthenticationFilter.createToken;
import static com.softlines.fastpos.jwtsecurity.securityfilters.SecurityConstants.HEADER_STRING;
import static com.softlines.fastpos.jwtsecurity.securityfilters.SecurityConstants.TOKEN_PREFIX;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude= SecurityAutoConfiguration.class)
class ModelApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private JWTuserRepository jwTuserRepository;

	@Autowired
	private UserController userController;

	@Test
	public void addingUsersGetsUsers() throws Exception {
		createTestUser("TestAdmin");
		createTestUser("TestUser");

		mvc.perform(get("/dbtest/users").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("TestAdmin")))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
				.andExpect(jsonPath("$[4].username", is("TestAdmin")))
				.andExpect(jsonPath("$[5].username", is("TestUser")));

		deleteTestUser("TestAdmin");
		deleteTestUser("TestUser");
	}

	@Test
	public void testUserPrivilege() throws Exception{
		mvc.perform(get("/dbtest/")
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("user"))
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isForbidden());

	}

	@Test
	public void testAdminPrivilege() throws Exception{


		mvc.perform(get("/dbtest/")
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("admin"))
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

	}

	@Test
	public void testUsersNumber() throws Exception{

		mvc.perform(get("/dbtest/users").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("admin")))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(equalTo(4))))
				.andExpect(jsonPath("$[0].username", is("admin")))
				.andExpect(jsonPath("$[1].username", is("user")))
				.andExpect(jsonPath("$[2].username", is("user3")))
				.andExpect(jsonPath("$[3].username", is("user2")));

	}

	@Test
	public void testWhenUserWithUserRoleDeleteAnotherUser() throws Exception{

		mvc.perform(delete("/delete/user2").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("user")))
				.andDo(print())
				.andExpect(status().isForbidden());

	}

	@Test
	public void adminDeleteUserAndReturn200() throws Exception{

		createTestUser("TestUser");
		mvc.perform(delete("/delete/TestUser").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("admin")))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void getByIdWillReturnTheSameUserThen200() throws Exception{

		mvc.perform(get("/getbyID/{id}", 1l).contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("admin")))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("username", is("admin")));
	}

	@Test
	public void testController() throws Exception{

		JWTuser jwTuser = userController.getById(1l);

		assertEquals(jwTuser.getUsername(), jwTuserRepository.findById(1l).get().getUsername());
	}

	private void deleteTestUser(String name) {
		jwTuserRepository.delete(jwTuserRepository.findByUsername(name));
	}

	private void createTestUser(String name) {
		JWTuser jwTuser = new JWTuser();
		jwTuser.setUsername(name);
		jwTuser.setPassword("password");
		jwTuserRepository.saveAndFlush(jwTuser);
	}
}
