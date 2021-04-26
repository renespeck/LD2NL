package org.aksw.owl2nl;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphFormatter;

import java.util.ArrayList;
import java.util.List;

public class OptimizerDepParse {

    public String optimize(String text) {
        try {
            if (text == null || text == "") return text;


            StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
            CoreDocument coreDocument = new CoreDocument(text);
            SemanticGraphFormatter sgf = new SemanticGraphFormatter(1, 1, false, false, false, false, false);

            stanfordCoreNLP.annotate(coreDocument);
            CoreSentence sentence = coreDocument.sentences().get(0);
            SemanticGraph dependencyParse = sentence.dependencyParse();



            List<IndexedWord> nodeList = dependencyParse.vertexListSorted();
            //System.out.println("****************** TEST *********************");
            //System.out.println("List: " + nodeList);
            //System.out.println("List: " + nodeList.get(0).value());
            //System.out.println("List: " + nodeList.get(0).tag());

            //initializing
            System.out.println("Example: dependency parse");
            System.out.println(dependencyParse.toRecoveredSentenceString()); //complete sentence
            System.out.println("**************************************************");
            System.out.println("Text before our changes : " + text);

            List<IndexedWord> ccList = new ArrayList();
            List<Integer> ccIndex = new ArrayList();

            List<IndexedWord> commaList = new ArrayList();
            List<Integer> commaIndex = new ArrayList();

            List<IndexedWord> combinedCcCommaList = new ArrayList();
            List<Integer> combinedCcCommaIndex = new ArrayList();

            List<IndexedWord> verbList = new ArrayList();
            List<Integer> verbIndex = new ArrayList();

            //List<Dict> verbList = new ArrayList();
            List<String> finalText = new ArrayList();

            //loading
            //ccList=dependencyParse.getAllNodesByPartOfSpeechPattern("CC") ;
            //System.out.println("LOLLLLLLL"+ccList);
            /*loading list*/
            for (int i = 0; i < nodeList.size(); i++) {
                if ((nodeList.get(i).tag()).equals("VBZ")) {
                    verbList.add(nodeList.get(i));
                    verbIndex.add(i);
                } else if ((nodeList.get(i).tag()).equals("CC")) {
                    ccList.add(nodeList.get(i));
                    ccIndex.add(i);
                    combinedCcCommaList.add(nodeList.get(i));
                    combinedCcCommaIndex.add(i);

                } else if ((nodeList.get(i).tag()).equals(",")) {
                    commaList.add(nodeList.get(i));
                    commaIndex.add(i);
                    combinedCcCommaList.add(nodeList.get(i));
                    combinedCcCommaIndex.add(i);
                    //System.out.println("comma is found");
                }
            }
            //System.out.println("ajaira:" + verbList + "\n" + ccList + "\n" + commaList + "\n" + commaIndex);
            System.out.println("same verbs: "+allVerbsSame(verbList));

            return text;
        } catch (Exception e) {
            return text;
        }
    }
    public static boolean allVerbsSame(List<IndexedWord> verbs){
        //System.out.println("hola:"+verbs);
        for(int j=1;j<verbs.size();j++){
            if(!((verbs.get(0).value()).equals(verbs.get(j).value()))){
                //not same verb
                //sameVerb=false;
                return false;

            }


        }
        return true;
    }
}
