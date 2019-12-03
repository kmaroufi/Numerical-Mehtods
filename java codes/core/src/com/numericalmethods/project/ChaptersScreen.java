package com.numericalmethods.project;

/**
 * Created by asus-pc on 12/3/2016.
 */

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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.numericalmethods.project.util.Constants;
import com.numericalmethods.project.util.Util;

import java.util.ArrayList;

/**
 * Created by asus-pc on 12/1/2016.
 */
public class ChaptersScreen implements Screen {
    private SpriteBatch batch;
    private Viewport viewport;
    private Stage stage;
    private Table table;
    private BitmapFont font;
    private ScrollPane scrollPane;
    private Screen parent;

    private ArrayList<Cell> tableCells = new ArrayList<Cell>();
    private ArrayList<Cell> buttonsCells = new ArrayList<Cell>();
    private ArrayList<Button> buttons = new ArrayList<Button>();
    private ArrayList<Table> descriptionTables = new ArrayList<Table>();

    public ChaptersScreen(Screen parent) {
        this.parent = parent;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        viewport = new ScreenViewport();
        font = new BitmapFont(Gdx.files.internal("LittleTitr.fnt"));
        font.getData().setScale(.3f);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.setColor(Color.BLACK);
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        initializeMenu();
    }

    private void initializeMenu() {
        table = new Table();
//        table.debug();
//        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("background4.jpg")))));
        Texture background = new Texture(Gdx.files.internal("background4.jpg"));
        TextureRegion backgroundRegion = new TextureRegion(background, (int)(0.85f*background.getWidth()), (int)(0.85f*background.getHeight()));
        table.setBackground(new TextureRegionDrawable(backgroundRegion));

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.vScrollKnob = new TextureRegionDrawable(new TextureRegion(new Texture("vScrollKnob2.png")));
        scrollPaneStyle.vScroll = new TextureRegionDrawable(new TextureRegion(new Texture("vScroll.png")));
        scrollPaneStyle.hScrollKnob = new TextureRegionDrawable(new TextureRegion(new Texture("hScrollKnob2.png")));
        scrollPaneStyle.hScroll = new TextureRegionDrawable(new TextureRegion(new Texture("hScroll.png")));
        scrollPane = new ScrollPane(table, scrollPaneStyle);
        scrollPane.setFadeScrollBars(true);
        scrollPane.setOverscroll(false, false);
        scrollPane.setFlickScroll(false);

        for (int i = 0; i < 50; i++) {
            tableCells.add(table.add().width(Constants.SCREEN_SIZE.x / 50).height(Util.multiplyToWorldFactor(40)).
                    padBottom(Util.multiplyToWorldFactor(10)));
        }

        table.row();
        table.add().height(Util.multiplyToWorldFactor(80)).colspan(1);
        //return to main menu
        Cell returnCell = table.add().height(Util.multiplyToWorldFactor(80)).colspan(9);
        returnCell.left();
        {
            TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
            textButtonStyle.font  = font;
            textButtonStyle.fontColor = Color.BLACK;
            textButtonStyle.down = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/down.png")), 35,35,35,35));
            textButtonStyle.up = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/up.png")), 35,35,35,35));
            textButtonStyle.over = textButtonStyle.down;
            TextButton textButton = new TextButton("return", textButtonStyle);
            textButton.getLabel().setFontScale(.8f * Util.multiplyToWorldFactor(1));
            textButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
                }
            });
            returnCell.setActor(textButton);
        }

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(10));

        table.row();
        //choose your chapter label
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(15);
        Cell pageTitr = table.add().height(Util.multiplyToWorldFactor(100)).colspan(20);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Click on your desired chapter!", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(1.1f));
            pageTitr.setActor(label);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(15);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(20));

        table.row();
        //chapter1
        table.add().height(Util.multiplyToWorldFactor(150)).colspan(8);
        Cell chapter1 = table.add().height(Util.multiplyToWorldFactor(150)).colspan(34);
        Button chapter1Button = createChapterButton(chapter1, "chapters_logo/chapter1.jpg", "Chapter1",
                "Calculating absolute and relative error for your desired equation.");
        chapter1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new Chapter1Screen(ChaptersScreen.this));
            }
        });
        chapter1.setActor(chapter1Button);
        table.add().height(Util.multiplyToWorldFactor(150)).colspan(8);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(20));

        table.row();
        //chapter2
        table.add().height(Util.multiplyToWorldFactor(150)).colspan(8);
        Cell chapter2 = table.add().height(Util.multiplyToWorldFactor(150)).colspan(34);
        Button chapter2Button = createChapterButton(chapter2, "chapters_logo/chapter2.png", "Chapter2",
                "Numerical Methods for solving nonlinear equations.");
        chapter2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new Chapter2Screen(ChaptersScreen.this));
            }
        });
        chapter2.setActor(chapter2Button);
        table.add().height(Util.multiplyToWorldFactor(150)).colspan(8);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(20));

        table.row();
        //chapter3
        table.add().height(Util.multiplyToWorldFactor(150)).colspan(8);
        Cell chapter3 = table.add().height(Util.multiplyToWorldFactor(150)).colspan(34);
        Button chapter3Button = createChapterButton(chapter3, "chapters_logo/chapter3-2small2.png", "Chapter3",
                "Interpolation, Extrapolation and Curve Fitting.");
        chapter3.setActor(chapter3Button);
        chapter3Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new Chapter3Screen(ChaptersScreen.this));
            }
        });
        table.add().height(Util.multiplyToWorldFactor(150)).colspan(8);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(20));

        table.row();
        //chapter4
        table.add().height(Util.multiplyToWorldFactor(150)).colspan(8);
        Cell chapter4 = table.add().height(Util.multiplyToWorldFactor(150)).colspan(34);
        Button chapter4Button = createChapterButton(chapter4, "chapters_logo/chapter4-small2.png", "Chapter4",
                "Numerical Integration and Differentiation.");
        chapter4.setActor(chapter4Button);
        chapter4Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new Chapter4Screen(ChaptersScreen.this));
            }
        });
        table.add().height(Util.multiplyToWorldFactor(150)).colspan(8);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(20));

        table.row();
        //chapter5
        table.add().height(Util.multiplyToWorldFactor(150)).colspan(8);
        Cell chapter5 = table.add().height(Util.multiplyToWorldFactor(150)).colspan(34);
        Button chapter5Button = createChapterButton(chapter5, "chapters_logo/chapter5.png", "Chapter5",
                "Ordinary Differential Equations.");
        chapter5.setActor(chapter5Button);
        chapter5Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new Chapter5Screen(ChaptersScreen.this));
            }
        });
        table.add().height(Util.multiplyToWorldFactor(150)).colspan(8);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(20));

        table.row();
        //chapter6
        table.add().height(Util.multiplyToWorldFactor(150)).colspan(8);
        Cell chapter6 = table.add().height(Util.multiplyToWorldFactor(150)).colspan(34);
        Button chapter6Button = createChapterButton(chapter6, "chapters_logo/chapter6.png", "Chapter6",
                "Systems of Linear Equations.");
        chapter6.setActor(chapter6Button);
        chapter6Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new Chapter6Screen(ChaptersScreen.this));
            }
        });
        table.add().height(Util.multiplyToWorldFactor(150)).colspan(8);

        table.top();
        stage.addActor(scrollPane);
    }

    private Button createChapterButton (Cell parent, String chapterImagePath, String chapterName, String descriptionString) {
        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
        buttonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("vScrollKnob2.png"))));
        buttonStyle.over = buttonStyle.down;
        Button button = new Button(buttonStyle);
        buttons.add(button);
