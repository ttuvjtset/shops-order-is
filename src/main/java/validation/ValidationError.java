package validation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ValidationError {
    private String code;
    private List<String> arguments;
}
