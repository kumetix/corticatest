package kmt.corticatest.transformers;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by assafe on 5/12/2016.
 */
@Component
public class ImageTransformationChain implements ImageTransformer, ApplicationContextAware{

    private final List<String> imageTransformersClasses;

    @Autowired
    public ImageTransformationChain(TransformersList transformersList) {
        imageTransformersClasses = transformersList.getTransformers();
    }

    private List<ImageTransformer> transformersInstances;
    private ApplicationContext applicationContext;

    @PostConstruct
    protected void postConstruct() {
        transformersInstances = imageTransformersClasses.stream().map(this::getTransformerInstance).collect(Collectors.toList());
    }

    @Override
    public ImageTransformation transform(ImageTransformation image) {
        ImageTransformation lastImage = image;
        for (ImageTransformer curr : transformersInstances) {
            lastImage = curr.transform(lastImage);
        }
        return lastImage;
    }

    // ----------------------------------- Exceptions ----------------------------------------------------------------
    public static class InvalidImageTransformer extends IllegalArgumentException {
        public InvalidImageTransformer(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // --------------------------------------- private ---------------------------------------------------------------

    private ImageTransformer getTransformerInstance(String className) {
        try {
            return (ImageTransformer) applicationContext.getBean(Class.forName(className));
        } catch (ClassNotFoundException e) {
            throw new InvalidImageTransformer("failed to instantiate " + className, e);
        }
    }

    private static void closeQuietly(InputStream is) {
        try {
            is.close();
        } catch (IOException e) {
            // do nothing
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
