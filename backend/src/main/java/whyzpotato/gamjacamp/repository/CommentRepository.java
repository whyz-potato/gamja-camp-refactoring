package whyzpotato.gamjacamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whyzpotato.gamjacamp.domain.post.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {


}
