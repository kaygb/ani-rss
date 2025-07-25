package ani.rss.entity;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class WebHookHeader implements Serializable {
    private String key;
    private String value;
}
