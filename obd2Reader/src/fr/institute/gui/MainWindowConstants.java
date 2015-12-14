package fr.institute.gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public interface MainWindowConstants {

	public static final Color DARK_GREEN = new Color(50,100,50);
	public static final Color TEMPERED_GREEN = new Color(70,150,70);
	public static final Color LIGHT_GREEN = new Color(75,255,75);
	public static final int CARET_BLINK_RATE = 500;
	public static final Border NEUTRAL_DARK_GRAY_BORDER = BorderFactory.createMatteBorder(1, 1, 1, 1,Color.DARK_GRAY);
	public static final Border NEUTRAL_GREEN_BORDER = BorderFactory.createMatteBorder(1, 1, 1, 1,Color.GREEN);
	public static final String APPLICATION_NAME = "Institute",
							   MENU_FILE = "file",
							   MENU_CONNECT = "connect",
							   MENU_DISCONNECT = "disconnect",
							   MENU_OPTIONS = "options",
							   MENU_FILE_RECORD = "record...",
							   MENU_OPTION_LANGUAGE = "language",
							   GLOBAL_FONT_FILE_PATH = "src/fr/institute/gui/ShareTechMono_Regular.ttf",
							   GLOBAL_FONT_NAME = "Share Tech Mono";
	}
