package ait.forum.post.service;

import ait.forum.post.dto.DatePeriodDto;
import ait.forum.post.dto.NewCommentDto;
import ait.forum.post.dto.NewPostDto;
import ait.forum.post.dto.PostDto;

import java.util.Set;

public interface PostService {
    PostDto addNewPost(String author, NewPostDto newPostDto);

    PostDto findPostById(String id);

    PostDto removePost(String id);

    PostDto updatePost(String id, NewPostDto newPostDto);

    PostDto addComment(String id, String author, NewCommentDto newCommentDto);

    void addLike(String id);

    Iterable<PostDto> findPostsByAuthor(String author);

    Iterable<PostDto> findPostsByTags(Set<String> tags);

    Iterable<PostDto> findPostsByPeriod(DatePeriodDto datePeriodDto);
}
