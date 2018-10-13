package servlet;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
class OrderRow {
    String itemName;
    int quantity;
    int price;
}
