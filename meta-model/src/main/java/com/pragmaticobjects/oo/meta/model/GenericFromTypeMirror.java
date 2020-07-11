package com.pragmaticobjects.oo.meta.model;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;

public class GenericFromTypeMirror extends GenericInferred{
    public GenericFromTypeMirror(TypeMirror typeMirror) {
        super(
            new Inference<Generic>() {
                @Override
                public final Generic inferredInstance() {
                    if(typeMirror instanceof WildcardType) {
                        return new GenericFromWildcardType((WildcardType) typeMirror);
                    }
                    if(typeMirror instanceof TypeVariable) {
                        return new GenericFromTypeVariable((TypeVariable) typeMirror);
                    }
                    return new GenericType(
                        new TypeFromTypeMirror(typeMirror)
                    );
                }
            }
        );
    }
}
