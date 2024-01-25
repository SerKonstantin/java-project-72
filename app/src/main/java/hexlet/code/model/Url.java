package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Url {
    private Long id;
    private String name;
    private String createdAt;

    public Url(String name) {
        this.name = name;
    }
}
