package io.jenkins.plugins.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.jenkinsci.plugins.workflow.libs.GlobalLibraries;
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration;
import org.kohsuke.stapler.bind.JavaScriptMethod;

import groovy.lang.Binding;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import hudson.Extension;
import hudson.model.RootAction;
import io.jenkins.plugins.config.ScriptsConfig;
import io.jenkins.plugins.util.ScriptSave;
import jenkins.model.Jenkins;

@Extension
public class ScriptRootAction implements RootAction {

    public final String codeTabId = "code";
    public final String configurationTabId = "configuration";

    private String activeScript = "test";

    public ScriptRootAction() {
        
    }

    @Override
    public String getDisplayName() {
        return "Better Scripting";
    }

    @Override
    public String getIconFileName() {
        return "notepad.png";
    }

    @Override
    public String getUrlName() {
        return "betterscripting";
    }

    public List<LibraryConfiguration> getLibraries() {
        GlobalLibraries libs = GlobalLibraries.get();

        return libs.getLibraries();
    }

    public Jenkins getJenkins() {
        return Jenkins.get();
    }

    private boolean check() {
        return true;
        /*try {
            Jenkins.get().getMe().checkPermission(Jenkins.MANAGE);
            return true;
        } catch (AccessDeniedException e) {
            System.out.println("Denied");
            return false;
        }*/
    }

    @JavaScriptMethod
    public String getScriptText() {
        return ScriptsConfig.get().getScriptSaveByName(activeScript).getText();
    }

    @JavaScriptMethod
    public boolean saveScript(String text) {
        try {
            ScriptsConfig.get().saveScript(activeScript, text);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @JavaScriptMethod
    public String runScript() {
        ScriptSave scriptSave = ScriptsConfig.get().getScriptSaveByName(activeScript);

        if(scriptSave == null) {
            return "No existing script was chosen";
        }

        GroovyScriptEngine engine;
        try {
            engine = new GroovyScriptEngine(scriptSave.getFolderPath(), this.getClass().getClassLoader());
        } catch (IOException e1) {
            return 
                new String("Error while creating groovy engine:\n" + e1.getLocalizedMessage())
                    .replace("\n", "<br/>");
        }
        CompilerConfiguration scriptConfig = new CompilerConfiguration();
        scriptConfig.addCompilationCustomizers(new ImportCustomizer().addStarImports(
            "jenkins",
            "jenkins.model",
            "hudson",
            "hudson.model"
        ));

        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);
        engine.setConfig(scriptConfig);

        try {
            Binding binding = new Binding();
            Script script = engine.createScript(scriptSave.getFileName(), binding);
            script.setProperty("out", writer);
            Object res = script.run();
            writer.println("");
            if(res != null) {
                writer.println("Result: " + res.toString());
            }
        } catch (Exception e) {
            writer.println(e.getLocalizedMessage());
        }

        

        return out.toString().replace("\n", "<br/>");
    }
    
}
