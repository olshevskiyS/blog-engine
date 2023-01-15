package ru.olshevskiy.blogengine;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * MethodSecurityIntegrationTest.
 *
 * @author Sergey Olshevskiy
 */
@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "/create-test-data.sql")
@Sql(scripts = "/clean-test-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class MethodSecurityIntegrationTest extends InitTestContainer {

  @Autowired
  private WebApplicationContext context;

  private MockMvc mockMvc;

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
  }

  @Test
  @WithAnonymousUser
  void testPermissionAnonymousUser() throws Exception {
    mockMvc.perform(get("/api/post/moderation?offset=0&limit=10&status=accepted")
                    .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isUnauthorized());
  }

  @Test
  @WithUserDetails(value = "user02@email.com",
          userDetailsServiceBeanName = "userDetailsServiceImpl")
  void testPermissionAuthenticatedUser() throws Exception {
    mockMvc.perform(get("/api/post/moderation?offset=0&limit=10&status=accepted")
                    .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isForbidden());
  }

  @Test
  @WithUserDetails(value = "user01@email.com",
          userDetailsServiceBeanName = "userDetailsServiceImpl")
  void testPermissionModerator() throws Exception {
    mockMvc.perform(get("/api/post/moderation?offset=0&limit=10&status=accepted")
                    .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk());
  }
}
