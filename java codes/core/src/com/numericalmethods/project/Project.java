package com.numericalmethods.project;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.mathworks.engine.MatlabEngine;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Project extends Game {
	private Music music;

	@Override
	public void create() {
		setScreen(new MainMenuScreen());
		startsMatlabSession();
		music = Gdx.audio.newMusic(Gdx.files.internal("music/music1.mp3"));
		music.setLooping(true);
		Preferences preferences = Gdx.app.getPreferences("NumericalMethodsProject");
		if (preferences.getString("music").equals("On")) {
			music.play();
		}
	}
	private Future<MatlabEngine> engFuture;
	private MatlabEngine engine;

	public void startsMatlabSession() {
		engFuture = MatlabEngine.connectMatlabAsync();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String path = Gdx.files.getLocalStoragePath();
					Gdx.app.log("SS", path);
					path = path.replace('\\', '/');
					Gdx.app.log("engine", "waiting");
					engine = engFuture.get();
					engine.eval("addpath('C:/Users/asus-pc/Documents/mohasebat/1')");
					engine.eval("addpath('C:/Users/asus-pc/Documents/mohasebat/2')");
					engine.eval("addpath('C:/Users/asus-pc/Documents/mohasebat/3')");
					engine.eval("addpath('C:/Users/asus-pc/Documents/mohasebat/4')");
					engine.eval("addpath('C:/Users/asus-pc/Documents/mohasebat/5/first')");
					engine.eval("addpath('C:/Users/asus-pc/Documents/mohasebat/5/second')");
					engine.eval("addpath('C:/Users/asus-pc/Documents/mohasebat/6')");
					engine.eval("addpath('C:/Users/asus-pc/Documents/mohasebat/6/inner')");
//					engine.eval("addpath('"+  path + "mohasebat/1" + "')");
//					engine.eval("addpath('"+  path + "mohasebat/2" + "')");
//					engine.eval("addpath('"+  path + "mohasebat/3" + "')");
//					engine.eval("addpath('"+  path + "mohasebat/4" + "')");
//					engine.eval("addpath('"+  path + "mohasebat/5/first" + "')");
//					engine.eval("addpath('"+  path + "mohasebat/5/second" + "')");
//					engine.eval("addpath('"+  path + "mohasebat/6" + "')");
//					engine.eval("addpath('"+  path + "mohasebat/6/inner" + "')");
					Gdx.app.log("engine", "ready");
				} catch (InterruptedException e) {
					e.printStackTrace();
					startsMatlabSession();
				} catch (ExecutionException e) {
					e.printStackTrace();
					startsMatlabSession();
				}
			}
		}).start();
	}

	public Future<MatlabEngine> getEngFuture() {
		return engFuture;
	}

	public MatlabEngine getEngine() {
		return engine;
	}

	public Music getMusic() {
		return music;
	}
}
