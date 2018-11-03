package model;


import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    String id;

    @Size(min = 2)
    String orderNumber;

    @Valid
    ArrayList<OrderRow> orderRows;

}