//        button.debug();
        button.top();

        for (int i = 0; i < 34; i++) {
            buttonsCells.add(button.add().width(Constants.SCREEN_SIZE.x / 50).height(Util.multiplyToWorldFactor(0)));
        }

        button.row();
//        chapter logo
        Cell logo = button.add().colspan(4).height(parent.getPrefHeight());
        logo.minWidth(logo.getColspan() * button.getCells().get(0).getPrefWidth());
        Image image = new Image(new Texture(Gdx.files.internal(chapterImagePath)));
        logo.setActor(image);
        Vector2 temp = Scaling.fit.apply(image.getWidth(),
                image.getHeight(), logo.getColspan() * button.getCells().get(0).getPrefWidth(), logo.getMaxHeight());
        logo.width(temp.x);
        logo.height(temp.y);

        Cell chapterDescription = button.add().colspan(30).height(parent.getPrefHeight());
        {
            Table descriptionTable = new Table();
            descriptionTables.add(descriptionTable);
//            descriptionTable.debug();
            descriptionTable.top();

            for (int i = 0; i < 30; i++) {
                buttonsCells.add(descriptionTable.add().width(Constants.SCREEN_SIZE.x / 50).height(Util.multiplyToWorldFactor(0)));
            }

            descriptionTable.row();
            // chapter's name
            Cell name = descriptionTable.add().colspan(30).height(chapterDescription.getPrefHeight() * 11 / 16);
            name.left();
            {
                Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
                Label label = new Label(chapterName, labelStyle);
                label.setFontScale(Util.multiplyToWorldFactor(1));
                name.setActor(label);
            }

            descriptionTable.row();
            // chapter's explanation
            Cell description = descriptionTable.add().colspan(30).height(chapterDescription.getPrefHeight() * 5 / 16);
            description.left();
            {
                Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
                Label label = new Label(descriptionString, labelStyle);
                label.setFontScale(Util.multiplyToWorldFactor(.5f));
                description.setActor(label);
            }

            chapterDescription.setActor(descriptionTable);
        }

        return button;
    }

    @Override
    public void render(float delta) {
        Color color = Color.LIGHT_GRAY;
        Gdx.gl.glClearColor(color.r, color.b, color.g, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        viewport.setScreenBounds(0, 0 , Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport.apply(true);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
//        viewport.getCamera().position.set(Constants.SCREEN_SIZE.x / 2 , Constants.SCREEN_SIZE.y / 2, 0);
        scrollPane.setBounds(0,0,width,height);
        Float newWidth = width / 50f;
        Float buttonWidth = 0f;
//        for (Cell tableCell : tableCells) {
//            tableCell.width(newWidth);
//        }
//        table.setWidth(newWidth*50);
//        for (int i = 8; i < 44; i++) {
//            buttonWidth += tableCells.get(i).getPrefWidth();
//        }
//        for (Button button : buttons) {
//            button.setWidth(buttonWidth);
//        }
//        for (Cell buttonsCell : buttonsCells) {
//            buttonsCell.width(newWidth);
//        }
//        for (Table descriptionTable : descriptionTables) {
//            descriptionTable.setWidth(newWidth * 30);
//        }
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
