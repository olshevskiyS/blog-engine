package ru.olshevskiy.blogengine.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * MyStatisticsRs.
 *
 * @author Sergey Olshevskiy
 */
@Schema(description = "Данные статистики по постам текущего пользователя")
public class MyStatisticsRs extends StatisticsRs {
}