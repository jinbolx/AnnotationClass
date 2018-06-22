package com.jinbolx.ioc_processor;

import com.google.auto.service.AutoService;
import com.jinbolx.ioc_annotation.BindView;
import com.jinbolx.ioc_annotation.OnClick;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes({"com.jinbolx.ioc_annotation.BindView",
        "com.jinbolx.ioc_annotation.OnClick"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@AutoService(Processor.class)
public class InjectViewProcessor extends AbstractProcessor {

    private Elements elementsUtils;
    private Filer filer;
    private Map<TypeElement, Proxy> proxyHashMap = new HashMap<>();

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        proxyHashMap.clear();
        Set<? extends Element> bindElements = roundEnvironment
                .getElementsAnnotatedWith(BindView.class);
        Set<? extends Element> onClickElements = roundEnvironment
                .getElementsAnnotatedWith(OnClick.class);
        for (Element e :
                bindElements) {
            VariableElement ve = ((VariableElement) e);
            BindView bindView = ve.getAnnotation(BindView.class);
            int id = bindView.value();
            TypeElement te = ((TypeElement) ve.getEnclosingElement());
            Proxy proxy = proxyHashMap.get(te);
            if (proxy == null) {
                proxy = new Proxy(elementsUtils, te);
                proxyHashMap.put(te, proxy);
            }
            proxy.idMap.put(ve, id);
        }
        if (onClickElements.size() > 1) {
            try {
                throw new Exception("only one onClick annotation can be used in a Java class");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Element onClickElement = null;
            for (Element e :
                    onClickElements) {
                onClickElement = e;
            }
            if (onClickElement != null) {
                VariableElement ve = ((VariableElement) onClickElement);
                TypeElement te = ((TypeElement) ve.getEnclosingElement());
                OnClick onClickAnnotation = onClickElement.getAnnotation(OnClick.class);
                int[] ids = onClickAnnotation.value();
                Proxy proxy = proxyHashMap.get(te);
                if (proxy != null) {
                    for (int id : ids) {
                        proxy.getList().add(id);
                    }
                }
            }

        }
        for (TypeElement te :
                proxyHashMap.keySet()) {
            Proxy proxy = proxyHashMap.get(te);
            StringBuilder stringBuilder = new StringBuilder();
            try {
                JavaFileObject javaFileObject = filer.createSourceFile(proxy.getFileName(), te);
                Writer writer = javaFileObject.openWriter();
                writer.write(proxy.generateClass(stringBuilder));
                writer.flush();
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementsUtils = processingEnvironment.getElementUtils();
        filer = processingEnvironment.getFiler();
    }
}
