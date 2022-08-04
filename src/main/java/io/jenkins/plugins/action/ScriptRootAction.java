package io.jenkins.plugins.action;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.jenkinsci.plugins.workflow.libs.GlobalLibraries;
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration;

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
        System.out.println("Creating action");
        try {
            ScriptsConfig.get().saveScript("test", "println 'hello world___'");
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public String getActiveScript() {
        return ScriptsConfig.get().getScriptSaveByName(activeScript).getText();
    }

    public void saveScript(String text) throws IOException {
        ScriptsConfig.get().saveScript(activeScript, text);
    }

    public String[] runScript() throws IOException {
        ScriptSave scriptSave = ScriptsConfig.get().getScriptSaveByName(activeScript);

        GroovyScriptEngine engine = new GroovyScriptEngine(scriptSave.getFolderPath(), this.getClass().getClassLoader());
        CompilerConfiguration scriptConfig = new CompilerConfiguration();

        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);
        engine.setConfig(scriptConfig);

        try {
            Script script = engine.createScript(scriptSave.getFileName(), new Binding());
            script.setProperty("out", writer);
            Object res = script.run();
            writer.println("");
            if(res != null) {
                writer.println("Result: " + res.toString());
            }
        } catch (ResourceException | ScriptException | CompilationFailedException e) {
            System.out.println("Error while running script");
            e.printStackTrace();
            writer.println(e.getMessage());
        }

        return out.toString().split("\n");
    }
    
}
