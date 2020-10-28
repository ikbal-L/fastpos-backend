package com.softlines.fastpos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import org.junit.jupiter.api.Order;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude= SecurityAutoConfiguration.class)
class PrivilegeControllerTests {

	@Autowired
	private MockMvc mvc;

    @MockBean
	HttpServletResponse response;

	@Test
    @Order(0)
	public void userAddingPrivilegeReturn403() throws Exception{
        Privilege privilege = new Privilege();
        privilege.setName("edit");
		mvc.perform(post("/privilege/save")
				.header(HEADER_STRING, TOKEN_PREFIX + createToken("user"))
				.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(privilege)))
				.andDo(print())
				.andExpect(status().isForbidden());
	}

    @Test
    @Order(1)
    public void adminAddingPrivilegeReturn201() throws Exception{
        Privilege privilege = new Privilege();
        privilege.setName("edit");
        mvc.perform(post("/privilege/save")
                .header(HEADER_STRING, TOKEN_PREFIX + createToken("admin"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(privilege)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    public void adminAddingExistingPrivilegeReturn204() throws Exception{
        Privilege privilege = new Privilege();
        privilege.setName("write");
        mvc.perform(post("/privilege/save")
                .header(HEADER_STRING, TOKEN_PREFIX + createToken("admin"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(privilege)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(3)
    public void userDeletingPrivilegeReturn403() throws Exception{
        mvc.perform(delete("/privilege/deletebyname/{privilegeName}", "edit")
                .header(HEADER_STRING, TOKEN_PREFIX + createToken("user"))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(4)
    public void adminDeletingPrivilegeReturn202() throws Exception{
        mvc.perform(delete("/privilege/deletebyname/{privilegeName}", "edit")
                .header(HEADER_STRING, TOKEN_PREFIX + createToken("admin"))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    @Order(5)
    public void adminDeletingNonExistingPrivilegeReturn204() throws Exception{
        mvc.perform(delete("/privilege/deletebyname/{privilegeName}", "edit0")
                .header(HEADER_STRING, TOKEN_PREFIX + createToken("admin"))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(6)
    public void adminEditingNonExistingPrivilegeReturn404() throws Exception{
        Privilege privilege = new Privilege();
        privilege.setName("edit");
        mvc.perform(put("/privilege/edit/{privilegeId}",20l)
                .header(HEADER_STRING, TOKEN_PREFIX + createToken("admin"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(privilege)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
