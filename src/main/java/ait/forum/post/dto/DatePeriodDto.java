package ait.forum.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DatePeriodDto {
    LocalDate dateFrom;
    LocalDate dateTo;
}
