package com.example.flutter_comm.utils.seeder;

import com.example.flutter_comm.dto.ReactionSeed;
import com.example.flutter_comm.entity.*;
import com.example.flutter_comm.repository.*;
import com.example.flutter_comm.service.impl.CategoryServiceImpl;
import com.example.flutter_comm.service.impl.ReactionServiceImpl;
import com.example.flutter_comm.service.impl.TagServiceImpl;
import com.example.flutter_comm.service.impl.UserServiceImpl;
import com.example.flutter_comm.utils.Generating;
import com.example.flutter_comm.utils.SlugGenerating;
import com.github.javafaker.Faker;
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
    PostRepository postRepository;

    @Autowired
    ReactionRepository reactionRepository;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    ReactionServiceImpl reactionService;

    @Autowired
    TagRepository tagRepository;
    @Autowired
    TagServiceImpl tagService;
    @Autowired
    BlackWordRepository blackWordRepository;

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CategoryServiceImpl categoryService;
    final
    Faker faker;
    int numberOfPost = 3000;

    public DatabaseSeeder() {
        this.faker = new Faker(new Locale("vi"));
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
//        seedRoleTable();
//        seedUser();
//        seedCategories();
//        seedTags();
//        seedEmoji();
//        postSeeder();
//        seedBlackWord();
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

    private void seedTags() {
        User userOptional = userService.findUserByEmail("ducbe2k2@gmail.com");
        List<Tag> tagSeederList = new ArrayList<>();
        tagSeederList.add(Tag.builder()
                .name("widget")
                .slug("widget")
                .created_by(userOptional)
                .build());
tagSeederList.add(Tag.builder()
                .name("state")
                .slug("state")
                .created_by(userOptional)
                .build());
        tagSeederList.add(Tag.builder()
                .name("flutter")
                .slug("flutter")
                .created_by(userOptional)
                .build());
        tagSeederList.add(Tag.builder()
                .name("dart")
                .slug("dart")
                .created_by(userOptional)
                .build());
        tagSeederList.add(Tag.builder()
                .name("android")
                .slug("android")
                .created_by(userOptional)
                .build());
        tagSeederList.add(Tag.builder()
                .name("ios")
                .slug("ios")
                .created_by(userOptional)
                .build());
        tagSeederList.add(Tag.builder()
                .name("firebase")
                .slug("firebase")
                .created_by(userOptional)
                .build());
        tagSeederList.add(Tag.builder()
                .name("api")
                .slug("api")
                .created_by(userOptional)
                .build());
        tagSeederList.add(Tag.builder()
                .name("http")
                .slug("http")
                .created_by(userOptional)
                .build());
        tagSeederList.add(Tag.builder()
                .name("json")
                .slug("json")
                .created_by(userOptional)
                .build());
        tagSeederList.add(Tag.builder()
                .name("provider")
                .slug("provider")
                .created_by(userOptional)
                .build());
        tagSeederList.add(Tag.builder()
                .name("bloc")
                .slug("bloc")
                .created_by(userOptional)
                .build());
        tagSeederList.add(Tag.builder()
                .name("getx")
                .slug("getx")
                .created_by(userOptional)
                .build());
        tagSeederList.add(Tag.builder()
                .name("riverpod")
                .slug("riverpod")
                .created_by(userOptional)
                .build());
        tagSeederList.add(Tag.builder()
                .name("state management")
                .slug("state-management")
                .created_by(userOptional)
                .build());

        tagSeederList.forEach(it -> {
            boolean tag = tagRepository.existsBySlug(it.getSlug());
            if (!tag) {
                tagRepository.save(it);
            }
        });
    }

    private void seedCategories() {
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category("Hỏi Đáp", "hoi-dap"));
        categoryList.add(new Category("Bài Viết", "bai-viet"));
        categoryList.add(new Category("Thảo luận", "thao-luan"));
        categoryList.add(new Category("Nghề nghiệp", "nghe-nhiep"));

        categoryList.forEach(it -> {
            boolean categoryCheck = categoryRepository.existsBySlug(it.getSlug());
            if (!categoryCheck) {
                categoryRepository.save(it);
            }
        });
    }

    private void seedUser() {

        Optional<User> userOptional = Optional.ofNullable(userService.findUserByEmail("ducbe2k2@gmail.com"));
        if (!userOptional.isPresent()) {
            User user = new User();
            user.setEmail("ducbe2k2@gmail.com");
            user.setName("Nguyen Van Duc");
            userService.seedUserService(user);
        }


    }

    private void postSeeder() {
        User userOptional = userService.findUserByEmail("ducbe2k2@gmail.com");

        List<Post> postList = new ArrayList<>();
        for (int i = 0; i < numberOfPost; i++) {
            UUID uuid = UUID.randomUUID();
            Random random = new Random();
            List<Tag> randomTags = new ArrayList<>(tagService.tagSeedList(userOptional));
            Collections.shuffle(randomTags);
            randomTags = randomTags.subList(0, 4);
            Set<Tag> setTag = new HashSet<>(randomTags);
            String title = faker.name().title();
            String slug = SlugGenerating.toSlug(title.concat("-" + String.valueOf(Generating.generatePassword(4, false))));
            Post post = new Post();
            post.setTitle(title);
            post.setSlug(slug);
            post.setUuid(uuid);
            post.setTags(setTag);
            post.setCategory(categoryService.categorySeedList().get(random.nextInt(3)));
            post.setAuthor(userOptional);
            post.setContent(faker.lorem().paragraph(random.nextInt(20) + 70));
            post.setDescription(faker.lorem().paragraph(10));
            post.setStatus("ACTIVE");
            postList.add(post);
        }
        postRepository.saveAll(postList);
    }

    private void seedBlackWord() {
        List<BlackWord> blackWordList = new ArrayList<>();
        blackWordList.add(new BlackWord("Chủ Tịch Hồ Chí Minh"));
        blackWordList.add(new BlackWord("Chu Tich Ho Chi Minh"));
        blackWordList.add(new BlackWord("Võ Nguyên Giáp"));
        blackWordList.add(new BlackWord("Đại Tướng Võ Nguyên Giáp"));
        blackWordList.add(new BlackWord("Đảng Cộng Sản"));
        blackWordList.add(new BlackWord("Chủ Tịch Nước"));
        blackWordList.add(new BlackWord("Máu"));
        blackWordList.add(new BlackWord("Đánh nhau"));
        blackWordList.add(new BlackWord("Cộng Hoà"));
        blackWordList.add(new BlackWord("Đảng Cộng Hoà"));
        blackWordList.add(new BlackWord("Chính Phủ"));
        blackWordList.add(new BlackWord("Cộng Sản"));
        blackWordList.add(new BlackWord("Đảng Cộng Sản"));
        blackWordList.add(new BlackWord("Việt Nam Cộng Hoà"));
        blackWordList.add(new BlackWord("Việt Nam Cộng Sản"));
        blackWordList.add(new BlackWord("Chính Trị"));
        blackWordList.add(new BlackWord("Chủ Tịch Nước"));
        blackWordList.add(new BlackWord("Chiến Tranh"));
        blackWordList.add(new BlackWord("Phản Động"));
        blackWordList.add(new BlackWord("Ba que"));
        blackWordList.add(new BlackWord("Bọn Ba Sọc"));
        blackWordList.add(new BlackWord("đồ con chó"));
        blackWordList.add(new BlackWord("thằng ml"));
        blackWordList.add(new BlackWord("thằng chó"));
        blackWordList.add(new BlackWord("thằng ngu"));
        blackWordList.add(new BlackWord("thằng"));
        blackWordList.add(new BlackWord("con lồn"));
        blackWordList.add(new BlackWord("Lồn"));
        blackWordList.add(new BlackWord("fuck"));
        blackWordList.add(new BlackWord("fuck you"));
        blackWordList.add(new BlackWord("buồi"));
        blackWordList.add(new BlackWord("cặc"));
        blackWordList.add(new BlackWord("đầu buồi"));
        blackWordList.add(new BlackWord("dau buoi"));
        blackWordList.add(new BlackWord("đầu cặc"));
        blackWordList.add(new BlackWord("dau cac"));
        blackWordList.add(new BlackWord("đầu lồn"));
        blackWordList.add(new BlackWord("ngu vãi"));
        blackWordList.add(new BlackWord("ngu vãi đái"));
        blackWordList.add(new BlackWord("Tình dục"));
        blackWordList.add(new BlackWord("đĩ"));
        blackWordList.add(new BlackWord("điếm"));
        blackWordList.add(new BlackWord("ngu xuẩn"));
        blackWordList.add(new BlackWord("thằng đần"));
        blackWordList.add(new BlackWord("thằng mặt lon"));
        blackWordList.add(new BlackWord("thằng mặt lồn"));
        blackWordList.add(new BlackWord("thang mat lon"));
        blackWordList.add(new BlackWord("Bọn Cộng Hản"));
        blackWordList.add(new BlackWord("Bọn Cộng Hoà"));
        blackWordRepository.saveAll(blackWordList);
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
