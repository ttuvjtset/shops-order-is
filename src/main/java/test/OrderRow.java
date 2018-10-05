package test;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderRow {
    String itemName;
    int quantity;
    int price;
}
