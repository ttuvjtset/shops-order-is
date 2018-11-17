package model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_rows")
@Embeddable
public class OrderRow {
    @Column(name="item_name")
    String itemName;

    @NotNull
    @Min(1)
    @Column(name="quantity")
    int quantity;

    @NotNull
    @Min(1)
    @Column(name="price")
    int price;
}
