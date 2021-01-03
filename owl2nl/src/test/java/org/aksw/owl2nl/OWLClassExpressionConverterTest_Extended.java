/*
 * #%L
 * OWL2NL
 * %%
 * Copyright (C) 2015 Agile Knowledge Engineering and Semantic Web (AKSW)
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
/**
 * 
 */
package org.aksw.owl2nl;


import org.apache.jena.base.Sys;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticweb.owlapi.dlsyntax.renderer.DLSyntaxObjectRenderer;
import org.semanticweb.owlapi.io.ToStringRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import javax.xml.soap.SOAPPart;


/**
 * @author Lorenz Buehmann
 *
 */
public class OWLClassExpressionConverterTest_Extended {

	private static OWLClassExpressionConverter converter;

	private static OWLClass place;
	private static OWLClass company;
	private static OWLClass person;
	private static OWLClass softwareCompany;
	private static OWLDataFactoryImpl df;
	private static OWLNamedIndividual paderborn;
	private static OWLNamedIndividual karaoke;
	private static OWLNamedIndividual Jazz;
	private static OWLNamedIndividual Cricket;
	private static OWLNamedIndividual football;
	private static OWLNamedIndividual hockey;

	private static OWLObjectProperty worksFor;
	private static OWLObjectProperty ledBy;
	private static OWLDataProperty amountOfSalary;
    private static OWLObjectProperty sings;
	private static OWLObjectProperty plays;
	private static OWLObjectProperty workPlace;
	private static OWLLiteral salary;

	OWLClassExpression ce;
	String text;

	/**
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		converter = new OWLClassExpressionConverter();

		df = new OWLDataFactoryImpl();
		PrefixManager pm = new DefaultPrefixManager("http://dbpedia.org/ontology/");


		place = df.getOWLClass("Place", pm);
		worksFor = df.getOWLObjectProperty("worksFor", pm);
		ledBy = df.getOWLObjectProperty("isLedBy", pm);
		sings = df.getOWLObjectProperty("sing",pm);
		plays = df.getOWLObjectProperty("play",pm);
		company = df.getOWLClass("Company", pm);
		person = df.getOWLClass("Person", pm);
		softwareCompany=df.getOWLClass("SoftwareCompany",pm);
		salary=df.getOWLLiteral(40000);
		amountOfSalary = df.getOWLDataProperty("amountOfSalary", pm);

		workPlace = df.getOWLObjectProperty("workPlace", pm);
		paderborn = df.getOWLNamedIndividual("Paderborn", pm);
		karaoke=df.getOWLNamedIndividual("karaoke",pm);
		Jazz=df.getOWLNamedIndividual("jazz",pm);
        football=df.getOWLNamedIndividual("football",pm);
        Cricket=df.getOWLNamedIndividual("cricket",pm);
        hockey=df.getOWLNamedIndividual("hockey",pm);

		ToStringRenderer.getInstance().setRenderer(new DLSyntaxObjectRenderer());
	}

	@Test
	public void testhasValue() {
		// work place is a place and is named paderborn
		ce = df.getOWLObjectHasValue(workPlace, paderborn);
		text = converter.convert(ce);
		System.out.println(ce + " = " + text);
	}
	@Test
	public void testNested1() {
		/*someone who works for at least 5 companies that is ledby a company or a person*/
		ce = df.getOWLObjectMinCardinality(5, worksFor,
				df.getOWLObjectIntersectionOf(company, df.getOWLObjectSomeValuesFrom(ledBy, df.getOWLObjectUnionOf(company,person))));
		text = converter.convert(ce);
		System.out.println(ce + " = " + text);
		//String expected = "someone who works for at least 5 companies that is ledby a company or a person";
		//Assert.assertEquals(expected, text);

		/*someone who works for at least one company which is led by a company or a person*/
		ce = df.getOWLObjectMinCardinality(1, worksFor,
				df.getOWLObjectIntersectionOf(company, df.getOWLObjectSomeValuesFrom(ledBy, df.getOWLObjectUnionOf(company,person))));
		text = converter.convert(ce);
		System.out.println(ce + " = " + text);



	}
	@Test
	public void simpleNegation(){
		/*someone who does not work for a person or a company*/
		ce = df.getOWLObjectComplementOf(df.getOWLObjectSomeValuesFrom(worksFor, df.getOWLObjectUnionOf(person,company)));
		text = converter.convert(ce);
		System.out.println(ce + " = " + text);
		System.out.println("Expected : someone who does not work for a person or a company");

		//ce = df.getOWLObjectHasValue(df.getOWLObjectComplementOf(workPlace), paderborn);
		//text = converter.convert(ce);
		//System.out.println(ce + " = " + text);
	}
	@Test
	public void testNested1WithNegation() {

		/*someone who works for at least 5 companies which are not software companies and are ledby a company or a person*/
		ce = df.getOWLObjectMinCardinality(5, worksFor,
				df.getOWLObjectIntersectionOf(company, df.getOWLObjectSomeValuesFrom(ledBy, df.getOWLObjectUnionOf(company,person)),df.getOWLObjectComplementOf(softwareCompany)));
		text = converter.convert(ce);
		System.out.println(ce + " = " + text);
		System.out.println("Expected: someone who works for at least 5 companies which are not software companies and are ledby a company or a person");

		/*someone who works for at least one company which is not a software company and led by a company or a person*/
		ce = df.getOWLObjectMinCardinality(1, worksFor,
				df.getOWLObjectIntersectionOf(company, df.getOWLObjectSomeValuesFrom(ledBy, df.getOWLObjectUnionOf(company,person)), df.getOWLObjectComplementOf(softwareCompany)));
		text = converter.convert(ce);
		System.out.println(ce + " = " + text);
		System.out.println("Expected: someone who works for at least one company which is not a software company and led by a company or a person");


	}
	@Test
	public void testDataHasValueAndObjectHasValue() {
		// works for a company
		/*someone whose work place is paderborn and whose amount of salary is 40000*/
		ce = df.getOWLObjectIntersectionOf(df.getOWLDataHasValue(amountOfSalary, salary),df.getOWLObjectHasValue(workPlace, paderborn));
		text = converter.convert(ce);
		System.out.println(ce + " = " + text);
	}

	@Test
	public void NestedtesthasValue() {
		// work place is a place and is named paderborn
		ce = df.getOWLObjectSomeValuesFrom(workPlace,
				df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(workPlace, paderborn)));
		text = converter.convert(ce);
		System.out.println(ce + " = " + text);

		String expected = "something that works place something that works place Paderborn";
		Assert.assertEquals(expected, text);
		//LOG.info(ce + " = " + text);
	}
	@Test
	public void testComplex(){



		// a person that sings karaoke
		ce=df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings,karaoke),person);
		text= converter.convert(ce);
		System.out.println(ce + "=" + text);



        // a person that sings karaoke or a person that sings  jazz
		ce=df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings,karaoke),person),df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings,Jazz),person));
		text= converter.convert(ce);
		System.out.println(ce + "=" + text);



        // a person that sings karaoke and a person that sings jazz
		ce=df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings,karaoke),person),df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings,Jazz),person));
		text= converter.convert(ce);
		System.out.println(ce + "=" + text);


        // a person that plays Cricket or a person that plays football or a person that plays hockey.
        ce=ce=df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(plays,Cricket),person),df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(plays,football),person),df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(plays,hockey),person));
		text= converter.convert(ce);
		System.out.println(ce + "=" + text);



	}











}



