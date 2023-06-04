package whyzpotato.gamjacamp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.post.Post;
import whyzpotato.gamjacamp.domain.post.PostType;
import whyzpotato.gamjacamp.dto.Post.GeneralPostDto;
import whyzpotato.gamjacamp.dto.Post.GeneralPostSaveRequestDto;
import whyzpotato.gamjacamp.dto.Post.GeneralPostUpdateRequestDto;
import whyzpotato.gamjacamp.dto.Post.SimpleGeneralPostResponseDto;
import whyzpotato.gamjacamp.repository.MemberRepository;
import whyzpotato.gamjacamp.repository.PostRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    /**
     * 자유게시판 글 쓰기
     */
    public Long saveGeneralPost(Long memberId, GeneralPostSaveRequestDto generalPostSaveRequestDto) {
        Member member = memberRepository.findById(memberId).get();
        return postRepository.save(generalPostSaveRequestDto.toEntity(member, PostType.GENERAL)).getId();
    }

    /**
     * 자유게시판 글 불러오기
     */
    @Transactional(readOnly = true)
    public GeneralPostDto findGeneralPost(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        Post generalPost = optionalPost.orElseThrow(() -> new NoSuchElementException());
        return new GeneralPostDto(generalPost);
    }

    /**
     * 자유게시판 글 목록 불러오기
     */
    public Page<SimpleGeneralPostResponseDto> findGeneralPostList(Long lastPostId, Pageable pageable) {
        Pageable sortedByIdDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").descending());
        Page<Post> posts = postRepository.findByIdLessThanAndType(lastPostId, PostType.GENERAL, sortedByIdDesc);
        Page<SimpleGeneralPostResponseDto> SimpleGeneralPostResponseDtoList = new SimpleGeneralPostResponseDto().toDtoList(posts);
        return SimpleGeneralPostResponseDtoList;
    }

    /**
     * 자유게시판 글 수정
     */
    public GeneralPostDto updateGeneralPost(Long memberId, Long postId, GeneralPostUpdateRequestDto generalPostUpdateRequestDto) {
        Member writer = memberRepository.findById(memberId).get();
        Post post = postRepository.findById(postId).get();
        if(post.getWriter().equals(writer)) {
            postRepository.save(post.update(generalPostUpdateRequestDto));
            return new GeneralPostDto(post);
        }
        throw new NoSuchElementException();
    }

    /**
     * 자유게시판 글 삭제
     */
    public void delete(Long memberId, Long postId) {
        Member writer = memberRepository.findById(memberId).get();
        Post post = postRepository.findById(postId).get();
        if(post.getWriter().equals(writer)) {
            postRepository.delete(post);
            return;
        }
        throw new NoSuchElementException();
    }

    /**
     * 자유게시판 글 검색
     */
    public Page<SimpleGeneralPostResponseDto> search(Long lastPostId, String keyword, Pageable pageable) {
        Pageable sortedByIdDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").descending());
        Page<Post> posts = postRepository.findByTitleOrContentContainingAndIdLessThanAndType(keyword, keyword, lastPostId, PostType.GENERAL, sortedByIdDesc);
        Page<SimpleGeneralPostResponseDto> SimpleGeneralPostResponseDtoList = new SimpleGeneralPostResponseDto().toDtoList(posts);
        return SimpleGeneralPostResponseDtoList;
    }
}
