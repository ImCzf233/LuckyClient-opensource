package net.minecraft;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Target(ElementType.PACKAGE)
@Repeatable(SkidClient.L.class)
public @interface SkidClient {
    String value();
    @Target(ElementType.PACKAGE)
    @interface L{
        SkidClient[] value();
    }
}
