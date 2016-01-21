package template;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import summoner.Summoner;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by Paul on 19.01.2016.
 */
public class TemplateObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private transient StringProperty text;

    public TemplateObject(String name){
        this.name = name;
        this.text = new SimpleStringProperty();
    }

    public static TemplateObject LevelTemplate(){
        TemplateObject levelTemplate = new TemplateObject("Level");
        levelTemplate.setText("@level");
        return levelTemplate;
    }
    public static TemplateObject DivisionTemplate(){
        TemplateObject divisionTemplate = new TemplateObject("Division");
        divisionTemplate.setText("@division - @lp LP");
        return divisionTemplate;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text.get();
    }

    public StringProperty textProperty() {
        return text;
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public String getOutputText(Summoner summoner){
        if(text.get().length()==0) return "";
        String str = text.get().replace("@lp",""+summoner.getLP()).replace("@level",""+summoner.getLevel())
                .replace("@division",summoner.getDivision()).replace("@name",summoner.getName());
        return str;
    }

    @Override
    public String toString() {
        return name;
    }
    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        out.defaultWriteObject();
        out.writeObject(text.get());
    }
    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException{
        in.defaultReadObject();
        String str = (String)in.readObject();
        text = new SimpleStringProperty(str);
    }
}
