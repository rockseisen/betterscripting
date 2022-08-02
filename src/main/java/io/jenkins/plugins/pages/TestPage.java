package io.jenkins.plugins.pages;

import hudson.Extension;
import hudson.ExtensionList;
import hudson.model.Describable;
import hudson.model.Descriptor;

public class TestPage implements Describable<TestPage> {

    public String title;

    public TestPage(String title) {
        this.title = title;
    }

    @Override
    public TestPageDescriptor getDescriptor() {
        return TestPageDescriptor.get();
    }

    @Extension
    public static class TestPageDescriptor extends Descriptor<TestPage> {

        public static TestPageDescriptor get() {
            return ExtensionList.lookupSingleton(TestPageDescriptor.class);
        }

    }
    
}
