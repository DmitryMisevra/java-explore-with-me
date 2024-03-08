package ru.yandex.practicum.mainservice.comment.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


/**
 * CreatedCommentDto передается при создании/обновлении Comment
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatedCommentDto {

    @NotBlank(message = "Комментарий не может быть пустым")
    @Size(max = 7000, message = "Размер комментария должен быть  не более 7000 символов")
    private String text;
}
