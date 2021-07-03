package org.aksw.owl2nl;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticweb.owlapi.dlsyntax.renderer.DLSyntaxObjectRenderer;
import org.semanticweb.owlapi.io.ToStringRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

public class OptimizerDepParseTest {
    private static OWLClassExpressionConverter converter;
    private static OWLDataFactoryImpl df;

    private static OWLClass company;
    private static OWLClass man;
    private static OWLClass softwareCompany;

    private static OWLNamedIndividual paderborn;
    private static OWLNamedIndividual karaoke;
    private static OWLNamedIndividual jazz;
    private static OWLNamedIndividual cricket;
    private static OWLNamedIndividual football;
    private static OWLNamedIndividual hockey;
    private static OWLNamedIndividual tennis;
    private static OWLNamedIndividual golf;
    private static OWLNamedIndividual hiphop;
    private static OWLNamedIndividual rock;

    private static OWLObjectProperty worksFor;
    private static OWLObjectProperty ledBy;
    private static OWLDataProperty amountOfSalary;
    private static OWLObjectProperty sings;
    private static OWLObjectProperty plays;
    private static OWLObjectProperty workPlace;
    private static OWLObjectProperty birthPlace;
    private static OWLLiteral salary;
    private static OWLDataProperty nrOfInhabitants;
    private static OWLDataRange dataRange;

    OWLClassExpression ce, ce1;

