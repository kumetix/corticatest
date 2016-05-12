package kmt.corticatest.transformers;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by assafe on 5/12/2016.
 */
@ConfigurationProperties(prefix = "kmt.corticatest")
@Component
public class TransformersList {
    private List<String> transformers = new ArrayList<>();
    public List<String> getTransformers() { return transformers;}
}
