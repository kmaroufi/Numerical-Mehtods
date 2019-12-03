package com.numericalmethods.project.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.numericalmethods.project.Project;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();


		config.height = (int) (java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 1);
		config.width = (int) (java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth()  / 1);
		new LwjglApplication(new Project(), config);
	}
}
