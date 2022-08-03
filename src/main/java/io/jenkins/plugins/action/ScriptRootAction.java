package io.jenkins.plugins.action;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLConnection;
import java.util.List;

import org.codehaus.groovy.bsf.GroovyEngine;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.jenkinsci.plugins.workflow.libs.GlobalLibraries;
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration;

import groovy.lang.Binding;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceConnector;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import hudson.Extension;
import hudson.model.RootAction;
import io.jenkins.plugins.config.ScriptsConfig;
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
        return check()?"Better Scripting":null;
    }

    @Override
    public String getIconFileName() {
        return check()?"notepad.png":null;
    }

    @Override
    public String getUrlName() {
        return check()?"betterscripting":null;
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

    public String[] runScript(String text) throws IOException {
        // First: save the text
        ScriptsConfig.get().saveScript(activeScript, text);

        GroovyScriptEngine engine = new GroovyScriptEngine(ScriptsConfig.get().getScriptFolderUrl(), this.getClass().getClassLoader());
        CompilerConfiguration scriptConfig = new CompilerConfiguration();

        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);
        engine.setConfig(scriptConfig);

        try {
            Script script = engine.createScript(ScriptsConfig.get().getScriptLocation(activeScript), new Binding());
            script.setProperty("out", writer);
            Object res = script.run();
            writer.println("");
            if(res != null) {
                writer.println("Result: " + res.toString());
            }
        } catch (ResourceException | ScriptException e) {
            System.out.println("Error while running script");
            e.printStackTrace();
            writer.println(e.getMessage());
        }

        return out.toString().split("\n");
    }
    
}
