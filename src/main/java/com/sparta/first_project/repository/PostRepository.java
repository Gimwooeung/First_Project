package com.sparta.first_project.repository;

import com.sparta.first_project.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
//
//    @Query("select p.id, p.title, p.content, p.author, p.createdAt, p.modifiedAt from Post p order by p.createdAt desc")
//    Page<Post> findAllByOrderByModifiedAtDesc(Pageable pageable);//페이징 //최신순 정렬
//
//
//    //댓글 순 정렬
//    @Query("select p.id, p.title, p.content, p.author, p.createdAt, p.modifiedAt from Post p order by p.commentList.size desc")
//    Page<Post> findAllByOrderByCommentCountDesc(Pageable pageable);//페이징 //댓글순 정렬

}
