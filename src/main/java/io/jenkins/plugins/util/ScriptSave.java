package io.jenkins.plugins.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ScriptSave {
    
    public String name;
    public ArrayList<String> libraries;
    public final String scriptsFolder = "scripts";

    public ScriptSave(
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
        return new File(getFilePath());
    }

    public String getFilePath() {
        return getFolderPath() + File.separator + getFileName();
    }

    public String getFolderPath() {
        return scriptsFolder + File.separator + name;
    }

    public String getFileName() {
        return name + ".groovy";
    }

    public String getText() {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(getFile()))) 
        {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) 
            {
                contentBuilder.append(sCurrentLine).append("\n");
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            contentBuilder.append(e.getMessage());
        }

        return contentBuilder.toString();
    }
}
