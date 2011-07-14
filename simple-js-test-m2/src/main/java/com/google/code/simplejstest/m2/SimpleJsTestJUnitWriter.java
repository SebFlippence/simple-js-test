package com.google.code.simplejstest.m2;

import com.google.code.simplejstest.SimpleJsTestResult;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.util.ArrayList;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

/**
 * Custom JUnit test writer that outputs test results to XML files. The files
 * use a similar format to the JUnit task XML formatter
 */
public class SimpleJsTestJUnitWriter {

    private static final String ENCODING_UTF_8 = "utf-8";
    private static final String TAG_SUITES = "testsuites";
    private static final String TAG_SUITE = "testsuite";
    private static final String TAG_CASE = "testcase";
    private static final String TAG_ERROR = "error";
    private static final String TAG_FAILURE = "failure";
    private static final String TAG_ERRORS = "errors";
    private static final String TAG_FAILURES = "failures";
    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_CLASS = "classname";
    private static final String ATTRIBUTE_TYPE = "type";
    private static final String ATTRIBUTE_MESSAGE = "message";
    private static final String ATTRIBUTE_TIME = "time";

    private String mReportFile;
    private String mReportDir;

    private ArrayList<SimpleJsTestResult> tests = new ArrayList<SimpleJsTestResult>();
    private ArrayList<String> errors = new ArrayList<String>();

    /**
     * Creates a new writer.
     *
     * @param reportFile name of the report file(s) to create
     * @param reportDir  path of the directory under which to write files
     */
    public SimpleJsTestJUnitWriter(String reportFile, String reportDir) {
        this.mReportFile = reportFile;
        this.mReportDir = reportDir;
    }

    /**
     * Adds a new test for reporting
     * 
     * @param test a SimpleJsTestResult object
     */
    public void newTest(SimpleJsTestResult test) {
        this.tests.add(test);
    }

    /**
     * Fails the current test with a generic error (e.g. JavaScript syntax error)
     * 
     * @param error message as a String
     */
    public void addError(String error) {
        this.errors.add(error);
    }

    /**
     * Internal function for looping through all of the tests added to this object and generating an XML object
     * in a JUnit report format
     * 
     * @return org.dom4j.Document
     */
    private Document convertXml() throws IOException {
        int testCount = 0;
        int testFailCount = 0;
        int testErrorCount = 0;
        float testTimeTotal = 0;

        for (SimpleJsTestResult test : this.tests) {
             testCount++;
              if (test.getState().equals(SimpleJsTestResult.State.FAIL)) {
                 testFailCount++;
             }
             testTimeTotal = testTimeTotal + test.getTime();
         }

        for (String error : this.errors) {
            testErrorCount++;
        }

        Document document = DocumentHelper.createDocument();
        Element root = document.addElement(TAG_SUITE)
                .addAttribute(ATTRIBUTE_NAME, this.mReportFile)
                .addAttribute(TAG_ERRORS,  Integer.toString(testErrorCount))
                .addAttribute(TAG_FAILURES, Integer.toString(testFailCount))
                .addAttribute(ATTRIBUTE_TIME, String.valueOf(testTimeTotal));

        for (String error : this.errors) {
            root.addText(error);
        }

        for (SimpleJsTestResult test : this.tests) {
            Element testcase = root.addElement(TAG_CASE)
                    .addAttribute(ATTRIBUTE_CLASS, this.mReportFile + " " + test.getName())
                    .addAttribute(ATTRIBUTE_NAME, test.getName())
                    .addAttribute(ATTRIBUTE_TIME, String.valueOf(test.getTime()));
            if (test.getState().equals(SimpleJsTestResult.State.FAIL)) {
                testcase.addText(test.getMessage());
                this.writeContent("ERROR--" + this.mReportFile + "--" + test.getName() + ".js", test.getCode());
            }
        }
         
        return document;        
    }

    /**
     * Creates a fileName in the report directory with the given content
     * 
     * @param fileName
     * @param content
     * @throws IOException 
     */
    public void writeContent(String fileName, String content) throws IOException {
        FileWriter writer = new FileWriter(this.mReportDir + File.separator + fileName);
        try {
            writer.write(content);
        } catch (Exception e) {
            throw new IOException("Unable to write content to file: "+e.getMessage());
        }
        writer.close();
    }

    /**
     * Converts tests added to this object to XML and writes out the JUnit reports to the reportDir
     * 
     * @throws IOException 
     */
    public void writeXmlReport() throws IOException {
        // Create target reportDir if it doesn't already exist
        File reportDir = new File(this.mReportDir);
        if (!reportDir.exists()) {
            try {
                reportDir.mkdir();
            } catch (Exception e){
                throw new IOException("Unable to create JUnit report directory: "+e.getMessage());
            }
        }

        //Pretty print XML and save
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(new FileWriter(this.mReportDir + File.separator + this.mReportFile), format);
        try {
            writer.write(this.convertXml());
        } catch (Exception e) {
            throw new IOException("Unable to write to JUnit report: "+e.getMessage());
        }
        writer.close();
    }
}