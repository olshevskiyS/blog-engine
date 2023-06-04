package ru.olshevskiy.blogengine.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DislikePostRs.
 *
 * @author Sergey Olshevskiy
 */
@Schema(description = "Ответ сервера на запрос проставки дизлайка посту")
public class DislikePostRs extends PostVoteRs {
}
