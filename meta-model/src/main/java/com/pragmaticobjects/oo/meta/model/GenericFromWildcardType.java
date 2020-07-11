package com.pragmaticobjects.oo.meta.model;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;
import java.util.Collection;
import java.util.Objects;

public class GenericFromWildcardType extends GenericInferred {
    public GenericFromWildcardType(WildcardType wildcard) {
        super(
            new Inference<Generic>() {
                @Override
                public final Generic inferredInstance() {
                    TypeMirror extendsBound = wildcard.getExtendsBound();
                    TypeMirror superBound = wildcard.getSuperBound();
                    if(Objects.nonNull(extendsBound)) {
                        return new GenericWildcard(GenericBoundary.EXTENDS, new GenericFromTypeMirror(extendsBound));
                    }
                    if(Objects.nonNull(superBound)) {
                        return new GenericWildcard(GenericBoundary.SUPER, new GenericFromTypeMirror(superBound));
                    }
                    throw new RuntimeException("Unexpected wildcard faced: " + wildcard.toString());
                }
            }
        );
    }
}
