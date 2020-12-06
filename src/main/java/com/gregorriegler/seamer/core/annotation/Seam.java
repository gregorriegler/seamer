package com.gregorriegler.seamer.core.annotation;

import com.gregorriegler.seamer.file.FileLocation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Seam {
    String value() default "seam";
    String basePath() default FileLocation.DEFAULT_BASE_PATH;
}