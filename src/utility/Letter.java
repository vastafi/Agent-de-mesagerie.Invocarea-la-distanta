package utility;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Letter {
    private String name;
    private String message;
    private boolean sent;

    public Letter(String name, String message) {
        this.name = name;
        this.message = message;
    }
}