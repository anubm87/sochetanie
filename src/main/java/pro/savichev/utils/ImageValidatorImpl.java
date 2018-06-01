package pro.savichev.utils;


import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ImageValidatorImpl implements ImageValidator {

    private static final String IMAGE_PATTERN =
            "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";

    private Pattern pattern;

    public ImageValidatorImpl() {
        pattern = Pattern.compile(IMAGE_PATTERN);
    }

    @Override
    public Boolean validate(String fileName) {
        Matcher matcher = pattern.matcher(fileName);
        return matcher.matches();
    }
}
