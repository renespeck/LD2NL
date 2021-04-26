package org.aksw.owl2nl;

import org.aksw.triple2nl.converter.IRIConverter;
import org.aksw.triple2nl.converter.LiteralConverter;
import org.aksw.triple2nl.converter.SimpleIRIConverter;
import org.aksw.triple2nl.nlp.stemming.PlingStemmer;
import org.aksw.triple2nl.property.PropertyVerbalization;
import org.aksw.triple2nl.property.PropertyVerbalizer;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectRestriction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simplenlg.features.Feature;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.english.Realiser;

public class OWLPropertyExpressionConverter implements OWLPropertyExpressionVisitorEx<NLGElement> {
    private static final Logger logger = LoggerFactory.getLogger(OWLClassExpressionConverter.class);

    NLGFactory nlgFactory;
    Realiser realiser;

    IRIConverter iriConverter = new SimpleIRIConverter();
    PropertyVerbalizer propertyVerbalizer = new PropertyVerbalizer(iriConverter, null);
    LiteralConverter literalConverter = new LiteralConverter(iriConverter);
    boolean noun;
    boolean verb;

    OWLObjectPropertyExpression root;

    private boolean isSubObjectPropertyExpression;
    private boolean isSubDataPropertyExpression;
    public OWLPropertyExpressionConverter(Lexicon lexicon) {
        nlgFactory = new NLGFactory(lexicon);
        realiser = new Realiser(lexicon);
    }

    public OWLPropertyExpressionConverter() {
        this(Lexicon.getDefaultLexicon());
    }


    public String convert(OWLPropertyExpression pe) {
        // process
        NLGElement nlgElement = asNLGElement(pe);

        // realise
        nlgElement = realiser.realise(nlgElement);

        return nlgElement.getRealisation();
    }

    public NLGElement asNLGElement(OWLPropertyExpression pe) {
        return asNLGElement(pe, false);
    }

    public NLGElement asNLGElement(OWLPropertyExpression pe, boolean isSubObjectPropertyExpression) {

        this.isSubObjectPropertyExpression = isSubObjectPropertyExpression;

        NLGElement nlgElement = pe.accept(this);

        return nlgElement;
    }



    private String getLexicalForm(OWLEntity entity) {
        return iriConverter.convert(entity.toStringID());
    }

    @NotNull
    @Override
    public NLGElement visit(@NotNull OWLObjectProperty pe) {
        SPhraseSpec phrase = nlgFactory.createClause();

        if (!pe.isAnonymous()) {
            PropertyVerbalization propertyVerbalization = propertyVerbalizer.verbalize(pe.getIRI().toString());
            String verbalizationText = propertyVerbalization.getVerbalizationText();
            if (propertyVerbalization.isNounType()) {
                NPPhraseSpec propertyNounPhrase = nlgFactory.createNounPhrase(PlingStemmer.stem(verbalizationText));

                phrase.setSubject("X");
                phrase.setVerb("is");
                phrase.setObject(verbalizationText);
                noun = true;
            } else if (propertyVerbalization.isVerbType()) {

                VPPhraseSpec verb = nlgFactory.createVerbPhrase(verbalizationText);
                phrase.setVerb(verb); // Issues with verbs like 'doneBy'
                phrase.setSubject("X");
                phrase.setObject("Y");
                noun = false;
            }
        }
        return phrase;
    }

    @NotNull
    @Override
    public NLGElement visit(@NotNull OWLObjectInverseOf owlObjectInverseOf) {
        return null;
    }

    @NotNull
    @Override
    //still working
    public NLGElement visit(@NotNull OWLDataProperty pe) {
        SPhraseSpec phrase = nlgFactory.createClause();
      //  OWLDataPropertyExpression property = pe.getProperty();
      //  OWLLiteral value = pe.getValue();
          OWLLiteral value1=null;
          OWLLiteral value2 = null;
        if(!pe.isAnonymous()) {
            PropertyVerbalization propertyVerbalization = propertyVerbalizer.verbalize(pe.asOWLDataProperty().getIRI().toString());
            String verbalizationText = propertyVerbalization.getVerbalizationText();
            if (propertyVerbalization.isNounType()) {
//				verbalizationText = PlingStemmer.stem(verbalizationText);

                phrase.setSubject("x");

                phrase.setVerb("is");

                NLGElement valueElement = nlgFactory.createNounPhrase(literalConverter.convert(value1));
                phrase.setObject(valueElement);


            } else if (propertyVerbalization.isVerbType()) {

                NLGElement valueElement = nlgFactory.createNounPhrase(literalConverter.convert(value2));
                phrase.setSubject("x");
                phrase.setObject(valueElement);
                phrase.setVerb(verbalizationText);


            }
        }

        logger.debug(pe +  " = " + realiser.realise(phrase));

        return phrase;

    }

    @NotNull
    @Override
    public NLGElement visit(@NotNull OWLAnnotationProperty owlAnnotationProperty) {
        return null;
    }
}
