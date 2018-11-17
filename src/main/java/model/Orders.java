package model;


import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq")
    @SequenceGenerator(name = "my_seq", sequenceName = "order_sequence", allocationSize = 1)
    long id;

    @Column(name="order_number")
    @Size(min = 2)
    String orderNumber;

    public Orders(String orderNumber) {
        this.orderNumber = orderNumber;
    }

//    @ElementCollection(fetch = FetchType.EAGER)

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "order_rows",
            joinColumns = @JoinColumn(name = "orders_id",
                    referencedColumnName = "id")
    )
    private List<OrderRow> orderRows = new ArrayList<>();
//    @Valid
//    ArrayList<OrderRow> orderRows;

}
