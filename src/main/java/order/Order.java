package order;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String id;
    private String orderNumber;
    private ArrayList<OrderRow> orderRows;

}
