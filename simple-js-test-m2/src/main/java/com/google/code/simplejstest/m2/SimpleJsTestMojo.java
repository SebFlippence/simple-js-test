package com.google.code.simplejstest.m2;

import com.google.code.simplejstest.SimpleJsTest;
import com.google.code.simplejstest.SimpleJsTestDependency;
import com.google.code.simplejstest.SimpleJsTestLoader;
import com.google.code.simplejstest.SimpleJsTestResult;
import com.google.code.simplejstest.TestResourceLoader;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 *
 *
 * @goal test
 */
public class SimpleJsTestMojo extends AbstractMojo {

    /** @parameter default-value="${project}" */
    private MavenProject project;

    public void execute() throws MojoExecutionException, MojoFailureException {

        File testBase = new File(project.getBasedir(),
                                 File.separator + "src" +
                                 File.separator + "test" +
                                 File.separator + "javascript" +
                                 File.separator);

		this.getLog().info("------------------------------------------------------------------------");
		this.getLog().info("");
        this.getLog().info("Preparing to execute JavaScript unit tests from: " + testBase.getPath());

		if (!testBase.exists()) {
            this.getLog().warn("JavaScript test directory does not exist");
            return;
        }

        TestResourceLoader resourceLoader = new M2TestResourceLoader(project);

        int globalTestCount = 0;
        for (File file : testFilesIn(testBase)) {
			int testCount = 0;

			this.getLog().info("");
			this.getLog().info("------------------------------------------------------------------------");
			this.getLog().info("");

            this.getLog().info("Loading JavaScript unit test(s) in file: " + file.getName());
            this.getLog().info("");

            String testFileContent = null;
            try {
                testFileContent = contentsOfFile(file);
            } catch (IOException ioe) {
                throw new MojoExecutionException(
                                    "Test file could not be loaded: " + file +
                                    ", " + ioe.getMessage());
            }

            List<SimpleJsTest> tests;
            try {
                tests = new SimpleJsTestLoader(resourceLoader).loadTestsForFile(testFileContent);
            } catch (IOException ioe) {
                throw new MojoExecutionException(ioe.getMessage());
            }

            this.getLog().info("Processing " + tests.size() + " JavaScript unit test(s) in file: " + file.getName());
            this.getLog().info("");

            if (!tests.isEmpty()) {
                this.getLog().info("Test file declared dependencies of:");
                for (SimpleJsTestDependency dependency : tests.get(0).getDependencies()) {
                    this.getLog().info("        " + dependency.getName());
                }
            }

            this.getLog().info("");



            for (SimpleJsTest test : tests) {
                this.getLog().info("Test: " + test.getName());
                
                SimpleJsTestResult result = test.execute();
                if (result.getState()==SimpleJsTestResult.State.FAIL) {
                    throw new MojoFailureException(file.getName() + "#" + test.getName() + " failed with " + result.toString() + ": " + result.getMessage());
                }

                testCount++;
                globalTestCount++;
            }
            
			this.getLog().info("");
			this.getLog().info("All " + testCount + " test(s) passed");
        }

		this.getLog().info("");
		this.getLog().info("------------------------------------------------------------------------");
        this.getLog().info("JavaScript unit testing complete. " + globalTestCount + " test(s) executed successfully");
		this.getLog().info("------------------------------------------------------------------------");
		this.getLog().info("");
    }

    private List<File> testFilesIn(File dir) {
        List<File> list = new ArrayList<File>();
        testFilesIn(dir, list);
        return list;
    }

    private void testFilesIn(File dir, List<File> list) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                testFilesIn(file, list);
            } else {
				if (isValidTestFile(file)) {
	                list.add(file);
                }
            }
        }
    }

    private boolean isValidTestFile(File file) {
        String fileName = file.getName();
        if (fileName.endsWith(".js") && fileName.contains("Test")) {
            return true;
        }

        return false;
    }

    private static String contentsOfFile(File file) throws IOException {
        return Files.toString(file, Charset.forName("UTF-8"));
    }

    private static class M2TestResourceLoader implements TestResourceLoader {
        private final MavenProject project;

        public M2TestResourceLoader(MavenProject project) {
            this.project = project;
        }

        public String loadResource(String path) throws FileNotFoundException, IOException {
            File file = new File(project.getBasedir().getPath(),
                                 File.separator + "src" +
                                 File.separator + path);
            if (!file.exists()) {
                throw new FileNotFoundException("Test resource does not exist: " + path);
            }
            if (file.isDirectory()) {
                throw new FileNotFoundException("Test resource can not be a directory: " + path);
            }
            return contentsOfFile(file);
        }
    }
}
