package io.jenkins.plugins.config;

import java.io.File;
import java.util.ArrayList;

import jenkins.model.GlobalConfiguration;

public class ScriptsConfig extends GlobalConfiguration {
    
    private ArrayList<ScriptElement> scriptsList = new ArrayList<>();
    private final String scriptsFolder = "scripts";

    public ScriptsConfig() {
        load();
    }

    public void saveScript(String name, String script) {
        if(getElementByName(name).equals(null)) {
            scriptsList.add(
                new ScriptElement(
                    name,
                    makeFileString(name),
                    new ArrayList<>()
                )
            );
        }
    }

    private String makeFileString(String name) {
        return scriptsFolder + File.separator + name + File.separator + name + ".groovy";
    }

    private ScriptElement getElementByName(String name) {
        for(ScriptElement e : scriptsList) {
            if(e.name.equals(name)) {
                return e;
            }
        }

        return null;
    }


    class ScriptElement {
        public String name;
        public String fileString;
        public ArrayList<String> libraries;

        public ScriptElement(
            String name,
            String fileString,
            ArrayList<String> libraries
        ) {
            this.name = name;
            this.fileString = fileString;
            this.libraries = libraries;
        }
    }

}
