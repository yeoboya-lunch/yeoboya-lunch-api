package com.yeoboya.lunch.config.security.validation;

import javax.validation.groups.Default;

public class ValidationGroups {


    public interface PatternCheckGroup {
    }



    public interface EmailCheckGroup {
    }



    public interface KnowOldPassword extends Default {
    }


}