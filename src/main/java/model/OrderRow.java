package model;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderRow {
    String itemName;

    @NotNull
    @Min(0)
    int quantity;

    @NotNull
    @Min(0)
    int price;
}
