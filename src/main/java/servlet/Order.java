package servlet;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
class Order {
    private String id;
    private String orderNumber;
    private ArrayList<OrderRow> orderRows;

}
