package com.human.web_board.repository;

import com.human.web_board.Entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Posts, Long> {
}