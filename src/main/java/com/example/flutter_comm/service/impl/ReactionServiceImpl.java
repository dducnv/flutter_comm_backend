package com.example.flutter_comm.service.impl;

import com.example.flutter_comm.dto.ReactionSeed;
import com.example.flutter_comm.dto.reaction.ReactionDto;
import com.example.flutter_comm.dto.reaction.ReactionGetDto;
import com.example.flutter_comm.dto.reaction.ReactionResDto;
import com.example.flutter_comm.dto.reaction.ReactionStatusResDto;
import com.example.flutter_comm.entity.*;
import com.example.flutter_comm.exception.ApiRequestException;
import com.example.flutter_comm.repository.PostRepository;
import com.example.flutter_comm.repository.ReactionPostRepository;
import com.example.flutter_comm.repository.ReactionRepository;
import com.example.flutter_comm.repository.UserReactionPostRepository;
import com.example.flutter_comm.service.ReactionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReactionServiceImpl implements ReactionService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    ReactionRepository reactionRepository;
    @Autowired
    ReactionPostRepository reactionPostRepository;
    @Autowired
    UserReactionPostRepository userReactionPostRepository;

    public List<ReactionSeed> reactionSeedList(){
        List<ReactionSeed> reactionSeed = new ArrayList<>();
        reactionSeed.add(new ReactionSeed(1,"like", "\uD83D\uDC4D", "https://github.githubassets.com/images/icons/emoji/unicode/1f44d.png"));
        reactionSeed.add(new ReactionSeed(2,"dislike", "\uD83D\uDC4E", "https://github.githubassets.com/images/icons/emoji/unicode/1f44e.png"));
        reactionSeed.add(new ReactionSeed(3,"love", "❤\uFE0F", "https://github.githubassets.com/images/icons/emoji/unicode/2764.png"));
        reactionSeed.add(new ReactionSeed(4,"congratulations", "\uD83C\uDF89", "https://github.githubassets.com/images/icons/emoji/unicode/1f389.png"));
        reactionSeed.add(new ReactionSeed(5,"haha", "\uD83D\uDE04", "https://github.githubassets.com/images/icons/emoji/unicode/1f604.png"));
        reactionSeed.add(new ReactionSeed(6,"eyes", "\uD83D\uDC40", "https://github.githubassets.com/images/icons/emoji/unicode/1f440.png"));
    return reactionSeed;
    }

    public ReactionResDto getReactionOfPostDto(User user, Post post){
        List<ReactionGetDto> reactionGetDtos = post.getReactionCounts().stream()
                .map(this::toReactionGetDto)
                .collect(Collectors.toList());
        List<ReactionGetDto> myReaction = post.getReactionCounts().stream().
                filter(it ->  it.getUserReactionPost().stream().anyMatch(uit -> uit.getUser().equals(user)))
                .map(this::toReactionGetDto).collect(Collectors.toList());
        int totalReaction = reactionGetDtos.stream().mapToInt(ReactionGetDto::getCount).sum();
        return  ReactionResDto.builder()
                .totalReaction(totalReaction)
                .myReactions(myReaction)
                .reactions(reactionGetDtos)
                .build();
    };
    @Override
    public ReactionStatusResDto addReactionToPost(Post post, ReactionDto reactionDto, User user) {
        //dto to entity
        List<ReactionSeed> reactionSeeds= reactionSeedList();
        Optional<ReactionSeed>  reactionExists = reactionSeeds.stream().filter(c -> c.getId() == reactionDto.getId()).findFirst();
        if(!reactionExists.isPresent()) throw new ApiRequestException("Emoji không tồn tại.");

        Reaction reaction = new Reaction();
        reaction.setId(reactionDto.getId());
        //check post is has reaction
        Optional<ReactionPostCount> reactionPostRepositoryOptional = Optional.ofNullable(reactionPostRepository.findFirstByPostAndReaction(post, reaction));
        if(!reactionPostRepositoryOptional.isPresent()){
            ReactionPostCount reactionPostCountSave = new ReactionPostCount();
            reactionPostCountSave.setReaction(reaction);
            reactionPostCountSave.setPost(post);
            ReactionPostCount reactionPostCount = reactionPostRepository.save(reactionPostCountSave);
            userReactionPost(user,reactionPostCount);
            return ReactionStatusResDto.builder()
                    .status("create")
                    .name(reactionExists.get().getName())
                    .build();
        }

        ReactionPostCount reactionPostCountGet =  reactionPostRepositoryOptional.get();
        UserReactionPost userReactionPost = new UserReactionPost();
        userReactionPost.setUser(user);
        userReactionPost.setReactionPostCount(reactionPostCountGet);
        //check xem người dùng đã sử dụng trạng thái trên request chưa
        Optional<UserReactionPost> userReactionPostOptional = userReactionPostRepository.findFirstByUserAndReactionPostCount(user,reactionPostCountGet);
        if(userReactionPostOptional.isPresent()){
            userReactionPostRepository.delete(userReactionPostOptional.get());
            if(userReactionPostRepository.countAllByReactionPostCount(reactionPostCountGet) == 0){
                reactionPostRepository.delete(reactionPostCountGet);
            }
            return ReactionStatusResDto.builder()
                    .status("destroy")
                    .name(reactionExists.get().getName())
                    .build();
        }
        userReactionPostRepository.save(userReactionPost);
        return ReactionStatusResDto.builder()
                .status("create")
                .name(reactionExists.get().getName())
                .build();
    }

    public void userReactionPost(User user, ReactionPostCount reactionPostCount){
        UserReactionPost userReactionPost = UserReactionPost.builder()
                .reactionPostCount(reactionPostCount)
                .user(user)
                .build();
        userReactionPostRepository.save(userReactionPost);
    }

    public ReactionGetDto toReactionGetDto(ReactionPostCount reactionPostCount){
        return ReactionGetDto.builder()
                .iconUrl(reactionPostCount.getReaction().getIconUrl())
                .name(reactionPostCount.getReaction().getName())
                .emoji(reactionPostCount.getReaction().getEmoji())
                .count(reactionPostCount.getUserReactionPost().size())
                .build();
    }

}
