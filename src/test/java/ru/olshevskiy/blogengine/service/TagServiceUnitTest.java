package ru.olshevskiy.blogengine.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.olshevskiy.blogengine.model.dto.TagDto;
import ru.olshevskiy.blogengine.model.dto.response.TagsByQueryRs;
import ru.olshevskiy.blogengine.model.entity.Post;
import ru.olshevskiy.blogengine.model.entity.Tag;
import ru.olshevskiy.blogengine.repository.PostRepository;
import ru.olshevskiy.blogengine.repository.TagRepository;

/**
 * TagServiceTest.
 *
 * @author Sergey Olshevskiy
 */
@ExtendWith(MockitoExtension.class)
public class TagServiceUnitTest {

  @InjectMocks
  private TagServiceImpl tagService;

  @Mock
  private TagRepository tagRepository;

  @Mock
  private PostRepository postRepository;

  @BeforeEach
  void setMockOutput() {
    List<Tag> tags = new ArrayList<>();
    Set<Post> posts1 = new HashSet<>() {{
        add(new Post());
        add(new Post());
        }};
    tags.add(new Tag("первый").setPosts(posts1));
    Set<Post> posts2 = new HashSet<>(posts1);
    posts2.add(new Post());
    tags.add(new Tag("второй").setPosts(posts2));
    Set<Post> posts3 = new HashSet<>(posts2);
    posts3.add(new Post());
    posts3.add(new Post());
    tags.add(new Tag("третий").setPosts(posts3));
    when(postRepository.getCountActivePosts()).thenReturn(10);
    when(tagRepository.findAll()).thenReturn(tags);
  }

  @Test
  void testGetTagsByQuery() {
    List<TagDto> dtoList = new ArrayList<>();
    dtoList.add(new TagDto("третий", "1,00"));
    dtoList.add(new TagDto("первый", "0,40"));
    TagsByQueryRs assignedTagsByQueryRs = new TagsByQueryRs().setTags(dtoList);
    TagsByQueryRs expectedTagsByQueryRs = tagService.getTagsByQuery("е");
    assertThat(expectedTagsByQueryRs).isEqualTo(assignedTagsByQueryRs);
  }
}
