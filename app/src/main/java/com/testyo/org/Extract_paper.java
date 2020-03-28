package com.testyo.org;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.ICell;
import org.apache.poi.xwpf.usermodel.IRunElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFieldRun;
import org.apache.poi.xwpf.usermodel.XWPFHyperlinkRun;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFSDT;
import org.apache.poi.xwpf.usermodel.XWPFSDTCell;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import androidx.annotation.RequiresApi;


public class Extract_paper {
    private File file;
    //private static String packageName = mCtx.getPackageName();

    // declaring start variables whichh are used in extraction


    public static MainActivity mainActivity;

    public static String questionStartsWith = "Q";
    public static String optionStartsWith = "A.";
    public static String option2StartsWith = "B.";
    public static String option3StartsWith = "C.";
    public static String option4StartsWith = "D.";
    public static String answerStartsWith = "Answer |||";
    public static String solutionStartsWith = "Solution |||";

    public static Boolean answerIncluded = true;
    public static Boolean solutionIncluded = true;




    private static File stylesheet;
    private static TransformerFactory tFactory = TransformerFactory.newInstance();;



    private static StreamSource stylesource = null ;
    public static Boolean extractAnswers = false;
    public static Boolean solutionModeActive = false;

    static private List<String> mathMLList = new ArrayList<String>(16);
//    String xmlText = "xmlText";
//    String MathML = "MathML";


    static File xmlText = null;   //new File("C:\\Users\\DELL\\Documents\\Word Files\\xmlText.xml");
    static File mathMLText = null;  // File("C:\\Users\\DELL\\Documents\\Word Files\\MathML.xml");
    static private int i=0;
    static StringBuilder buf = new StringBuilder();
    static StringBuilder htmlbuffer = new StringBuilder();
    static Boolean tableactive = false;
    static Boolean questionactive = false;
    static Boolean optionactive = false;
    static Boolean answeractive = false;
    static Boolean solutionactive = false;

    static int neww = 1;
    static int s = -1;
    static int q = 0;
    static int tempo = 0;
    static int finalo = 0;
    public static List<StringBuilder> questionlist = new ArrayList<StringBuilder>();
    public static List<StringBuilder> optionlist = new ArrayList<StringBuilder>();
    public static List<StringBuilder> answerlist = new ArrayList<StringBuilder>();
    public static List<StringBuilder> modifiedanswerlist = new ArrayList<StringBuilder>();
    public static List<StringBuilder> solutionlist = new ArrayList<StringBuilder>();
    static Document mathML;
    static Context mCtx;



    // constructor

    public Extract_paper(File file, Context mCtx, StreamSource stylesource) {
        this.mCtx = mCtx;
        this.file = file;
        this.stylesource = stylesource;
//        questionlist.add(new StringBuilder());
//        questionlist.get(0).append("my name is khan");
    }


    // getters

    public static List<StringBuilder> getAnswerlist() {
        return answerlist;
    }

    public static List<StringBuilder> getModifiedanswerlist() {
        return modifiedanswerlist;
    }

    public static List<StringBuilder> getQuestionlist() {
        return questionlist;
    }

    public static List<StringBuilder> getOptionlist() {
        return optionlist;
    }

