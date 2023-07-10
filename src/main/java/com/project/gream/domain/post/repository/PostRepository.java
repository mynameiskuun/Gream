package com.project.gream.domain.post.repository;

import com.project.gream.common.enumlist.PostType;
import com.project.gream.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByPostTypeOrderByCreatedTimeDesc(PostType postType, Pageable pageable);
    Page<Post> findAllByMember_IdAndPostTypeOrderByCreatedTimeDesc(String memberId, PostType postType, Pageable pageable);
    List<Post> findTop4ByMember_IdAndPostTypeOrderByCreatedTimeDesc(String MemberId, PostType postType);
}
