package whyzpotato.gamjacamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whyzpotato.gamjacamp.domain.post.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
