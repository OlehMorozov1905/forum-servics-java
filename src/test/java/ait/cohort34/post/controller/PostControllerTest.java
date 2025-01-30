package ait.cohort34.post.controller;

import ait.forum.post.controller.PostController;
import ait.forum.post.dto.PostDto;
import ait.forum.post.dto.NewPostDto;
import ait.forum.post.dto.NewCommentDto;
import ait.forum.post.dto.DatePeriodDto;
import ait.forum.post.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PostControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
    }

    @Test
    void testAddNewPost() throws Exception {
        PostDto postDto = new PostDto("1", "Test Title", "Test Content", "testUser");
        when(postService.addNewPost(anyString(), any(NewPostDto.class))).thenReturn(postDto);

        mockMvc.perform(post("/forum/post/testUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test Title\",\"content\":\"Test Content\",\"tags\":[\"tag1\",\"tag2\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"));
    }

    @Test
    void testFindPostById() throws Exception {
        PostDto postDto = new PostDto("1", "Test Title", "Test Content", "testUser");
        when(postService.findPostById(anyString())).thenReturn(postDto);

        mockMvc.perform(get("/forum/post/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"));
    }

    @Test
    void testRemovePost() throws Exception {
        PostDto postDto = new PostDto("1", "Test Title", "Test Content", "testUser");
        when(postService.removePost(anyString())).thenReturn(postDto);

        mockMvc.perform(delete("/forum/post/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"));
    }

    @Test
    void testUpdatePost() throws Exception {
        PostDto postDto = new PostDto("1", "Updated Title", "Updated Content", "testUser");
        when(postService.updatePost(anyString(), any(NewPostDto.class))).thenReturn(postDto);

        mockMvc.perform(put("/forum/post/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Title\",\"content\":\"Updated Content\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void testAddComment() throws Exception {
        PostDto postDto = new PostDto("1", "Test Title", "Test Content", "testUser");
        when(postService.addComment(anyString(), anyString(), any(NewCommentDto.class))).thenReturn(postDto);

        mockMvc.perform(put("/forum/post/1/comment/testUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"New comment\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testAddLike() throws Exception {
        mockMvc.perform(put("/forum/post/1/like"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFindPostsByAuthor() throws Exception {
        List<PostDto> posts = List.of(new PostDto("1", "Test Title", "Test Content", "testUser"));
        when(postService.findPostsByAuthor(anyString())).thenReturn(posts);

        mockMvc.perform(get("/forum/posts/author/testUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Title"));
    }

    @Test
    void testFindPostsByTags() throws Exception {
        List<PostDto> posts = List.of(new PostDto("1", "Test Title", "Test Content", "testUser"));
        when(postService.findPostsByTags(any(Set.class))).thenReturn(posts);

        mockMvc.perform(post("/forum/posts/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"java\",\"spring\"]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Title"));
    }

    @Test
    void testFindPostsByPeriod() throws Exception {
        List<PostDto> posts = List.of(new PostDto("1", "Test Title", "Test Content", "testUser"));
        when(postService.findPostsByPeriod(any(DatePeriodDto.class))).thenReturn(posts);

        mockMvc.perform(post("/forum/posts/period")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dateFrom\":\"2023-01-01\",\"dateTo\":\"2023-12-31\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Title"));
    }
}