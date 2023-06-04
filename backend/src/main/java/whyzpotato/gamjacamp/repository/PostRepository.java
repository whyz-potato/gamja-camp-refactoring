package whyzpotato.gamjacamp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import whyzpotato.gamjacamp.domain.post.Post;
import whyzpotato.gamjacamp.domain.post.PostType;

public interface PostRepository extends JpaRepository<Post, Long>{
    Page<Post> findByIdLessThanAndType(Long id, PostType type, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE (p.title LIKE %:title% OR p.content LIKE %:content%) AND p.id < :id AND p.type = :type")
    Page<Post> findByTitleOrContentContainingAndIdLessThanAndType(@Param("title") String title, @Param("content") String content, @Param("id") Long id, @Param("type") PostType type, Pageable pageable);
}
