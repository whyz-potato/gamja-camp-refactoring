package whyzpotato.gamjacamp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.controller.dto.GatherPostDto;
import whyzpotato.gamjacamp.controller.dto.GatherPostDto.GatherPostDetail;
import whyzpotato.gamjacamp.controller.dto.GatherPostDto.GatherPostSaveRequest;
import whyzpotato.gamjacamp.controller.dto.GatherPostDto.GatherPostSimple;
import whyzpotato.gamjacamp.controller.dto.GatherPostDto.GatherPostUpdateRequest;
import whyzpotato.gamjacamp.controller.dto.GeneralPostDto.GeneralPostDetail;
import whyzpotato.gamjacamp.controller.dto.GeneralPostDto.GeneralPostSimple;
import whyzpotato.gamjacamp.controller.dto.GeneralPostDto.GeneralPostUpdateRequest;
import whyzpotato.gamjacamp.domain.Image;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.post.Post;
import whyzpotato.gamjacamp.domain.post.PostType;
import whyzpotato.gamjacamp.controller.dto.GeneralPostDto.GeneralPostSaveRequest;
import whyzpotato.gamjacamp.repository.ImageRepository;
import whyzpotato.gamjacamp.repository.MemberRepository;
import whyzpotato.gamjacamp.repository.PostRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;

    /**
     * 자유게시판 글 쓰기
     */
    public Long saveGeneralPost(Long memberId, GeneralPostSaveRequest request, List<String> fileNameList) {
        Member member = memberRepository.findById(memberId).get();
        Post post = postRepository.save(request.toEntity(member, PostType.GENERAL));
        if (fileNameList == null)
            return post.getId();
        for(String fileName : fileNameList) {
            String fileUrl = "https://gamja-camp.s3.ap-northeast-2.amazonaws.com/" + fileName;
            post.getImages().add(imageRepository.save(Image.builder().post(post).fileName(fileName).path(fileUrl).build()));
        }
        return post.getId();
    }

    /**
     * 자유게시판 글 불러오기
     * TODO comment list 조회
     */
    @Transactional(readOnly = true)
    public GeneralPostDetail findGeneralPost(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        Post generalPost = optionalPost.orElseThrow(() -> new NoSuchElementException());
        return new GeneralPostDetail(generalPost);
    }

    /**
     * 자유게시판 글 목록 불러오기
     */
    public Page<GeneralPostSimple> findGeneralPostList(Long lastPostId) {
        Pageable sortedByIdDesc = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Post> posts = postRepository.findByIdLessThanAndType(lastPostId, PostType.GENERAL, sortedByIdDesc);
        Page<GeneralPostSimple> GeneralPostSimpleList = new GeneralPostSimple().toList(posts);
        return GeneralPostSimpleList;
    }

    /**
     * 자유게시판 글 수정
     */
    public GeneralPostDetail updateGeneralPost(Long memberId, Long postId, GeneralPostUpdateRequest request, List<String> fileNameList) {
        Member writer = memberRepository.findById(memberId).get();
        Post post = postRepository.findById(postId).get();
        if(post.getWriter().equals(writer)) {
            post.getImages().clear();
            if(fileNameList != null) {
                for(String fileName : fileNameList) {
                    String fileUrl = "https://gamja-camp.s3.ap-northeast-2.amazonaws.com/" + fileName;
                    post.getImages().add(imageRepository.save(Image.builder().post(post).fileName(fileName).path(fileUrl).build()));
                }
            }
            postRepository.save(post.update(request));
            return new GeneralPostDetail(post);
        }
        throw new NoSuchElementException();
    }

    /**
     * 자유게시판 글 삭제
     */
    public void deleteGeneralPost(Long memberId, Long postId) {
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
    public Page<GeneralPostSimple> searchGeneralPost(Long lastPostId, String keyword) {
        Pageable sortedByIdDesc = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Post> posts = postRepository.findByTitleOrContentContainingAndIdLessThanAndType(keyword, keyword, lastPostId, PostType.GENERAL, sortedByIdDesc);
        Page<GeneralPostSimple> SimpleGeneralPostResponseDtoList = new GeneralPostSimple().toList(posts);
        return SimpleGeneralPostResponseDtoList;
    }

    public List<Image> findGeneralPostImages(Long memberId, Long postId) {
        Post post = postRepository.findById(postId).get();
        Member member = memberRepository.findById(memberId).get();
        if (post.getWriter().equals(member)) {
//            List<Image> images = imageRepository.findAllByPost(post);
            return post.getImages();
        }
        throw new NoSuchElementException();
    }

    /**
     * 모집게시판 글 쓰기
     */
    public Long saveGatherPost(Long memberId, GatherPostSaveRequest request, List<String> fileNameList) {
        Member member = memberRepository.findById(memberId).get();
        Post post = postRepository.save(request.toEntity(member, PostType.GATHER));
        if(fileNameList == null)
            return post.getId();
        for(String fileName : fileNameList) {
            String fileUrl = "https://gamja-camp.s3.ap-northeast-2.amazonaws.com/" + fileName;
            post.getImages().add(imageRepository.save(Image.builder().post(post).fileName(fileName).path(fileUrl).build()));
        }
        return post.getId();
    }

    /**
     * 모집게시판 글 불러오기
     */
    @Transactional(readOnly = true)
    public GatherPostDetail findGatherPost(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        Post gatherPost = optionalPost.orElseThrow(() -> new NoSuchElementException());
        return new GatherPostDetail(gatherPost);
    }

    /**
     * 모집게시판 글 목록 불러오기
     */
    public Page<GatherPostSimple> findGatherPostList(Long lastPostId) {
        Pageable sortedByIdDesc = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Post> posts = postRepository.findByIdLessThanAndType(lastPostId, PostType.GATHER, sortedByIdDesc);
        Page<GatherPostSimple> gatherPostSimpleList = new GatherPostSimple().toList(posts);
        return gatherPostSimpleList;
    }

    /**
     * 모집게시판 글 수정
     */
    public GatherPostDetail updateGatherPost(Long memberId, Long postId, GatherPostUpdateRequest request, List<String> fileNameList) {
        Member writer = memberRepository.findById(memberId).get();
        Post post = postRepository.findById(postId).get();
        if(post.getWriter().equals(writer)) {
            post.getImages().clear();
            if(fileNameList != null) {
                for (String fileName : fileNameList) {
                    String fileUrl = "https://gamja-camp.s3.ap-northeast-2.amazonaws.com/" + fileName;
                    post.getImages().add(imageRepository.save(Image.builder().post(post).fileName(fileName).path(fileUrl).build()));
                }
            }
            postRepository.save(post.update(request));
            return new GatherPostDetail(post);
        }
        throw new NoSuchElementException();
    }

    /**
     * 모집게시판 글 삭제
     */
    public void deleteGatherPost(Long memberId, Long postId) {
        Member writer = memberRepository.findById(memberId).get();
        Post post = postRepository.findById(postId).get();
        if(post.getWriter().equals(writer)) {
            postRepository.delete(post);
            return;
        }
        throw new NoSuchElementException();
    }

    /**
     * 모집게시판 글 검색
     */
    public Page<GatherPostSimple> searchGatherPost(Long lastPostId, String keyword) {
        Pageable sortedByIdDesc = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Post> posts = postRepository.findByTitleOrContentContainingAndIdLessThanAndType(keyword, keyword, lastPostId, PostType.GATHER, sortedByIdDesc);
        Page<GatherPostSimple> simpleGatherPostRequestDtoList = new GatherPostSimple().toList(posts);
        return simpleGatherPostRequestDtoList;
    }

    public List<Image> findGatherPostImages(Long memberId, Long postId) {
        Member member = memberRepository.findById(memberId).get();
        Post post = postRepository.findById(postId).get();
        if(post.getWriter().equals(member)) {
//            List<Image> images = imageRepository.findAllByPost(post);
            return post.getImages();
        }
        throw new NoSuchElementException();
    }

}