    String text;
    /**
     * @throws Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        converter = new OWLClassExpressionConverter();

        df = new OWLDataFactoryImpl();
        PrefixManager pm = new DefaultPrefixManager("http://dbpedia.org/ontology/");

//        worksFor = df.getOWLObjectProperty("worksFor", pm);
//        ledBy = df.getOWLObjectProperty("isLedBy", pm);
        sings = df.getOWLObjectProperty("sing", pm);
        plays = df.getOWLObjectProperty("play", pm);
//        company = df.getOWLClass("Company", pm);
        man = df.getOWLClass("Man", pm);
//        softwareCompany = df.getOWLClass("SoftwareCompany", pm);
//        salary = df.getOWLLiteral(40000);
//        amountOfSalary = df.getOWLDataProperty("amountOfSalary", pm);
//        birthPlace = df.getOWLObjectProperty("birthPlace", pm);
//        worksFor = df.getOWLObjectProperty("worksFor", pm);
//        ledBy = df.getOWLObjectProperty("isLedBy", pm);

//        workPlace = df.getOWLObjectProperty("workPlace", pm);
//        paderborn = df.getOWLNamedIndividual("Paderborn", pm);
        karaoke = df.getOWLNamedIndividual("karaoke", pm);
        jazz = df.getOWLNamedIndividual("jazz", pm);
        football = df.getOWLNamedIndividual("football", pm);
        cricket = df.getOWLNamedIndividual("cricket", pm);
        hockey = df.getOWLNamedIndividual("hockey", pm);
        tennis= df.getOWLNamedIndividual("tennis", pm);
        golf= df.getOWLNamedIndividual("golf", pm);
        hiphop= df.getOWLNamedIndividual("hiphop", pm);
        rock=df.getOWLNamedIndividual("rock", pm);


//        nrOfInhabitants = df.getOWLDataProperty("nrOfInhabitants", pm);
//        dataRange = df.getOWLDatatypeMinInclusiveRestriction(10000000);

        ToStringRenderer.getInstance().setRenderer(new DLSyntaxObjectRenderer());
    }

    @Test
    public void testDisjunctionSSSVDO() {
        ce = df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, karaoke), man),
                df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, jazz), man));
        text = converter.convert(ce);
        Assert.assertEquals("(Man ⊓ (∃ sing.{jazz})) ⊔ (Man ⊓ (∃ sing.{karaoke}))", ce.toString());
        Assert.assertEquals("a man that sings jazz or karaoke", text);
    }

    @Test
    public void testConjunctionSSSVDO() {
        ce = df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, karaoke),
                df.getOWLObjectHasValue(sings, jazz), man);
        text = converter.convert(ce);
        Assert.assertEquals("Man ⊓ (∃ sing.{jazz}) ⊓ (∃ sing.{karaoke})", ce.toString());
        Assert.assertEquals("a man that sings jazz and karaoke", text);
    }

    @Test
    public void testConjunctionSSSVDO2() {
        ce = df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, karaoke), man),
                df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, jazz), man));
        text = converter.convert(ce);
        Assert.assertEquals("(Man ⊓ (∃ sing.{jazz})) ⊓ (Man ⊓ (∃ sing.{karaoke}))", ce.toString());
        Assert.assertEquals("a man that sings jazz and karaoke", text);
    }

    @Test
    public void testConjunctionMultipleSSSVDO() {
        ce = df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, karaoke),
                df.getOWLObjectHasValue(sings, jazz),
                df.getOWLObjectHasValue(sings, rock), man);
        text = converter.convert(ce);
        Assert.assertEquals("Man ⊓ (∃ sing.{jazz}) ⊓ (∃ sing.{karaoke}) ⊓ (∃ sing.{rock})", ce.toString());
        Assert.assertEquals("a man that sings jazz, karaoke and rock", text);
    }

    @Test
    public void testConjunctionMultipleSSSVDO2() {
        ce = df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, karaoke), man),
                df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, jazz), man),
                df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, rock), man));
        text = converter.convert(ce);
        Assert.assertEquals("(Man ⊓ (∃ sing.{jazz})) ⊓ (Man ⊓ (∃ sing.{karaoke}))" +
                " ⊓ (Man ⊓ (∃ sing.{rock}))", ce.toString());
        Assert.assertEquals("a man that sings jazz, karaoke and rock", text);
    }

    @Test
    public void testDisjunctionMultipleSSSVDO() {
        ce = df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, karaoke), man),
                df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, jazz), man),
                df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, rock), man));
        text = converter.convert(ce);
        Assert.assertEquals("(Man ⊓ (∃ sing.{jazz})) ⊔ (Man ⊓ (∃ sing.{karaoke}))" +
                " ⊔ (Man ⊓ (∃ sing.{rock}))", ce.toString());
        Assert.assertEquals("a man that sings jazz, karaoke or rock", text);
    }

    @Test
    public void testDisjunctionMultipleSSSVDO2() {
        ce = df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, karaoke), man),
                df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, jazz), man),
                        df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, rock), man)));
        text = converter.convert(ce);
        Assert.assertEquals("(Man ⊓ (∃ sing.{karaoke})) ⊔ ((Man ⊓ (∃ sing.{jazz}))" +
                " ⊔ (Man ⊓ (∃ sing.{rock})))", ce.toString());
        Assert.assertEquals("a man that sings karaoke or jazz or rock", text);
    }

    @Test
    public void testCombinationMultipleSSSVDO() {
        ce = df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, karaoke), man),
                df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, jazz),
                        df.getOWLObjectHasValue(sings, rock), man));
        text = converter.convert(ce);
        Assert.assertEquals("(Man ⊓ (∃ sing.{jazz}) ⊓ (∃ sing.{rock}))" +
                " ⊔ (Man ⊓ (∃ sing.{karaoke}))", ce.toString());
        Assert.assertEquals("a man that sings jazz and rock or karaoke", text);
    }

    @Test
    public void testCombinationMultipleSSSVDO2() {
        ce1 = df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, karaoke),
                man), df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, jazz), man));
        ce = df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, rock),
                man), ce1);
        text = converter.convert(ce);
        Assert.assertEquals("(Man ⊓ (∃ sing.{rock})) ⊓ ((Man ⊓ (∃ sing.{jazz}))" +
                " ⊔ (Man ⊓ (∃ sing.{karaoke})))", ce.toString());
        Assert.assertEquals("a man that sings rock and jazz or karaoke", text);
    }
    @Test
    public void testConjunctionMultipleSSDVDO() {
        ce = df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(plays, cricket),
                df.getOWLObjectHasValue(sings, jazz),
                df.getOWLObjectHasValue(sings, rock), man);
        text = converter.convert(ce);
        Assert.assertEquals("Man ⊓ (∃ play.{cricket}) ⊓ (∃ sing.{jazz})" +
                " ⊓ (∃ sing.{rock})", ce.toString());
        Assert.assertEquals("a man that plays cricket, sings jazz and sings rock", text);
    }

    @Test
    public void testCombinationMultipleSSDVDO() {
        ce1 = df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings,
                karaoke), man), df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, jazz), man));
        ce = df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(plays, cricket),
                man), ce1);
        text = converter.convert(ce);
        Assert.assertEquals("(Man ⊓ (∃ play.{cricket})) ⊔ ((Man ⊓ (∃ sing.{jazz}))" +
                " ⊓ (Man ⊓ (∃ sing.{karaoke})))", ce.toString());
        Assert.assertEquals("a man that plays cricket or sings jazz and sings karaoke", text);

    }
}
