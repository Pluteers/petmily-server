package com.pet.petmily.board.service;

import com.pet.petmily.board.dto.ChannelDTO;
import com.pet.petmily.board.dto.PostDTO;
import com.pet.petmily.board.entity.Category;
import com.pet.petmily.board.entity.Channel;
import com.pet.petmily.board.entity.Favorite;
import com.pet.petmily.board.entity.Post;
import com.pet.petmily.board.repository.CategoryRepository;
import com.pet.petmily.board.repository.ChannelRepository;
import com.pet.petmily.board.repository.FavoriteRepository;
import com.pet.petmily.board.repository.PostRepository;
import com.pet.petmily.board.response.Response;
import com.pet.petmily.comment.entity.Comment;
import com.pet.petmily.comment.repository.CommentRepository;
import com.pet.petmily.comment.response.CommentResponse;
import com.pet.petmily.report.ReportResponse;
import com.pet.petmily.report.entity.Report;
import com.pet.petmily.report.repository.ReportRepository;
import com.pet.petmily.user.entity.Member;
import com.pet.petmily.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final ChannelRepository channelRepository;
    private final CommentRepository commentRepository;
    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;

    //게시판 전체 조회
    @Transactional(readOnly = true)
    public List<PostDTO> getAllPost(Long channelId) {
        List<Post> posts = postRepository.findAllByChannel_ChannelId(channelId);
        List<PostDTO> postDtos = new ArrayList<>();
        posts.forEach(s -> postDtos.add(PostDTO.toDto(s)));


        return postDtos;

    }

    //개별 게시글 조회
    @Transactional
    public PostDTO getPost(Long channelId, Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다=" + id));
        if (post.getChannel().getChannelId() != channelId) {
            throw new IllegalArgumentException("해당 게시글이 없습니다=" + id);
        }
        post.setHit(post.getHit() + 1);
        log.info("조회수 증가");
        log.info("조회수:{}", post.getHit());
        return PostDTO.toDto(post);
    }


    //게시글 작성
    @Transactional
    public PostDTO writePost(PostDTO postDto, Member member, ChannelDTO channelDTO) {
        Post post = new Post();
        Channel channel = channelRepository.findById(channelDTO.getChannelId())
                .orElseThrow(() -> new IllegalArgumentException("해당 채널이 없습니다. id=" + channelDTO.getChannelId()));
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setMember(member);
        post.setImagePath(postDto.getImagePath());
        post.setStatus(true);

        post.setChannel(channel);
        post.setLikePost(0);
        post.setHit(1);
        postRepository.save(post);
        return postDto.toDto(post);
    }


    public boolean isWriter(Long postId, long memberId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            return post.getMember().getId() == memberId;
        }
        // Handle the case when the post is not found
        throw new IllegalArgumentException("Post not found.  postId=" + postId);


    }
    @Transactional
    public PostDTO updatePost(Long channelId, Long id, PostDTO postDto) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            if (post.getChannel().getChannelId() != channelId) {
                throw new IllegalArgumentException("해당 게시글이 없습니다=" + id);
            }
            post.setTitle(postDto.getTitle());
            post.setContent(postDto.getContent());
            post.setImagePath(postDto.getImagePath());
            postRepository.save(post);
            return postDto.toDto(post);
        } else {
            throw new IllegalArgumentException("해당 게시글이 없습니다=" + id);
        }

    }

    @Transactional
    public Object deletePost(Long channelId, Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            if (post.getChannel().getChannelId() != channelId) {
                throw new IllegalArgumentException("해당 게시글이 없습니다=" + id);
            }
            reportRepository.deleteByPost(post);

            List<Comment> comments = post.getComments();
            for (Comment comment : comments) {
                commentRepository.delete(comment);
            }


            postRepository.delete(post);
            return "삭제되었습니다.";
        } else {
            throw new IllegalArgumentException("해당 게시글이 없습니다=" + id);
        }
    }

    public Object likePost(Long channelId, Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            if (post.getChannel().getChannelId() != channelId) {
                return ("실패 : 채널에 해당 게시글이 없습니다 다른 채널을 조회해보세요 = " + postId);
            }
            post.setLikePost(post.getLikePost() + 1);
            postRepository.save(post);
            log.info("좋아요 증가");
            log.info("좋아요:{}", post.getLikePost());
            return "성공 : 좋아요가 추가되었습니다, 현재 좋아요 수 : " + post.getLikePost();
        } else {
            return ("실패 : 해당 게시글이 없습니다 = " + postId);
        }
    }

    @Transactional
    public ReportResponse reportPost(Long postId, Long userId, String content) {
        log.info("reportPost메소드 진입");
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();


            Optional<Member> memberOptional = memberRepository.findById(userId);
            if (memberOptional.isPresent()) {
                Member member = memberOptional.get();

                Optional<Report> existingReport = reportRepository.findByPostAndMember(post, member);
                if (existingReport.isPresent()) {
                    // 이미 유저가 신고한 게시글
                    return new ReportResponse<>("false", "이미 신고한 게시글입니다.", post.getTitle(),content, null);
                }

                Report report = new Report();

                report.setPost(post);
                report.setMember(member);
                report.setContent(content);



                // Save the report entity
                reportRepository.save(report);
                int reportCount = reportRepository.countByPost(post);
                if (reportCount >= 3) {

                    deletePost(post.getChannel().getChannelId(), postId);  // DeletePost메소드 호출
                    return new ReportResponse<>("true", "신고가 접수되었습니다. 신고 한도에 도달하여 게시글이 자동 삭제되었습니다."
                            , post.getTitle(),content,null);
                }
                return new ReportResponse<>("true", "신고가 접수되었습니다.", post.getTitle(),content,null);



                // Rest of your report logic...
            } else {
                return new ReportResponse<>("false", "해당 유저가 없습니다.", post.getTitle(),content,null);
            }
        } else {
            return new ReportResponse<>("false", "해당 게시글이 없습니다.", null,content,null);
        }

    }
    @Transactional
    public Object bookmarkPost(Long postId, Member member) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            if(favoriteRepository.findByPostAndMember(post, member).isPresent()){
                return "실패 : 이미 즐겨찾기에 추가된 게시글입니다.";
            }
            Favorite favorite = new Favorite(post, member);
            favoriteRepository.save(favorite);
            return "성공 : 즐겨찾기가 추가되었습니다.";


        }

        return "실패 : 해당 게시글이 없습니다.";
    }

    @Transactional
    public Object deleteBookmarkPost(Long postId, Member member) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            Optional<Favorite> favoriteOptional = favoriteRepository.findByPostAndMember(post, member);
            if (favoriteOptional.isPresent()) {
                Favorite favorite = favoriteOptional.get();
                favoriteRepository.delete(favorite);
                return "성공 : 즐겨찾기가 삭제되었습니다.";
            }
            return "실패 : 즐겨찾기에 추가되지 않은 게시글입니다.";
        }
        return "실패 : 해당 게시글이 없습니다.";
    }

    @JsonIgnore
    public List<PostDTO> getBookmarkPost(Member member) {
        List<Favorite> favorites = favoriteRepository.findByMember(member);
        List<PostDTO> postDTOList = new ArrayList<>();
        for (Favorite favorite : favorites) {
            Post post = favorite.getPost();
            PostDTO postDTO = new PostDTO();
            postDTO.setCreateDate(post.getCreateDate());
            postDTO.setLastModifiedDate(post.getLastModifiedDate());
            postDTO.setId(post.getPostId());
            postDTO.setTitle(post.getTitle());
            postDTO.setContent(post.getContent());
            postDTO.setImagePath(post.getImagePath());
            postDTO.setLikePost(post.getLikePost());
            postDTO.setHit(post.getHit());

            postDTO.setChannelId(post.getChannel().getChannelId());
            postDTO.setMemberId(post.getMember().getId());
            postDTO.setNickname(post.getMember().getNickname());
            postDTO.setChannelName(post.getChannel().getChannelName());
            postDTOList.add(postDTO);
        }
        return postDTOList;

    }


}
