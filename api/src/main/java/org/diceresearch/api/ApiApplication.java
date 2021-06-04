package org.diceresearch.api;

import com.google.gson.Gson;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.dlsyntax.parser.DLSyntaxOWLParser;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.mansyntax.ManchesterOWLSyntaxParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.aksw.owl2nl.OWLAxiomConverter;
import org.semanticweb.owlapi.dlsyntax.parser.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@GetMapping("/hello")
	public String sayHello(@RequestParam(value = "myName", defaultValue = "from OwlAPI") String name) {
		return String.format("Hello %s!", name);
	}

	@GetMapping("/getOntology")
	public String GetOntology(@RequestParam(value = "path", defaultValue = "http://www.cs.man.ac.uk/~stevensr/ontology/family.rdf.owl") String path) {
		String response;
		try {
			if (path.isEmpty())
				path = "http://www.cs.man.ac.uk/~stevensr/ontology/family.rdf.owl";
			path = URLDecoder.decode(path, StandardCharsets.UTF_8.toString());
			OWLAxiomConverter converter = new OWLAxiomConverter();
			Map<String, String> json = converter.readOntology(path);
			Gson gson = new Gson();
			response = gson.toJson(json);
			System.out.println(String.format("reading from %s \n Ontologies :\n %s", path, response));
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return String.format("Internal Error. Please try later! \n \n %s", e.getMessage());
		}
	}

	@PostMapping ("/convertClassExpression")
	public String convertExpression(@RequestBody uiModel model){
		String response;
		try{
//			if (model.path.isEmpty())
//				throw new InvalidArgumentException({"path cannot be Empty"});
//			if( model.expression.isEmpty()) {
//				throw new InvalidArgumentException({"expression cannot be Empty"});
//			}
			//DLSyntaxParser parser = new DLSyntaxParser();
			return  " Still in progress !";

		}
		catch( Exception ex){
			ex.printStackTrace();
			return String.format("Internal Error. Please try later! \n \n %s", ex.getMessage());
		}
	}
//	private OWLClassExpression convertStringToClassExpression(String expression) {
//		ManchesterOWLSyntaxParser parser = OWLManager.createManchesterParser();
//		parser.setStringToParse(expression);
//		parser.setDefaultOntology(owlOntology); // my ontology
//		ShortFormEntityChecker checker = new ShortFormEntityChecker(getShortFormProvider());
//		parser.setOWLEntityChecker(checker);
//		return parser.parseClassExpression();
//	}
//	private BidirectionalShortFormProvider getShortFormProvider() {
//		Set<OWLOntology> ontologies = owlManager.getOntologies(); // my OWLOntologyManager
//		ShortFormProvider sfp = new ManchesterOWLSyntaxPrefixNameShortFormProvider(
//				owlManager.getOntologyFormat(owlOntology));
//		BidirectionalShortFormProvider shortFormProvider = new BidirectionalShortFormProviderAdapter(
//				ontologies, sfp);
//		return shortFormProvider;
//	}
}
