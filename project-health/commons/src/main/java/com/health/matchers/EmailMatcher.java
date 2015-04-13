package com.health.matchers;

import java.util.regex.Pattern;

public class EmailMatcher {
    private Pattern pattern;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public EmailMatcher() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    /**
     * Validate email with regular expression
     *
     * @param email email address to validate
     * @return true valid hex, false invalid hex
     */
    public boolean validate(final String email) {
        return pattern.matcher(email).matches();
    }
}
