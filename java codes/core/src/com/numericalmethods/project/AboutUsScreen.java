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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.numericalmethods.project.util.Constants;
import com.numericalmethods.project.util.Util;

import java.util.ArrayList;

/**
 * Created by asus-pc on 12/9/2016.
 */
public class AboutUsScreen implements Screen {
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

    public AboutUsScreen(Screen parent) {
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
        //About us label
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(15);
        Cell pageTitr = table.add().height(Util.multiplyToWorldFactor(100)).colspan(20);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("About us", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(1.1f));
            pageTitr.setActor(label);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(15);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(150));

        table.row();
        table.add().height(Util.multiplyToWorldFactor(800)).colspan(5);
        Cell group = table.add().height(Util.multiplyToWorldFactor(500)).colspan(27).top();
        createGroup(group);
        table.add().height(Util.multiplyToWorldFactor(800)).colspan(4);
//        Cell ta = table.add().height(Util.multiplyToWorldFactor(500)).colspan(6).top();
//        createTa(ta);
        table.add().height(Util.multiplyToWorldFactor(800)).colspan(3);
        Cell instructor = table.add().height(Util.multiplyToWorldFactor(500)).colspan(6).top();
        createInstructor(instructor);
        table.add().height(Util.multiplyToWorldFactor(800)).colspan(5);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(40));

        table.top();
        stage.addActor(scrollPane);
    }

    private void createTa(Cell parent) {
        Table taTable = new Table();
        taTable.top();
//        instructorTable.debug();
        taTable.top();

        for (int i = 0; i < 6; i++) {
            taTable.add().width(Constants.SCREEN_SIZE.x / 50).height(Util.multiplyToWorldFactor(0));
        }

        taTable.row();
        //Instructor label
        Cell taLabel = taTable.add().height(Util.multiplyToWorldFactor(100)).colspan(6);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("TA", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(0.8f));
            taLabel.setActor(label);
        }

        taTable.row();
        //ta
        Cell parmida = taTable.add().height(Util.multiplyToWorldFactor(400)).colspan(6);
        createPerson(parmida, "images/ta.jpg", "Parmida Vahdatnia", "");

        parent.setActor(taTable);
    }

    private void createInstructor(Cell parent) {
        Table instructorTable = new Table();
        instructorTable.top();
//        instructorTable.debug();
        instructorTable.top();

        for (int i = 0; i < 6; i++) {
            instructorTable.add().width(Constants.SCREEN_SIZE.x / 50).height(Util.multiplyToWorldFactor(0));
        }

        instructorTable.row();
        //Instructor label
        Cell instructorLabel = instructorTable.add().height(Util.multiplyToWorldFactor(100)).colspan(6);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Instructor", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(0.8f));
            instructorLabel.setActor(label);
        }

        instructorTable.row();
        //gharib
        Cell gharib = instructorTable.add().height(Util.multiplyToWorldFactor(400)).colspan(6);
        createPerson(gharib, "images/gharib.jpg", "Mohammad Gharib", "");

        parent.setActor(instructorTable);
    }

    private void createGroup(Cell parent) {
        Table groupTable = new Table();
        groupTable.top();
//        groupTable.debug();
        groupTable.top();

        for (int i = 0; i < 27; i++) {
            groupTable.add().width(Constants.SCREEN_SIZE.x / 50).height(Util.multiplyToWorldFactor(0));
        }

        groupTable.row();
        //group label
        groupTable.add().height(Util.multiplyToWorldFactor(100)).colspan(6);
        Cell groupLabel = groupTable.add().height(Util.multiplyToWorldFactor(100)).colspan(15);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Our Team", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(0.8f));
            groupLabel.setActor(label);
        }
        groupTable.add().height(Util.multiplyToWorldFactor(100)).colspan(6);

        groupTable.row();
        //kamyar
        Cell kamyar = groupTable.add().height(Util.multiplyToWorldFactor(600)).colspan(6);
        createPerson(kamyar, "images/kamyar.jpg", "Kamyar Maroufi", "Graphical User\nInterface");
        groupTable.add().height(Util.multiplyToWorldFactor(600)).colspan(1);
        //helia
        Cell helia = groupTable.add().height(Util.multiplyToWorldFactor(600)).colspan(6);
        createPerson(helia, "images/helia.jpg", "Helia Ziaei", "Chapter1\nChapter5");
        groupTable.add().height(Util.multiplyToWorldFactor(600)).colspan(1);
        //mohsen
        Cell mohsen = groupTable.add().height(Util.multiplyToWorldFactor(600)).colspan(6);
        createPerson(mohsen, "images/mohsen.jpg", "Mohsen Ferdosi", "Chapter3\nChapter4");
        groupTable.add().height(Util.multiplyToWorldFactor(600)).colspan(1);
        Cell abolfazl = groupTable.add().height(Util.multiplyToWorldFactor(600)).colspan(6);
        //abolfazl
        createPerson(abolfazl, "images/abolfazl2.jpg", "Abolfazl Asadi", "Chapter2\nChapter6");

        parent.setActor(groupTable);
    }

    private void createPerson(Cell parent, String imagePath, String name, String job) {
        Table personTable = new Table();
//        personTable.debug();
        personTable.top();

        for (int i = 0; i < 6; i++) {
            personTable.add().width(Constants.SCREEN_SIZE.x / 50).height(Util.multiplyToWorldFactor(0));
        }

        personTable.row();
        //image
        Cell imageCell = personTable.add().colspan(6).height(Util.multiplyToWorldFactor(300));
        imageCell.minWidth(imageCell.getColspan() * personTable.getCells().get(0).getPrefWidth());
        Image image = new Image(new Texture(Gdx.files.internal(imagePath)));
        imageCell.setActor(image);
        Vector2 temp = Scaling.fit.apply(image.getWidth(),
                image.getHeight(), imageCell.getColspan() * personTable.getCells().get(0).getPrefWidth(), imageCell.getMaxHeight());
        imageCell.width(temp.x);
        imageCell.height(temp.y);

        personTable.row();
        //name
        Cell nameCell = personTable.add().colspan(6).height(Util.multiplyToWorldFactor(100));
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label(name, labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            label.setAlignment(Align.center);
            nameCell.setActor(label);
        }

        personTable.row();
        //name
        Cell jobCell = personTable.add().colspan(6).height(Util.multiplyToWorldFactor(100));
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label(job, labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            label.setAlignment(Align.center);
            jobCell.setActor(label);
        }

        parent.setActor(personTable);
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
