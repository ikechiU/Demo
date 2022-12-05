package com.example.demotest.controller;

import com.example.demotest.model.UserRequest;
import com.example.demotest.service.UserService;
import com.example.demotest.shared.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserService mockUserService;

    private MockMvc mockMvc;
    private UserDto userDto;
    private List<UserDto> userDtoList;

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Rahman");
        userDto.setContact("09034567891");
        userDto.setPassword("12345");

        UserDto userDto2 = new UserDto();
        userDto2.setId(2L);
        userDto2.setName("Badmus");
        userDto2.setContact("09034567893");
        userDto2.setPassword("12345");

        userDtoList = new ArrayList<>();
        userDtoList.add(userDto);
        userDtoList.add(userDto2);
    }

    @Test
    void createUser() throws Exception {
        when(mockUserService.createUser(any(UserDto.class))).thenReturn(userDto);

        String jsonBody = "{\"name\": \"Rahman\", \"contact\":\"09034567891\", \"password\":\"12345\"}";

        MvcResult mvcResult = mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Rahman"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contact").value("09034567891"))
                .andReturn();

        assertEquals("application/json", mvcResult.getResponse().getContentType());
        verify(mockUserService, times(1)).createUser(any(UserDto.class));
    }

    @Test
    void updateUser() throws Exception {
        when(mockUserService.updateUser(eq(1L), any(UserDto.class))).thenReturn(userDto);

        String jsonBody = "{\"name\": \"Rahman\", \"contact\":\"09034567891\", \"password\":\"12345\"}";

        MvcResult mvcResult = mockMvc.perform(
                put("/users/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Rahman"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contact").value("09034567891"))
                .andReturn();

        assertEquals("application/json", mvcResult.getResponse().getContentType());
        verify(mockUserService, times(1)).updateUser(eq(1L), any(UserDto.class));
    }

    @Test
    void getUser() throws Exception {
        when(mockUserService.getUser(1L)).thenReturn(userDto);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/users/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andReturn();

        assertEquals("application/json", mvcResult.getResponse().getContentType());
        verify(mockUserService, times(1)).getUser(1L);
    }

    @Test
    void getUsers() throws Exception {
        when(mockUserService.getUsers()).thenReturn(userDtoList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/users")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].name").value("Badmus"))
                .andReturn();

        assertEquals("application/json", mvcResult.getResponse().getContentType());
        verify(mockUserService, times(1)).getUsers();
    }

    @Test
    void deleteUser() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .delete("/users/{id}", 1))
                .andExpect(status().isOk())
                .andReturn();

        assertNull( mvcResult.getResponse().getContentType());
        verify(mockUserService, times(1)).deleteUser(anyLong());
    }
}