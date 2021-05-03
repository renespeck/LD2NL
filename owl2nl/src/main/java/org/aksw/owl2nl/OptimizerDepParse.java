package org.aksw.owl2nl;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphFormatter;
//import org.graalvm.compiler.core.common.type.ArithmeticOpTable;

import javax.xml.soap.SOAPPart;
import java.util.ArrayList;
import java.util.List;

public class OptimizerDepParse {

    public String optimize(String text) {
        try {
            if (text == null || text == "") return text;

            text="something that a man that sings rock and that a man that sings jazz  or a man that sings karaoke  or a man that sings bal";
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
            //System.out.println("same verbs: "+allVerbsSame(verbList));
            //List<IndexedWord> afterSubjectAggregation=subjectAggregation(nodeList,verbIndex,combinedCcCommaIndex);
            //System.out.println("observing :");
            //for (int i=0;i<afterSubjectAggregation.size();i++){
            //    System.out.print(afterSubjectAggregation.get(i).value()+" ");
            //}
            //System.out.println();
            System.out.println("same subjects: "+allSubjectsSame(nodeList,verbIndex,combinedCcCommaIndex));


            return text;
        } catch (Exception e) {
            return text;
        }
    }
    //method to check whether all verbs in the sentence are same or not
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
    //method to check whether all verbs in the sentence are same or not
    public static boolean allSubjectsSame(List<IndexedWord> nodeList,List<Integer> verbInderx,List<Integer> combinedCcCommaIndex){
        //System.out.println("hola:"+verbs);
        int numberOfSubjects=verbInderx.size();
        List<String> subjects=new ArrayList();
        //storing subjects
        for(int j=0;j<numberOfSubjects;j++){
            if(j==0){
                int pointer=0;
                String s=""; //temporary string
                for(;pointer<verbInderx.get(j);pointer++){
                    s+=nodeList.get(pointer).value();
                }
                subjects.add(s);
            }
            else{
                int pointer=combinedCcCommaIndex.get(j-1)+1;
                String s=""; //temporary string
                for(;pointer<verbInderx.get(j);pointer++){
                    s+=nodeList.get(pointer).value();
                }
                subjects.add(s);
            }
        }
        for(int i=0;i<subjects.size()-1;i++){
            //String maxString="";
            if(subjects.get(i).length()>=subjects.get(i+1).length()){
                if(!(subjects.get(i).contains(subjects.get(i+1)))){
                    return false;
                }
            }
            else if(!(subjects.get(i+1).contains(subjects.get(i)))){
                return false;
            }


        }

        return true;
    }
    //if the subject are same then this method will perform the aggregation on subjects
    public static List<IndexedWord> subjectAggregation(List<IndexedWord> nodeList,List<Integer> verbIndex,List<Integer> combinedCcCommaIndex){
        //System.out.println("hola:"+verbs);
        List<IndexedWord> aggregatedList=new ArrayList();
        int pointer=0;
        for(int j=0;j<verbIndex.size();j++){
            if(j==0){
                for(;pointer<=combinedCcCommaIndex.get(j);pointer++){
                    aggregatedList.add(nodeList.get(pointer));
                }
            }
            else if (j==verbIndex.size()-1){
                pointer=verbIndex.get(j);
                for(;pointer<nodeList.size();pointer++){
                    aggregatedList.add(nodeList.get(pointer));
                }
            }
            else{
                pointer=verbIndex.get(j);
                for (;pointer<=combinedCcCommaIndex.get(j);pointer++){
                    aggregatedList.add(nodeList.get(pointer));
                }
            }

        }
        return aggregatedList;
    }
}
