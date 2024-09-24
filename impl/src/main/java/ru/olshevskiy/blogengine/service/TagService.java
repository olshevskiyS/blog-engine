package ru.olshevskiy.blogengine.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.olshevskiy.blogengine.dto.TagDto;
import ru.olshevskiy.blogengine.dto.response.TagsByQueryRs;
import ru.olshevskiy.blogengine.model.Tag;
import ru.olshevskiy.blogengine.repository.PostRepository;
import ru.olshevskiy.blogengine.repository.TagRepository;

/**
 * TagService.
 *
 * @author Sergey Olshevskiy
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TagService {

  private final TagRepository tagRepository;
  private final PostRepository postRepository;

  /**
   * TagService. Getting tags by query method.
   */
  public TagsByQueryRs getTagsByQuery(String query) {
    log.info("Start request getTagsByQuery on query = {}", query);
    if (query == null) {
      query = "";
    }
    TagsByQueryRs tagsByQueryRs = new TagsByQueryRs();
    int totalAmountOfPosts = postRepository.getCountActivePosts();
    Map<String, Integer> listOfPostsByTags = getMapOfPostsByTagsDescendingSorted();
    float normalizationRatio = calculationNormalizationRatioForTags(
            listOfPostsByTags, totalAmountOfPosts);

    List<TagDto> listTagsWithNormalizedWeights = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("0.00");
    for (String tagName : listOfPostsByTags.keySet()) {
      if (tagName.toLowerCase().contains(query.toLowerCase())) {
        float tagWeights = (float) listOfPostsByTags.get(tagName) / totalAmountOfPosts;
        float tagNormalizedWeights = normalizationRatio * tagWeights;
        listTagsWithNormalizedWeights.add(new TagDto(tagName, df.format(tagNormalizedWeights)));
      }
    }
    tagsByQueryRs.setTags(listTagsWithNormalizedWeights);
    log.info("Finish request getTagsByQuery");
    return tagsByQueryRs;
  }

  private Map<String, Integer> getMapOfPostsByTagsDescendingSorted() {
    Map<String, Integer> countPostsByTagsMap = new HashMap<>();
    List<Tag> listTags = tagRepository.findAll();
    for (Tag tag : listTags) {
      countPostsByTagsMap.put(tag.getName(), tag.getPosts().size());
    }
    return countPostsByTagsMap.entrySet().stream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
  }

  private float calculationNormalizationRatioForTags(
          Map<String, Integer> listOfPostsByTags, int totalAmountOfPosts) {
    int amountPostsByTheMostPopularTag = listOfPostsByTags.entrySet().iterator().next().getValue();
    float weightOfTheMostPopularTag = (float) amountPostsByTheMostPopularTag / totalAmountOfPosts;
    return (1 / weightOfTheMostPopularTag);
  }
}
