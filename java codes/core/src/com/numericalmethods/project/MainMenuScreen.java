package com.numericalmethods.project;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.numericalmethods.project.util.Constants;
import com.numericalmethods.project.util.Util;

import java.util.ArrayList;

/**
 * Created by asus-pc on 12/1/2016.
 */
public class MainMenuScreen implements Screen {
    private SpriteBatch batch;
    private Viewport viewport;
    private Stage stage;
    private Table table;
    private BitmapFont font;
    private ScrollPane scrollPane;

    private ArrayList<Cell> tableCells = new ArrayList<Cell>();
    private ArrayList<Cell> menuTableCells = new ArrayList<Cell>();
    private Table menuTable;

    private TextButton seeChapters;

    @Override
    public void show() {

//        FileHandle fontFile = Gdx.files.internal("calibri.ttf");
//        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
//        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        parameter.size = 10;
//        font = generator.generateFont(parameter);
//        generator.dispose();

        batch = new SpriteBatch();
//        viewport = new ExtendViewport(Constants.SCREEN_SIZE.x, Constants.SCREEN_SIZE.y);
        viewport = new ScreenViewport();
        font = new BitmapFont(Gdx.files.internal("LittleTitr.fnt"));
        font.getData().setScale(0.6f);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.setColor(Color.BLACK);
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        initializeMenu();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (((Project) Gdx.app.getApplicationListener()).getEngine() == null) {
                    try {
                        if (seeChapters != null) {
                            seeChapters.setDisabled(true);
                        }
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (seeChapters != null) {
                    seeChapters.setText("See Chapters");
                    seeChapters.setDisabled(false);
                }
            }
        }).start();
    }

    private void initializeMenu() {
        table = new Table();
//        table.debug();
//        TextureRegionDrawable background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("background4.jpg"))));
//        table.setBackground(background);
        Texture background = new Texture(Gdx.files.internal("background4.jpg"));
        TextureRegion backgroundRegion = new TextureRegion(background, (int)(0.9f*background.getWidth()), (int)(0.9f*background.getHeight()));
        table.setBackground(new TextureRegionDrawable(backgroundRegion));

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
//        scrollPaneStyle.vScrollKnob = new TextureRegionDrawable(new TextureRegion(new Texture("vScrollKnob2.png")));
//        scrollPaneStyle.vScroll = new TextureRegionDrawable(new TextureRegion(new Texture("vScroll.png")));
//        scrollPaneStyle.hScrollKnob = new TextureRegionDrawable(new TextureRegion(new Texture("hScrollKnob2.png")));
//        scrollPaneStyle.hScroll = new TextureRegionDrawable(new TextureRegion(new Texture("hScroll.png")));
        scrollPane = new ScrollPane(table, scrollPaneStyle);
        scrollPane.setFadeScrollBars(true);
        scrollPane.setOverscroll(false, false);
//        scrollPane.setFlickScroll(false);

        for (int i = 0; i < 50; i++) {
            tableCells.add(table.add().height(0).padBottom(0).width(Constants.SCREEN_SIZE.x / 50));
        }

        table.row();
        //menu
        table.add().colspan(1).height(0.9f*Constants.SCREEN_SIZE.y).minWidth(Util.multiplyToWorldFactor(30));
        Cell menu = table.add().colspan(19).height(0.9f*Constants.SCREEN_SIZE.y);
        {
            menuTable = new Table();
//            menuTable.debug();
            menuTable.top();

            for (int i = 0; i < 19; i++) {
                menuTableCells.add(menuTable.add().height(Util.multiplyToWorldFactor(30)).padBottom(10).width(Constants.SCREEN_SIZE.x / 50));
            }

            menuTable.row();
            //padding
            menuTable.add().colspan(19).height(Util.multiplyToWorldFactor(100));

            menuTable.row();
            //Welcome lable
            Cell welcome = menuTable.add().height(Util.multiplyToWorldFactor(250)).colspan(19);
            {
                Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
                Label label = new Label(" Welcome to Numerical\nMethods course project!", labelStyle);
                label.setFontScale(Util.multiplyToWorldFactor(1));
                welcome.setActor(label);

            }

            menuTable.row();
            //padding
            menuTable.add().colspan(19).height(Util.multiplyToWorldFactor(200));

            menuTable.row();
            //chapterScreen's button
            menuTable.add().height(Util.multiplyToWorldFactor(150)).colspan(5);
            Cell chapterScreen  = menuTable.add().height(Util.multiplyToWorldFactor(150)).colspan(9);
            chapterScreen.width(chapterScreen.getColspan() * menuTable.getCells().get(0).getPrefWidth());
            {
                TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
                textButtonStyle.font  = font;
                textButtonStyle.fontColor = Color.BLACK;
                textButtonStyle.down = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/down.png")), 42,42,42,42));
                textButtonStyle.up = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/up.png")), 42,42,42,42));
                textButtonStyle.over = textButtonStyle.down;
//                textButtonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("vScrollKnob2.png"))));
                seeChapters = new TextButton(" See Chapters \n(please wait)", textButtonStyle);
                seeChapters.getLabel().setFontScale(.8f * Util.multiplyToWorldFactor(1));
                seeChapters.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (seeChapters.isChecked()) {
                            ((Game) Gdx.app.getApplicationListener()).setScreen(new ChaptersScreen(MainMenuScreen.this));
                        }
                    }
                });
                chapterScreen.setActor(seeChapters);
            }
            menuTable.add().height(Util.multiplyToWorldFactor(150)).colspan(5);

            menuTable.row();
            //padding
            menuTable.add().colspan(19).height(Util.multiplyToWorldFactor(20));

            menuTable.row();
            //setting button
            menuTable.add().height(Util.multiplyToWorldFactor(80)).colspan(5);
            Cell setting  = menuTable.add().height(Util.multiplyToWorldFactor(80)).colspan(9);
            setting.width(setting.getColspan() * menuTable.getCells().get(0).getPrefWidth());
            {
                TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
                textButtonStyle.font  = font;
                textButtonStyle.fontColor = Color.BLACK;
                textButtonStyle.down = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/down.png")), 42,42,42,42));
                textButtonStyle.up = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/up.png")), 42,42,42,42));
                textButtonStyle.over = textButtonStyle.down;
                TextButton textButton = new TextButton("Setting", textButtonStyle);
                textButton.getLabel().setFontScale(.8f * Util.multiplyToWorldFactor(1));
                textButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new SettingScreen(MainMenuScreen.this));
                    }
                });
                setting.setActor(textButton);
            }
            menuTable.add().height(Util.multiplyToWorldFactor(80)).colspan(5);

            menuTable.row();
            //padding
            menuTable.add().colspan(19).height(Util.multiplyToWorldFactor(20));

            menuTable.row();
            //about us button
            menuTable.add().height(Util.multiplyToWorldFactor(80)).colspan(5);
            Cell aboutUs  = menuTable.add().height(Util.multiplyToWorldFactor(80)).colspan(9);
            aboutUs.width(aboutUs.getColspan() * menuTable.getCells().get(0).getPrefWidth());
            {
                TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
                textButtonStyle.font  = font;
                textButtonStyle.fontColor = Color.BLACK;
                textButtonStyle.down = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/down.png")), 42,42,42,42));
                textButtonStyle.up = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/up.png")), 42,42,42,42));
                textButtonStyle.over = textButtonStyle.down;
                TextButton textButton = new TextButton("About us", textButtonStyle);
                textButton.getLabel().setFontScale(.8f * Util.multiplyToWorldFactor(1));
                textButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new AboutUsScreen(MainMenuScreen.this));
                    }
                });
                aboutUs.setActor(textButton);
            }
            menuTable.add().height(Util.multiplyToWorldFactor(80)).colspan(5);

            menuTable.row();
            //padding
            menuTable.add().colspan(19).height(Util.multiplyToWorldFactor(40));

            menuTable.row();
            //exit button
            menuTable.add().height(Util.multiplyToWorldFactor(80)).colspan(5);
            Cell exit  = menuTable.add().height(Util.multiplyToWorldFactor(80)).colspan(9);
            exit.width(exit.getColspan() * menuTable.getCells().get(0).getPrefWidth());
            {
                TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
                textButtonStyle.font  = font;
                textButtonStyle.fontColor = Color.BLACK;
                textButtonStyle.down = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/down.png")), 42,42,42,42));
                textButtonStyle.up = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/up.png")), 42,42,42,42));
                textButtonStyle.over = textButtonStyle.down;
                TextButton textButton = new TextButton("Exit", textButtonStyle);
                textButton.getLabel().setFontScale(.8f * Util.multiplyToWorldFactor(1));
                textButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Gdx.app.exit();
                    }
                });
                exit.setActor(textButton);
            }
            menuTable.add().height(Util.multiplyToWorldFactor(80)).colspan(5);

            menu.setActor(menuTable);
        }
        table.add().colspan(1).height(0.9f*Constants.SCREEN_SIZE.y).minWidth(Util.multiplyToWorldFactor(30));
        //numerical methods project label
        Cell imageCell = table.add().colspan(28);
        Image image = new Image(new Texture(Gdx.files.internal("1.png")));
        imageCell.setActor(image);
        Float aspectRatio = image.getHeight() / image.getWidth();
        imageCell.width(imageCell.getColspan() * tableCells.get(45).getPrefWidth());
        imageCell.height(imageCell.getPrefWidth() * aspectRatio);
        table.add().colspan(1).height(0.9f*Constants.SCREEN_SIZE.y).minWidth(Util.multiplyToWorldFactor(30));

        table.top();
        stage.addActor(scrollPane);
        scrollPane.setForceScroll(true, true);
    }

    @Override
    public void render(float delta) {
        Color color = Color.WHITE;
        Gdx.gl.glClearColor(color.r, color.b, color.g, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        viewport.setScreenBounds(0, 0 , Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport.apply(true);

        stage.act(delta);
        stage.draw();

        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
//        font.draw(batch, "Welcome!", 100, 100);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
//        viewport.getCamera().position.set(width / 2 , height / 2, 0);
//        Float newWidth = width / 50f;
//        Gdx.app.log("newWidth", String.valueOf(newWidth));
//        for (Cell menuTableCell : menuTableCells) {
//            menuTableCell.width(newWidth);
//        }
//        menuTable.setWidth(newWidth*19);
//        for (Cell tableCell : tableCells) {
//            tableCell.width(newWidth);
//        }
//        table.setWidth(newWidth*50);
        scrollPane.setBounds(0,0,width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        stage.dispose();
    }
}
