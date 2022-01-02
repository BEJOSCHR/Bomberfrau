/*
 * Menu Animationen
 *
 * Version 1.0
 * Author: Carsten
 *
 * Animationen für Intro und Hauptmenü
 */

package uni.bombenstimmung.de.menu;

import java.util.Random;

import uni.bombenstimmung.de.backend.animation.Animation;
import uni.bombenstimmung.de.backend.animation.AnimationData;
import uni.bombenstimmung.de.backend.console.ConsoleHandler;
import uni.bombenstimmung.de.backend.console.MessageType;
import uni.bombenstimmung.de.backend.graphics.GraphicsHandler;

public class MenuAnimations {

    private static Boolean grow = true;

    /**
     * Animation Intro Bild Zooming
     */
    public static void introAnimation() {
	new Animation(1, 1200) {
	    @Override
	    public void initValues() {
		AnimationData.intro_zoom = 0;
	    }

	    @Override
	    public void changeValues() {
		if (getSteps() % 3 == 0) {
		    if ((getSteps() > 50) && (getSteps() <= 350))
			AnimationData.intro_zoom += 0.01;
		    if ((getSteps() >= 750) && (getSteps() < 1050))
			AnimationData.intro_zoom -= 0.01;
		}
	    }

	    @Override
	    public void finaliseValues() {
		AnimationData.intro_zoom = 0;
		// ConsoleHandler.print("Wechsel vom Intro zu Menü am Ende der Animation",
		// MessageType.MENU);
		GraphicsHandler.switchToMenuFromIntro();
	    }
	};

    }

    /**
     * Animation Intro Text "click to skip"
     */
    public static void introTextAni() {
	new Animation(60, -1) {
	    @Override
	    public void initValues() {
		AnimationData.intro_skip_text = 0;
	    }

	    @Override
	    public void changeValues() {
		if (getSteps() % 2 == 0) {
		    AnimationData.intro_skip_text = 3;
		} else {
		    AnimationData.intro_skip_text = 0;
		}
	    }

	    @Override
	    public void finaliseValues() {
		AnimationData.intro_skip_text = 0;
		GraphicsHandler.switchToMenuFromIntro();
	    }
	};

    }

    /**
     * Animation Title Text Pulsing
     */
    public static void titlePulseAni() {

	ConsoleHandler.print("Starting Animation Title Text Pulsing", MessageType.MENU);
	new Animation(1, -1) {
	    @Override
	    public void initValues() {
		AnimationData.title_Modifier = 0;
	    }

	    @Override
	    public void changeValues() {
//		if (getSteps() % 15 == 0)
//		    ConsoleHandler.print("AnimationData.title_Modifier = " + AnimationData.title_Modifier,
//			    MessageType.MENU);
		if (AnimationData.title_Modifier >= 30)
		    grow = false;
		if (AnimationData.title_Modifier == 0)
		    grow = true;
		if (getSteps() % 2 == 0) {
		    if (grow)
			AnimationData.title_Modifier += 1;
		    else
			AnimationData.title_Modifier -= 1;
		}
	    }

	    @Override
	    public void finaliseValues() {
		AnimationData.title_Modifier = 0;
	    }
	};

    }

    /**
     * Animation Title Text Shaking
     */
    public static void titleShakeAni() {
	Random r = new Random();
	new Animation(10, -1) {
	    @Override
	    public void initValues() {
		AnimationData.title_posXModifier = 0;
		AnimationData.title_posYModifier = 0;
	    }

	    @Override
	    public void changeValues() {
		if (getSteps() % 2 == 0) {
		    int stepSize = 1;
		    AnimationData.title_posXModifier += r.nextInt(stepSize * 2) - stepSize;
		    AnimationData.title_posYModifier += r.nextInt(stepSize * 2) - stepSize;
		    int limit = 10;
		    if (AnimationData.title_posXModifier < -limit) {
			AnimationData.title_posXModifier = -limit;
		    } else if (AnimationData.title_posXModifier > limit) {
			AnimationData.title_posXModifier = limit;
		    }
		    if (AnimationData.title_posYModifier < -limit) {
			AnimationData.title_posYModifier = -limit;
		    } else if (AnimationData.title_posYModifier > limit) {
			AnimationData.title_posYModifier = limit;
		    }
		} else {
		    AnimationData.title_posXModifier = 0;
		    AnimationData.title_posYModifier = 0;
		}
	    }

	    @Override
	    public void finaliseValues() {
		AnimationData.title_posXModifier = 0;
		AnimationData.title_posYModifier = 0;
	    }
	};

    }
}