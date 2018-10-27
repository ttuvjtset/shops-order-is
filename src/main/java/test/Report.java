package test;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    int count;
    int averageOrderAmount;
    int turnoverWithoutVAT;
    int turnoverVAT;
    int turnoverWithVAT;
}
