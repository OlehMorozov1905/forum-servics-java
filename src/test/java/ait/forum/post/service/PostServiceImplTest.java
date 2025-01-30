package ait.forum.post.service;

import ait.forum.post.dao.PostRepository;
import ait.forum.post.dto.DatePeriodDto;
import ait.forum.post.dto.NewCommentDto;
import ait.forum.post.dto.NewPostDto;
import ait.forum.post.dto.PostDto;
import ait.forum.post.model.Post;
import ait.forum.post.dto.exceptions.PostNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Optional;
import java.util.stream.Stream;

class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PostServiceImpl postService;

    private Post post;
    private PostDto postDto;
    private NewPostDto newPostDto;
    private NewCommentDto newCommentDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        post = new Post("Test title", "Test content", Set.of("tag1", "tag2"), "author1");
        post.setId("1");
        postDto = new PostDto("1", "Test title", "Test content", "author1", post.getDateCreated(), Set.of("tag1", "tag2"), 0, null);
        newPostDto = new NewPostDto("Updated title", "Updated content", Set.of("tag3", "tag4"));
        newCommentDto = new NewCommentDto("This is a comment");

        when(modelMapper.map(any(), eq(PostDto.class))).thenReturn(postDto);
        when(modelMapper.map(any(), eq(Post.class))).thenReturn(post);
    }

    @Test
    void addNewPost_ShouldReturnPostDto() {
        when(postRepository.save(any(Post.class))).thenReturn(post);

        PostDto result = postService.addNewPost("author1", newPostDto);

        assertNotNull(result);
        assertEquals("Test title", result.getTitle());
        assertEquals("Test content", result.getContent());
        assertEquals("author1", result.getAuthor());
        assertEquals(0, result.getLikes());
    }

    @Test
    void findPostById_ShouldReturnPostDto() {
        when(postRepository.findById("1")).thenReturn(Optional.of(post));

        PostDto result = postService.findPostById("1");

        assertNotNull(result);
        assertEquals("Test title", result.getTitle());
        assertEquals("Test content", result.getContent());
        assertEquals("author1", result.getAuthor());
    }

    @Test
    void findPostById_ShouldThrowPostNotFoundException() {
        when(postRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.findPostById("1"));
    }

    @Test
    void removePost_ShouldReturnPostDto() {
        when(postRepository.findById("1")).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        PostDto result = postService.removePost("1");

        assertNotNull(result);
        assertEquals("Test title", result.getTitle());
        assertEquals("Test content", result.getContent());
    }

    @Test
    void addLike_ShouldIncreaseLikes() {
        when(postRepository.findById("1")).thenReturn(Optional.of(post));

        postService.addLike("1");

        assertEquals(1, post.getLikes());
    }

    @Test
    void findPostsByAuthor_ShouldReturnPostDtos() {
        when(postRepository.findByAuthorIgnoreCase("author1")).thenReturn(Stream.of(post));

        Iterable<PostDto> result = postService.findPostsByAuthor("author1");

        assertNotNull(result);
        assertTrue(result.iterator().hasNext());
    }

    @Test
    void findPostsByTags_ShouldReturnPostDtos() {
        Set<String> tags = new HashSet<>();
        tags.add("tag1");

        when(postRepository.findByTagsInIgnoreCase(tags)).thenReturn(Stream.of(post));

        Iterable<PostDto> result = postService.findPostsByTags(tags);

        assertNotNull(result);
        assertTrue(result.iterator().hasNext());
    }

    @Test
    void findPostsByPeriod_ShouldReturnPostDtos() {
        DatePeriodDto datePeriodDto = new DatePeriodDto(LocalDate.now().minusDays(1), LocalDate.now());
        when(postRepository.findByDateCreatedBetween(datePeriodDto.getDateFrom(), datePeriodDto.getDateTo())).thenReturn(Stream.of(post));

        Iterable<PostDto> result = postService.findPostsByPeriod(datePeriodDto);

        assertNotNull(result);
        assertTrue(result.iterator().hasNext());
    }
}

