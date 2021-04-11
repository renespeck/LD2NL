package org.aksw.owl2nl;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphFormatter;

import java.util.List;

public class OptimizerDepParse {

    public String optimize(String text) {

        if (text == null || text == "") return text;

        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
        CoreDocument coreDocument = new CoreDocument(text);
        SemanticGraphFormatter sgf = new SemanticGraphFormatter(1,1,false,false,false,false,false);

        stanfordCoreNLP.annotate(coreDocument);
        CoreSentence sentence = coreDocument.sentences().get(0);
        SemanticGraph dependencyParse = sentence.dependencyParse();

        List<IndexedWord> iList = dependencyParse.vertexListSorted();
        System.out.println("****************** TEST *********************");
        System.out.println("List: " + iList);
        System.out.println("List: " + iList.get(0).value());
        System.out.println("List: " + iList.get(0).tag());

        System.out.println("Example: dependency parse");
        System.out.println(dependencyParse.toRecoveredSentenceString());

        return text;
    }
}