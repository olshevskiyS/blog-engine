package ru.olshevskiy.blogengine.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * LikePostRq.
 *
 * @author Sergey Olshevskiy
 */
@Schema(description = "Запрос на проставку лайка посту")
public class LikePostRq extends PostVoteRq {
}