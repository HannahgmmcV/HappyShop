/*
 * Author: Hannah Virgo
 * Changes: Created a new class, within the Utility Package.
 * This is because it allows multiple Views for HappyShop to use it. and therefore you can call different sounds as well.
 */
package ci553.happyshop.utility;

import javafx.scene.media.AudioClip;

public class ButtonSounds {
    public static void play(String path) {
        AudioClip clickSound = new AudioClip(ButtonSounds.class.getResource(path).toExternalForm());
        clickSound.play();
    }
}
