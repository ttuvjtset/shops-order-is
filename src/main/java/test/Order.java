package test;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@Setter
@ToString
//@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String  id;
    private String orderNumber;
    private ArrayList<OrderRow> orderRows;

}
