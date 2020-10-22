package com.softlines.fastpos;

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

import static com.softlines.fastpos.jwtsecurity.securityfilters.SecurityConstants.HEADER_STRING;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

	@Test
	public void givenEmployees_whenGetEmployees_thenStatus200() throws Exception {
		createTestUser("TestAdmin");
		createTestUser("TestUser");

		mvc.perform(get("/dbtest/users").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, "Bearer " +
						"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImR" +
						"iSUQiOjEsImV4cCI6MTYwMzQ0NTQxM30.wMP6NfPVlIN2CQ30o_uaEGfB-Ol" +
						"iI6o2kfA35TTWmHHeO8FZpKz5_DLOb0LFhx7lVrkkAbC-eKx1SzqwXpufBA"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
				.andExpect(jsonPath("$[2].username", is("TestAdmin")))
				.andExpect(jsonPath("$[3].username", is("TestUser")));

		deleteTestUser("TestAdmin");
		deleteTestUser("TestUser");
	}

	@Test
	public void testUserPrivilege() throws Exception{


		mvc.perform(get("/dbtest/")
				.header(HEADER_STRING, "Bearer " +
						"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiZGJJRCI6" +
						"MiwiZXhwIjoxNjAzNDYxMzk4fQ.QQqCWHHdBBqmXRTus828hMIwHXxYRpN2GZhq3Y3dAAsD" +
						"rYe6Tz8NCFiN87gdWEJSDwfuy0BH7cNQlTTPCx6HqA")
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isForbidden());

	}

	@Test
	public void testAdminPrivilege() throws Exception{


		mvc.perform(get("/dbtest/")
				.header(HEADER_STRING, "Bearer " +
						"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImRiSUQi" +
						"OjEsImV4cCI6MTYwMzQ2Mjg4OX0.ARMehJRAY3CW56F3Kq07neiZp5Rds8aSusuJt" +
						"lMTFBnrYP6NMtnwdbjyRNsuY0avTO51-01_OmCv74Tx08qPwQ")
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

	}

	@Test
	public void testUsersNumber() throws Exception{

		mvc.perform(get("/dbtest/users").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9." +
						"eyJzdWIiOiJhZG1pbiIsImRiSUQiOjEsImV4cCI6MTYwMzQ0NTQxM30.wMP6NfPVl" +
						"IN2CQ30o_uaEGfB-OliI6o2kfA35TTWmHHeO8FZpKz5_DLOb0LFhx7lVrkkAbC-eK" +
						"x1SzqwXpufBA"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(equalTo(2))))
				.andExpect(jsonPath("$[0].username", is("admin")))
				.andExpect(jsonPath("$[1].username", is("user")));

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
