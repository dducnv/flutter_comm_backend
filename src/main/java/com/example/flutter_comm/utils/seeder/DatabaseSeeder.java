package com.example.flutter_comm.utils.seeder;

import com.example.flutter_comm.dto.ReactionSeed;
import com.example.flutter_comm.entity.*;
import com.example.flutter_comm.repository.CategoryRepository;
import com.example.flutter_comm.repository.ReactionRepository;
import com.example.flutter_comm.repository.RoleRepository;
import com.example.flutter_comm.repository.TagRepository;
import com.example.flutter_comm.service.impl.ReactionServiceImpl;
import com.example.flutter_comm.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DatabaseSeeder {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ReactionRepository reactionRepository;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    ReactionServiceImpl reactionService;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedRoleTable();
        seedUser();
        seedCategories();
        seedTags();
        seedEmoji();
    }

    private void seedRoleTable() {
        Optional<Role> roleU = Optional.ofNullable(roleRepository.findByRoleName("USER"));
        Optional<Role> roleA = Optional.ofNullable(roleRepository.findByRoleName("ADMIN"));
        Optional<Role> roleSA = Optional.ofNullable(roleRepository.findByRoleName("SUPER_ADMIN"));
        if (!roleSA.isPresent()) {
            Role role3 = new Role();
            role3.setRoleName("SUPER_ADMIN");
            roleRepository.save(role3);
        }
        if (!roleU.isPresent()) {
            Role role1 = new Role();
            role1.setRoleName("USER");
            roleRepository.save(role1);
        }
        if (!roleA.isPresent()) {
            Role role2 = new Role();
            role2.setRoleName("ADMIN");
            roleRepository.save(role2);
        }
    }

    private void seedTags(){
       User userOptional =userService.findUserByEmail("ducbe2k2@gmail.com");
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(new Tag("api","api",userOptional));
        tagSet.add(new Tag("widget","widget",userOptional));
        tagSet.add(new Tag("ListView","list-view",userOptional));
        tagSet.add(new Tag("bloc","bloc",userOptional));
        tagSet.add(new Tag("image","image",userOptional));

        tagSet.forEach(it->{
            boolean tag = tagRepository.existsBySlug(it.getSlug());
            if(!tag){
                tagRepository.save(it);
            }
        });
    }

    private void seedCategories(){
        Set<Category> categorySet = new HashSet<>();
        categorySet.add(new Category("Hỏi Đáp","hoi-dap"));
        categorySet.add(new Category("Bài Viết","bai-viet"));
        categorySet.add(new Category("Bàn Luận","ban-luan"));
        categorySet.add(new Category("Nghề Nhiệp","nghe-nhiep"));
        categorySet.forEach(it->{
            boolean categoryCheck = categoryRepository.existsBySlug(it.getSlug());
            if(!categoryCheck){
                categoryRepository.save(it);
            }
        });
    }

    private void seedUser() {
        Optional<User> userOptional = Optional.ofNullable(userService.findUserByEmail("ducbe2k2@gmail.com"));
        if(!userOptional.isPresent()){
            User user = new User();
            user.setEmail("ducbe2k2@gmail.com");
            user.setName("Nguyen Van Duc");
            userService.seedUserService(user);
        }

    }

    private void seedEmoji() {
        List<ReactionSeed> reactionSeed = reactionService.reactionSeedList();
        reactionSeed.forEach((it) -> {
            Optional<Reaction> reaction = Optional.ofNullable(reactionRepository.findFirstByName(it.getName()));
            if (!reaction.isPresent()) {
                Reaction reactionSave = new Reaction();
                reactionSave.setName(it.getName());
                reactionSave.setEmoji(it.getEmoji());
                reactionSave.setIconUrl(it.getUrl());
                reactionRepository.save(reactionSave);
            }
        });
    }


}
