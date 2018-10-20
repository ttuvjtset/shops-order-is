package test;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderRow {
    String itemName;
    int quantity;
    int price;
}