    public static List<StringBuilder> getSolutionlist() {
        return solutionlist;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void runExtractCode() {

        //stylesheet = new File(pathOmml);
        tFactory = TransformerFactory.newInstance();
        //stylesource = stylesourc;

        if(stylesource == null){
//            Log.i("Enter","also null");
        }

        // crating temp file

        xmlText = createTempFile("xmlText",".xml");
        mathMLText = createTempFile("mathMLText",".xml");


        Log.i("Enter","I am in Extract Code");
        Log.i("Enter",file.getAbsolutePath());

        try (XWPFDocument document = new XWPFDocument(new FileInputStream(file))) {

            initializeAllVariable();
            // decide what to extract and then reload respected list

            if(Extract_paper.solutionModeActive){
                solutionlist.clear();
                solutionlist.add(new StringBuilder());
            }else if(extractAnswers){
                answerlist.clear();
            }else {
                questionlist.clear();
                optionlist.clear();
                answerlist.clear();
                solutionlist.clear();
                modifiedanswerlist.clear();
                questionactive = true;
                questionlist.add(new StringBuilder());
//                questionlist.add(new StringBuilder());
//                questionlist.get(0).append("");

            }



            mainActivity = MainActivity.getInstance();
            traverseBodyElements(document.getBodyElements());


            if(xmlText.exists()){
                xmlText.delete();
            }
            if(mathMLText.exists()){
                mathMLText.delete();
            }

//            Log.i("Enter","I am at the end");
//            Log.i("Enter",questionlist.get(0).toString());
//            Log.i("Enter",optionlist.get(0).toString());
//            Log.i("Enter",optionlist.get(1).toString());
//            Log.i("Enter",optionlist.get(2).toString());
//            Log.i("Enter",optionlist.get(3).toString());
//        getHtml();
//            System.out.println("1.  " + questionlist.get(0));
//            System.out.println("2.  " + optionlist.get(0));
//            System.out.println("3.  " + optionlist.get(1));
//            System.out.println("4.  " + optionlist.get(2));
//            System.out.println("5.  " + optionlist.get(3));
//        System.out.println("1.  " + questionlist.get(1));
//        System.out.println("2.  " + optionlist.get(4));
//        System.out.println("3.  " + optionlist.get(5));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initializeAllVariable() {
        tableactive = false;
        questionactive = false;
        optionactive = false;
        answeractive = false;
        solutionactive = false;
        neww = 1;
        s = -1;
        q = 0;
        tempo = 0;
        finalo = 0;
    }


    static private void convertToMathML(XWPFParagraph para) throws Exception {

        for (CTOMath ctomath : para.getCTP().getOMathList()) {
            //Log.i("Enter", mathMLList.get(0));
            mathMLList.add(getMathML(ctomath));
           // Log.i("Enter", mathMLList.get(0));

        }
        for (CTOMathPara ctomathpara : para.getCTP().getOMathParaList()) {
            for (CTOMath ctomath : ctomathpara.getOMathList()) {
               // Log.i("Enter", "before para mathml - " + mathMLList.get(0));
                mathMLList.add(getMathML(ctomath));

               // Log.i("Enter", "after para mathml - " + mathMLList.get(0));
            }
        }
    }

    private void saveParaAsXMLFile(XWPFParagraph paragraph1) throws Exception {

        String wholeText= "";
        String startstring = "";
        convertToMathML(paragraph1);
        // local variable to store mathmllist size
        int mathMlListSize = mathMLList.size();

        if(mathMlListSize == 0){

            wholeText = paragraph1.getText();
            startstring= wholeText;
            if(tableactive){
                buf.append(wholeText);

                // Log.i("Enter","Buffer data -   " + buf.toString());
            }

        }else{
            FileWriter xmlTextfileWriter = new FileWriter(xmlText);
            FileWriter mathMLfileWriter = new FileWriter(mathMLText);
            xmlTextfileWriter.write(getXMLFromParagraph(paragraph1));
//        System.out.println(getXMLFromParagraph(paragraph1));

            int l =0;

            mathMLfileWriter.write("<math>");
            for(String L :mathMLList ) {
                mathMLfileWriter.write("<math>");
                if(tableactive) {
                    //System.out.println(" List = " +L + i );
                    l++;
                }
                mathMLfileWriter.write(L);

                mathMLfileWriter.write("</math>");
            }
            mathMLfileWriter.write("</math>");



            //Log.i("Enter","mathml list sizee -   " + mathMLList.size());
            mathMLList.clear();

//    mathMLfileWriter.write(mathMLList.get(1));

            xmlTextfileWriter.close();
            mathMLfileWriter.close();

            try {

                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(xmlText);
                mathML = docBuilder.parse(mathMLText);

                // Get the root element
                Node xml = doc.getFirstChild();

                startstring = xml.getTextContent();

//            System.out.println("firstnodetext   " + startstring);
//            System.out.println("xmlText = "+xml.toString());
                NodeList math = mathML.getFirstChild().getChildNodes();

                List<Node> imported = new ArrayList<Node>(16);

                // Log.i("Enter","mathml length   " + String.valueOf(math.getLength()));

                for(int i=0; i<math.getLength(); i++) {

                    imported.add(doc.importNode(math.item(i), true));

                    //Log.i("Enter","mathml node  " + imported.get(i).getNodeName());

                }


                NodeList xmlchild = xml.getChildNodes();
                int rNo = 0;

                for (int j = 0; j < xmlchild.getLength(); j++) {

                    Node node = xmlchild.item(j);

                    if ("m:oMath".equals(node.getNodeName()) || "m:oMathPara".equals(node.getNodeName())) {
//                    System.out.println("");
                        xml.replaceChild(imported.get(rNo), node);
                        rNo++;
                    }
                }

                clearFileContents();

                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(xmlText);
                transformer.transform(source, result);

//            System.out.println("Done");
                //filename is filepath string
                BufferedReader br = new BufferedReader(new FileReader(xmlText));
                String line;
                StringBuilder sb = new StringBuilder();

                while((line=br.readLine())!= null){
                    sb.append(line.trim());
                }

                Log.i("Enter","mathml list size -   " + mathMLList.size());
                wholeText = sb.toString();
                if(tableactive){
                    buf.append(wholeText);

                    // Log.i("Enter","Buffer data -   " + buf.toString());
                }
                String sss =  "paragraph " + neww + " = ";
//         System.out.println( wholeText);
                neww++;
            } catch (ParserConfigurationException pce) {
                pce.printStackTrace();
            } catch (TransformerException tfe) {
                tfe.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (SAXException sae) {
                sae.printStackTrace();
            }

        }





            //Log.i("Enter","wholetext   " + wholeText);
            //question and option
            int t = startstring.compareTo("");
            if(t != 0){
                if(Extract_paper.solutionModeActive){
                    String sno = String.valueOf(s+2);//char)(s+2+'0');
                   // Log.i("startwith","Boolean for startwith "+ startstring.startsWith("Q"+sno) );

                    if(startstring.length() > 1 && startstring.startsWith(solutionStartsWith+sno)){//startstring.charAt(0) == 'Q' && startstring.charAt(1) == sno) {

                        solutionlist.add(new StringBuilder());
                        s = s+1;
                       // Log.i("Enterr","S "+ s );

                    }


                }else if(questionactive){

                    //questionlist.add(new StringBuilder());
                    if(startstring.length() > 1 && startstring.startsWith(optionStartsWith)) {

                        optionlist.add(new StringBuilder());
                        questionactive = false;
                        optionactive = true;
                        tempo = 0;
                    }


//              System.out.println("questionactive  q=0");
                }else if(optionactive){

                    if(startstring.length() > 1 && startstring.startsWith(option2StartsWith)){//startstring.charAt(0) == 'A' && startstring.charAt(1) == '2'){

                        optionlist.add(new StringBuilder());
                        tempo = 1;
                    }else if(startstring.length() > 1 && startstring.startsWith(option3StartsWith)){//startstring.charAt(0) == 'A' && startstring.charAt(1) == '3'){

                        optionlist.add(new StringBuilder());
//                   clearFileContents();
                        tempo = 2;
                    }else if(startstring.length() > 1 && startstring.startsWith(option4StartsWith)){//startstring.charAt(0) == 'A' && startstring.charAt(1) == '4'){

                        optionlist.add(new StringBuilder());
//                   clearFileContents();
                        tempo = 3;



//               System.out.println("optionactive  tempo = 3");
                    }else if(answerIncluded && startstring.startsWith(answerStartsWith)){
                         answerlist.add(new StringBuilder());
                         optionactive = false;
                         answeractive = true;
                    }else if(solutionIncluded && startstring.startsWith(solutionStartsWith)){
                        solutionlist.add(new StringBuilder());
                        optionactive = false;
                        solutionactive= true;
                    }else if(startstring.startsWith(questionStartsWith + String.valueOf(q+2))){
                        questionlist.add(new StringBuilder());
                        optionactive = false;
                        questionactive = true;
                        q += 1;
                    }
                }else if(solutionIncluded && answeractive && startstring.startsWith(solutionStartsWith)) {

                    solutionlist.add(new StringBuilder());
                    answeractive = false;
                    solutionactive = true;
                }if( solutionIncluded && startstring.startsWith(questionStartsWith +String.valueOf(q+2))) {

                    questionlist.add(new StringBuilder());
                    solutionactive = false;
                    questionactive = true;

                    q+=1;
                }




                if(!tableactive){

                    if(Extract_paper.solutionModeActive){

                        solutionlist.get(s).append(wholeText);
                        Log.i("Enterr","S "+ s +wholeText);

                    }else if(optionactive){

                        finalo = (q*4) + tempo;

                        optionlist.get(finalo).append(wholeText);
                        Log.i("Enterr","O "+finalo+wholeText);
//               System.out.println("insideoptionlist finalo = " + finalo);

                    }else if(questionactive){

//                        String fileName = MainBeforeActivity.selectedPaperName+String.valueOf(q)+".xml";
//                        mainActivity.save(wholeText,fileName);


                        questionlist.get(q).append(wholeText);
                        Log.i("Enterr","Q"+q+wholeText);

//              System.out.println("insideqlist q = " + q);
                    }else if (answeractive){
                        answerlist.get(q).append(wholeText);
                        Log.i("Enterr","A"+q+wholeText);
                    }else if (solutionactive){
                        wholeText = wholeText.replace("*","<br>");
                        wholeText = wholeText.replace("|||","");         // to remove extra symbol from answer
                        solutionlist.get(q).append(wholeText);
                        Log.i("Enterr","S"+q+wholeText);
                    }

                }

            }




    }


    static private String getXMLFromParagraph(XWPFParagraph para) {
       // Log.i("Enter","whole xml text"+para.getCTP().xmlText());
        return para.getCTP().xmlText();
    }


//    static private void getHtml() {
//        try {
//
//            //creating a sample HTML file
//            String encoding = "UTF-8";
//            FileOutputStream fos = new FileOutputStream("result.html");
//            OutputStreamWriter writer = new OutputStreamWriter(fos, encoding);
//            writer.write("<!DOCTYPE html>\n");
//            writer.write("<html lang=\"en\">");
//            writer.write("<head>");
//            writer.write("<meta charset=\"utf-8\"/>");
//
//            //using MathJax for helping all browsers to interpret MathML
//            writer.write("<script type=\"text/javascript\"");
//            writer.write(" async src=\"https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.1/MathJax.js?config=MML_CHTML\"");
//            writer.write(">");
//            writer.write("</script>");
//
//            writer.write("</head>");
//            writer.write("<body>");
//            writer.write("<p>Following formulas was found in Word document: </p>");
//
////  int i = 1;
////  for (String mathML : mathMLList) {
////   writer.write("<p>Formula" + i++ + ":</p>");
////   writer.write(mathML);
////   writer.write("<p/>");
////  }
//
//
//
//            writer.write("</body>");
//            writer.write("</html>");
//            writer.close();
//
//            Desktop.getDesktop().browse(new File("result.html").toURI());
//
//        } catch(Exception e) {
//            System.out.println(e);
//        }
//    }
    public static void removeAll(Node node, short nodeType, String name) {
        if (node.getNodeType() == nodeType && (name == null || node.getNodeName().equals(name))) {
            node.getParentNode().removeChild(node);
        } else {
            NodeList list = node.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
//          System.out.println(i);
                removeAll(list.item(i), nodeType, name);
            }
        }
    }

    public static void clearFileContents(){
        try {
            removeAll(mathML, Node.ELEMENT_NODE, "math");
            removeAll(mathML, Node.COMMENT_NODE, null);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(mathML);
            StreamResult result = new StreamResult(mathMLText);
            transformer.transform(source, result);

        }catch(Exception e) {

        }
    }


    static String getMathML(CTOMath ctomath) throws Exception {

        if(stylesource == null){
           // Log.i("Enter","also null2");
        }
        Transformer transformer = tFactory.newTransformer(stylesource);

        Node node = ctomath.getDomNode();

       // Log.i("Enter", "entry in get mathml");

        DOMSource source = new DOMSource(node);
        StringWriter stringwriter = new StringWriter();
        StreamResult result = new StreamResult(stringwriter);
        transformer.setOutputProperty("omit-xml-declaration", "yes");
        transformer.transform(source, result);

        String mathML = stringwriter.toString();

        //Log.i("Enter", "mathml string" + mathML);
        stringwriter.close();

        mathML = mathML.replaceAll("xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\"", "");
        mathML = mathML.replaceAll("xmlns:mml", "xmlns");
        mathML = mathML.replaceAll("mml:", "");

        //Log.i("Enter", "replaced mathml string" + mathML);
        return mathML;
    }

    static void traversePictures(List<XWPFPicture> pictures) throws Exception {
        for (XWPFPicture picture : pictures) {
//   System.out.println("nitesh  === "+picture);
            //picture.getPictureData().
            XWPFPictureData pictureData = picture.getPictureData();
//   System.out.println("niteshinfo   "+i+pictureData);
            byte[] bytepic=pictureData.getData();

            String imgageBase64 = Base64.encodeToString(bytepic, Base64.DEFAULT);
            String image = "data:image/png;base64," + imgageBase64;

            //htmlbuffer.append("<img src='"+ image +"' />");
            String html="<img src='"+ image +"' />";

          //  Log.i("Enter","Image html-"+html);

            addBitmapImage(html);

            //Bitmap bmp = BitmapFactory.decodeByteArray(bytepic, 0, bytepic.length);

            ////// Now implemented

           // BufferedImage imag=ImageIO.read(new ByteArrayInputStream(bytepic));
            //captureimage(imag,i,flag,j);

            //addBitmapImage(bmp);
            String fname = "";


                //saveImageInCache(bmp,fname);

//           System.out.println("========"i);
//               ImageIO.write(imag, "png", new File("C:\\Users\\hp\\Documents\\Word Files\\r"+ i +".png"));
//
//               i=i+1;

        }
    }

    private static void addBitmapImage(String bmp) {

       // adding image to our text


        if(bmp != null)
        {


            if(Extract_paper.solutionModeActive){
                solutionlist.get(s).append(bmp);

            }else if(questionactive){

                //fname = "question"+ String.valueOf(q);


                questionlist.get(q).append(bmp);

            //    Log.i("Enter","I found image no. = "+ String.valueOf(q));
                //ImageIO.write(imag, "png", new File("C:\\Users\\DELL\\Documents\\Word Files\\question"+ q +".png"));


            }else if(optionactive){

                finalo = q*4 + tempo;


                optionlist.get(finalo).append(bmp);



                //fname = "answer" + String.valueOf(finalo);
                //ImageIO.write(imag, "png", new File("C:\\Users\\DELL\\Documents\\Word Files\\option"+ finalo +".png"));

            }else if (answeractive){
                answerlist.get(q).append(bmp);
            }else if (solutionactive){
                solutionlist.get(q).append(bmp);
            }
        }else{
            System.out.println("imag is empty");
        }

    }

    public static void saveImageInCache(Bitmap bmp, String fname){

        File file = createTempFile(fname,".jpg");



        // code to save bitmap image in cache directory

        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static File createTempFile(String fname, String suffix) {
        File outputDir = mCtx.getCacheDir(); // context being the Activity pointer
        File file = null;
        try {

            file = File.createTempFile(fname, suffix, outputDir);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    static void traverseRunElements(List<IRunElement> runElements) throws Exception {
//        System.out.println("inside run");
        for (IRunElement runElement : runElements) {
            if (runElement instanceof XWPFFieldRun) {
                XWPFFieldRun fieldRun = (XWPFFieldRun) runElement;
//    System.out.println(fieldRun.getClass().getName());
//    System.out.println(fieldRun);
                traversePictures(fieldRun.getEmbeddedPictures());
            } else if (runElement instanceof XWPFHyperlinkRun) {
                XWPFHyperlinkRun hyperlinkRun = (XWPFHyperlinkRun) runElement;
//    System.out.println(hyperlinkRun.getClass().getName());
//    System.out.println(hyperlinkRun);
                traversePictures(hyperlinkRun.getEmbeddedPictures());
            } else if (runElement instanceof XWPFRun) {
                XWPFRun run = (XWPFRun) runElement;
//    System.out.println(run.getClass().getName());
//    System.out.println(run);
                traversePictures(run.getEmbeddedPictures());
            } else if (runElement instanceof XWPFSDT) {
                XWPFSDT sDT = (XWPFSDT) runElement;
//    System.out.println(sDT);
//    System.out.println(sDT.getContent());
                //ToDo: The SDT may have traversable content too.
            }
        }
    }

    void traverseTableCells(List<ICell> tableICells) throws Exception {
        for (ICell tableICell : tableICells) {
            buf.append("<th>");
            if (tableICell instanceof XWPFSDTCell) {
                XWPFSDTCell sDTCell = (XWPFSDTCell) tableICell;
                buf.append(sDTCell.toString());
            //    Log.i("Enter","Table cell data - "+sDTCell.toString());
//    System.out.println(sDTCell);
                //ToDo: The SDTCell may have traversable content too.
            } else if (tableICell instanceof XWPFTableCell) {
                XWPFTableCell tableCell = (XWPFTableCell) tableICell;
               // Log.i("Enter","Table cell data - ");
//    System.out.println(tableCell);
                traverseBodyElements(tableCell.getBodyElements());
            }
            buf.append("</th>");
        }
    }

    void traverseTableRows(List<XWPFTableRow> tableRows) throws Exception {
        for (XWPFTableRow tableRow : tableRows) {
//   System.out.println(tableRow);
            buf.append("<tr>");
            traverseTableCells(tableRow.getTableICells());
            buf.append("</tr>");
        }
    }

     void traverseBodyElements(List<IBodyElement> bodyElements) throws Exception {
        for (IBodyElement bodyElement : bodyElements) {
            if (bodyElement instanceof XWPFParagraph) {
                XWPFParagraph paragraph = (XWPFParagraph) bodyElement;

                if(extractAnswers){

                    answerlist.add(new StringBuilder(paragraph.getText()));
                    //Log.i("Enter","answer added in list "+ paragraph.getText());
                }else {
                    saveParaAsXMLFile(paragraph);


                    traverseRunElements(paragraph.getIRuns());
                }
//    System.out.println(paragraph);

                //Log.i("Enter","I am in paragraph");


            } else if (bodyElement instanceof XWPFSDT) {
                XWPFSDT sDT = (XWPFSDT) bodyElement;
//    System.out.println(sDT);
//    System.out.println(sDT.getContent());
                //ToDo: The SDT may have traversable content too.
            }
            else if (bodyElement instanceof XWPFTable) {
                XWPFTable table = (XWPFTable) bodyElement;
                int trow = table.getNumberOfRows();
                tableactive = true;
                buf.append("<table border=\"1\">");


//    System.out.println(table.getCTTbl().xmlText());
                traverseTableRows(table.getRows());


                buf.append("</table>");

              //  Log.i("Enter","Final Buffer data- " + buf.toString());

                if(Extract_paper.solutionModeActive){
                        solutionlist.get(s).append(buf);
                }else if(questionactive){

                    questionlist.get(q).append(buf);


                }else if(optionactive){

                    finalo = q*4 + tempo;

                    optionlist.get(finalo).append(buf);

                }else if (answeractive){
                    answerlist.get(q).append(buf);
                }else if (solutionactive){
                    solutionlist.get(q).append(buf);
                }
                buf.delete(0, buf.length());
                tableactive = false;
//                System.out.println(buf);
            }
        }
    }


    public static void fillDataInModifiedList(){

        Log.i("Enterrrr","size answerlist" +answerlist.size());
        for(int i=0;i<MainActivity.totalQuestion;i++){
            Log.i("Enter","I am in modify");
            String rightAnswer = answerlist.get(i).toString();
            rightAnswer = rightAnswer.replace(" ","");

            modifiedanswerlist.add(new StringBuilder(""));

            if(rightAnswer.endsWith("A") || rightAnswer.endsWith("1")){
                //modifiedanswerlist.add(new StringBuilder("1"));
                modifiedanswerlist.get(i).append("1");
             //   Log.i("Enter","I am in A");
            }else if(rightAnswer.endsWith("B") || rightAnswer.endsWith("2")){
                //modifiedanswerlist.add(new StringBuilder("2"));
                modifiedanswerlist.get(i).append("2");
               // Log.i("Enter","I am in B");
            }
            else if(rightAnswer.endsWith("C") || rightAnswer.endsWith("3")){
                //modifiedanswerlist.add(new StringBuilder("3"));
                modifiedanswerlist.get(i).append("3");
               // Log.i("Enter","I am in C");
            }
            else if(rightAnswer.endsWith("D") || rightAnswer.endsWith("4")){
                //modifiedanswerlist.add(new StringBuilder("4"));
                modifiedanswerlist.get(i).append("4");
               // Log.i("Enter","I am in D");
            }

            if(modifiedanswerlist.get(i).toString() ==""){
                Log.i("Enter","modified list index" + i + " is incorrect");
                modifiedanswerlist.get(i).append("1");
            }
        }

//        if(answerlist.size()!=modifiedanswerlist.size()){
//            Toast.makeText(mCtx,"ModifiedList Error",Toast.LENGTH_SHORT).show();
//        }

    }


  }
