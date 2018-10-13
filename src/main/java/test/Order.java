package test;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public class Order {
    private String id;
    private String orderNumber;
    private ArrayList<OrderRow> orderRows;

}
