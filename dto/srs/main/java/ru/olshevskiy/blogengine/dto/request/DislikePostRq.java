package ru.olshevskiy.blogengine.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DislikePostRq.
 *
 * @author Sergey Olshevskiy
 */
@Schema(description = "Запрос на проставку дизлайка посту")
public class DislikePostRq extends PostVoteRq {
}
