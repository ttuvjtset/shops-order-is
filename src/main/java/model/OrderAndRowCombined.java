package model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderAndRowCombined {
    Long id;
    String orderNumber;
    String itemName;
    int quantity;
    int price;
}
