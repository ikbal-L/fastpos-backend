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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletResponse;

import static com.softlines.fastpos.jwtsecurity.securityfilters.JWTAuthenticationFilter.createToken;
import static com.softlines.fastpos.jwtsecurity.securityfilters.SecurityConstants.HEADER_STRING;
import static com.softlines.fastpos.jwtsecurity.securityfilters.SecurityConstants.TOKEN_PREFIX;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

	@MockBean
	HttpServletResponse response;

	@Test
	public void testUserPrivilege() throws Exception{
		mvc.perform(get("/dbtest/")
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("user"))
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void differentUserDifferentDatabase_Admin() throws Exception{
		mvc.perform(get("/dbtest/")
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("admin"))
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", equalTo("Something1")))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	/*@Test
	public void differentUserDifferentDatabase_User() throws Exception{
		mvc.perform(get("/dbtest/")
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("user"))
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", equalTo("Something2")))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}*/

	@Test
	public void testUsersNumber() throws Exception{

		mvc.perform(get("/getallusers").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("admin")))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(equalTo(2))))
				.andExpect(jsonPath("$[0].username", is("admin")))
				.andExpect(jsonPath("$[1].username", is("user")));

	}

	@Test
	public void testWhenUserWithUserRoleDeleteAnotherUser() throws Exception{
		mvc.perform(delete("/deleteuser/user").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("user")))
				.andDo(print())
				.andExpect(status().isForbidden());
	}

	@Test
	public void adminDeleteUserAndReturn200() throws Exception{
		createTestUser("TestUser");
		mvc.perform(delete("/deleteuser/{username}","TestUser").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("admin")))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void getByUsernameWillReturnTheSameUserThen200() throws Exception{

		mvc.perform(get("/getuserbyusername/{username}", "admin").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("admin")))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("username", is("admin")));
	}

	@Test
	public void addingNewPrivilegeByUserWillReturn403() throws Exception{
		mvc.perform(put("/addprivilege/{privilegeName}", "edit").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("user")))
				.andDo(print())
				.andExpect(status().isForbidden());
	}

	@Test
	public void addingNewPrivilegeByAdminWillReturn200() throws Exception{
		mvc.perform(put("/addprivilege/{privilegeName}", "edit").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("admin")))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void deletingPrivilegeByUserWillReturn403() throws Exception{
		mvc.perform(delete("/deleteprivilege/{privilegeName}", "edit").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("user")))
				.andDo(print())
				.andExpect(status().isForbidden());
	}

	@Test
	public void deletingPrivilegeByAdminWillReturn200() throws Exception{
		mvc.perform(delete("/deleteprivilege/{privilegeName}", "edit").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("admin")))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void addingNewRoleByUserWillReturn403() throws Exception{
		mvc.perform(put("/addrole/{roleName}", "hr").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("user")))
				.andDo(print())
				.andExpect(status().isForbidden());
	}

	@Test
	public void addingNewRoleByAdminWillReturn200() throws Exception{
		mvc.perform(put("/addrole/{roleName}", "hr").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("admin")))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void deletingRoleByUserWillReturn403() throws Exception{
		mvc.perform(delete("/deleterole/{roleName}", "hr").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("user")))
				.andDo(print())
				.andExpect(status().isForbidden());
	}

	@Test
	public void deletingRoleByAdminWillReturn200() throws Exception{
		mvc.perform(delete("/deleterole/{roleName}", "hr").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("admin")))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void addingExistingPrivilegeToRoleReturn400() throws Exception{
		mvc.perform(put("/addprivilegetorole/{roleName}/{privilegeName}", "finance", "update").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("admin")))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	public void addingPrivilegeToNonExistedRoleReturn400() throws Exception{
		mvc.perform(put("/addprivilegetorole/{roleName}/{privilegeName}", "hr", "update").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("admin")))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	public void removingNotContainedPrivilegeFromRoleReturn400() throws Exception{
		mvc.perform(delete("/removeprivilegefromrole/{roleName}/{privilegeName}", "finance", "read").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("admin")))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	public void removingPrivilegeFromNonExistedRoleReturn400() throws Exception{
		mvc.perform(delete("/removeprivilegefromrole/{roleName}/{privilegeName}", "hr", "update").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("admin")))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	public void removingNonExistedPrivilegefromRoleReturn400() throws Exception{
		mvc.perform(delete("/removeprivilegefromrole/{roleName}/{privilegeName}", "finance", "add").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("admin")))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	public void addingNonExistedRoleToUserReturn400() throws Exception{
		mvc.perform(put("/addroletouser/{userName}/{roleName}", "admin", "hr").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("admin")))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	public void addingPrivilegeToNonExistedUserReturn400() throws Exception{
		mvc.perform(put("/addroletouser/{userName}/{roleName}", "user0", "finance").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("admin")))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	public void addingContainedRoleToUserReturn400() throws Exception{
		mvc.perform(put("/addroletouser/{userName}/{roleName}", "admin", "admin").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("admin")))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	public void removingNonExistedRoleFromUserReturn400() throws Exception{
		mvc.perform(delete("/removerolefromuser/{userName}/{roleName}", "admin", "hr").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("admin")))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	public void removingPrivilegeFromNonExistedUserReturn400() throws Exception{
		mvc.perform(delete("/removerolefromuser/{userName}/{roleName}", "user0", "finance").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("admin")))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	public void removingNonContainedRoleFromUserReturn400() throws Exception{
		mvc.perform(delete("/removerolefromuser/{userName}/{roleName}", "admin", "user").contentType(MediaType.APPLICATION_JSON)
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("admin")))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}


	@Test
	public void gettingNonExistedUserReturnNull(){
		assertEquals(null, userController.getByUsername("user0", response));
	}

	@Test
	public void getAllUsersListHasSize2(){
		assertEquals(2, userController.getAllUsers(response).size());
	}

	@Test
	public void gettingNonExistedUserRolesReturnNull(){
		assertEquals(null, userController.getUserRoles("user0", response));
	}

	@Test
	public void gettingAdminRolesListHasSize2(){
		assertEquals(2, userController.getUserRoles("admin", response).size());
	}

	private void createTestUser(String name) {
		JWTuser jwTuser = new JWTuser();
		jwTuser.setUsername(name);
		jwTuser.setPassword("password");
		jwTuserRepository.saveAndFlush(jwTuser);
	}
}
