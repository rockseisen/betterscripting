package io.jenkins.plugins.config;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import hudson.Extension;
import hudson.ExtensionList;
import jenkins.model.GlobalConfiguration;

@Extension
public class ScriptsConfig extends GlobalConfiguration {
    
    private ArrayList<ScriptElement> scriptsList = new ArrayList<>();
    private final String scriptsFolder = "scripts";

    public ScriptsConfig() {
        load();
    }

    public static ScriptsConfig get() {
        return ExtensionList.lookupSingleton(ScriptsConfig.class);
    }

    public void saveScript(String name, String script) throws IOException {
        if(getElementByName(name) == null) {
            scriptsList.add(
                new ScriptElement(
                    name,
                    new ArrayList<>()
                )
            );
        }
        PrintWriter writer = new PrintWriter(getElementByName(name).getFile());
        writer.write(script);
        writer.close();
    }

    

    private ScriptElement getElementByName(String name) {
        for(ScriptElement e : scriptsList) {
            if(e.name.equals(name)) {
                return e;
            }
        }

        return null;
    }

    public String getScriptLocation(String name) {
        return getElementByName(name).getScriptInFolder();
    }

    public String getScriptFolderUrl() {
        return scriptsFolder;
    }


    class ScriptElement {
        public String name;
        public ArrayList<String> libraries;

        public ScriptElement(
            String name,
            ArrayList<String> libraries
        ) throws IOException {
            this.name = name;
            this.libraries = libraries;
            
            File file = new File(scriptsFolder + File.separator + name + File.separator + name + ".groovy");
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        public File getFile() {
            return new File(makeFileString());
        }

        public String makeFileString() {
            return scriptsFolder + File.separator + getScriptInFolder();
        }

        public String getScriptInFolder() {
            return name + File.separator + name + ".groovy";
        }
    }

}
