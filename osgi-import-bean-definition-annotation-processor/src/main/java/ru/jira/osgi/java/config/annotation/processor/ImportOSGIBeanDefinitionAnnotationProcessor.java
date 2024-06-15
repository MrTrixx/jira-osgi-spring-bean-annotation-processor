package ru.jira.osgi.java.config.annotation.processor;

import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import ru.jira.osgi.java.config.annotation.processor.dto.BeanDefinitionData;
import ru.jira.osgi.java.config.annotation.processor.dto.ConfigurationClazzData;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ImportOSGIBeanDefinitionAnnotationProcessor extends AbstractProcessor {
    private BeanDefinitionDataResolver beanDefinitionDataResolver;
    private SpringConfigurationImportOSGIBeanGenerator springConfigurationImportOSGIBeanGenerator;
    private TerminalPrinter terminalPrinter;


    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        terminalPrinter = new TerminalPrinter(env);
        beanDefinitionDataResolver = new BeanDefinitionDataResolver(terminalPrinter, new SameBeanDefinitionClassNameHolder());
        springConfigurationImportOSGIBeanGenerator = new SpringConfigurationImportOSGIBeanGenerator(terminalPrinter);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of("ru.jira.osgi.java.config.annotation.processor.annotation.GenerateOsgiImportConfigurationClass");
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_11;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            if (annotatedElements.isEmpty()) {
                continue;
            }
            processBeanDefinitions(annotatedElements);
            break;
        }

        springConfigurationImportOSGIBeanGenerator.infoUserAboutGeneratedClasses();
        return true;
    }

    private void processBeanDefinitions(final Set<? extends Element> annotatedElements) {
        for (Element annotatedElement : annotatedElements) {
            String annotatedClassName = ((TypeElement) annotatedElement).getQualifiedName().toString();
            List<? extends AnnotationMirror> annotationMirrors = annotatedElement.getAnnotationMirrors();
            List<BeanDefinitionData> beanDefinitionsData = new ArrayList<>();
            for (AnnotationMirror annotationMirror : annotationMirrors) {
                Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = annotationMirror.getElementValues();
                Collection<? extends AnnotationValue> values = elementValues.values();
                for (AnnotationValue value : values) {
                    List<? extends Attribute> comSunToolListWithClassesFromAnnotation = (List<? extends Attribute>) value.getValue();
                    for (Attribute annotationAttributeAsClassForCreateBeanDefinition : comSunToolListWithClassesFromAnnotation) {
                        Type classForCreateBeanDefinition = (Type) annotationAttributeAsClassForCreateBeanDefinition.getValue();
                        Symbol.TypeSymbol tsym = classForCreateBeanDefinition.tsym;
                        if (tsym instanceof Symbol.ClassSymbol) {
                            String classNameFromAnnotationClassesParam = ((Symbol.ClassSymbol) tsym).className();
                            beanDefinitionDataResolver.processStringClassNameToBeanDefinitionData(annotatedClassName, classNameFromAnnotationClassesParam).ifPresent(beanDefinitionsData::add);
                        }
                    }
                    try {
                        springConfigurationImportOSGIBeanGenerator.generateConfigurationClassWithBeanDefinitions(ConfigurationClazzData.of(annotatedClassName, beanDefinitionsData), processingEnv);
                    } catch (IOException e) {
                        terminalPrinter.printError(e);
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}