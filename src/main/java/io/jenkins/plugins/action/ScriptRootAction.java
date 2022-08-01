package io.jenkins.plugins.action;

import java.util.ArrayList;
import java.util.List;

import org.jenkinsci.plugins.workflow.libs.GlobalLibraries;
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration;
import org.springframework.security.access.AccessDeniedException;

import hudson.Extension;
import hudson.model.RootAction;
import jenkins.model.Jenkins;

@Extension
public class ScriptRootAction implements RootAction {

    @Override
    public String getDisplayName() {
        return check()?"Better Scripting":null;
    }

    @Override
    public String getIconFileName() {
        // TODO Auto-generated method stub
        return check()?"notepad.png":null;
    }

    @Override
    public String getUrlName() {
        // TODO Auto-generated method stub
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
    
}
