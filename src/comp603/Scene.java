package comp603;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author archy
 */
// Create abstract Scene class with enter method
public abstract class Scene {
    protected List<String> textQueue = new ArrayList<>();
    protected int queueIndex = 0;

    // Call this to load all your dialogue lines upfront
    protected void loadTextQueue(String... lines) {
        textQueue.clear();
        queueIndex = 0;
        for (String line : lines) {
            textQueue.add(line);
        }
    }

    // Call this to show the next line and return whether there are more
    protected boolean nextLine(GameEngine engine) {
        if (queueIndex < textQueue.size()) {
            engine.window.showText(textQueue.get(queueIndex));
            queueIndex++;
            return queueIndex < textQueue.size(); // true = more lines remain
        }
        return false;
    }

    // Helper to show a continue button
    protected void showContinueButton(GameEngine engine) {
        List<String[]> cont = new ArrayList<>();
        cont.add(new String[]{"Continue →", "continue"});
        engine.window.setChoices(cont);
    }
    
    public abstract void buildUI(GameEngine engine);
    public abstract void onChoice(GameEngine engine, String key);
    

}
