package io.jenkins.plugins.config;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import hudson.Extension;
import hudson.ExtensionList;
import io.jenkins.plugins.util.ScriptSave;
import jenkins.model.GlobalConfiguration;

@Extension
public class ScriptsConfig extends GlobalConfiguration {
    
    private ArrayList<ScriptSave> scriptsList = new ArrayList<>();

    public ScriptsConfig() {
        load();
    }

    public static ScriptsConfig get() {
        return ExtensionList.lookupSingleton(ScriptsConfig.class);
    }

    public void saveScript(String name, String script) throws IOException {
        if(getScriptSaveByName(name) == null) {
            scriptsList.add(
                new ScriptSave(
                    name,
                    new ArrayList<>()
                )
            );
        }
        PrintWriter writer = new PrintWriter(getScriptSaveByName(name).getFile());
        writer.write(script);
        writer.close();
        save();
    }

    public ScriptSave getScriptSaveByName(String name) {
        for(ScriptSave e : scriptsList) {
            if(e.name.equals(name)) {
                return e;
            }
        }

        return null;
    }


    

}
