package ru.olshevskiy.blogengine.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * LikePostRs.
 *
 * @author Sergey Olshevskiy
 */
@Schema(description = "Ответ сервера на запрос проставки лайка посту")
public class LikePostRs extends PostVoteRs {
}