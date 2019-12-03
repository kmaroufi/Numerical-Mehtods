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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.numericalmethods.project.util.Constants;
import com.numericalmethods.project.util.Util;

import java.util.ArrayList;

/**
 * Created by asus-pc on 12/7/2016.
 */
public class Chapter3GetPointsScreen implements Screen {
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

    private String function;
    private Integer numberOfPoints;

    private TextField functionTextField;
    private TextField numberOfPointsTextField;
    private SelectBox selectBox;
    private ArrayList<TextField> pointxArray = new ArrayList<TextField>();
    private ArrayList<TextField> pointyArray = new ArrayList<TextField>();

    public Chapter3GetPointsScreen(Screen parent, String function, Integer numberOfPoints) {
        this.parent = parent;
        this.function = function;
        this.numberOfPoints = numberOfPoints;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        viewport = new ScreenViewport();
        font = new BitmapFont(Gdx.files.internal("LittleTitr.fnt"));
        font.getData().setScale(.6f);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.setColor(Color.BLACK);
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.vScrollKnob = new TextureRegionDrawable(new TextureRegion(new Texture("vScrollKnob2.png")));
        scrollPaneStyle.vScroll = new TextureRegionDrawable(new TextureRegion(new Texture("vScroll.png")));
        scrollPaneStyle.hScrollKnob = new TextureRegionDrawable(new TextureRegion(new Texture("hScrollKnob2.png")));
        scrollPaneStyle.hScroll = new TextureRegionDrawable(new TextureRegion(new Texture("hScroll.png")));
        scrollPane = new ScrollPane(table, scrollPaneStyle);
        scrollPane.setFadeScrollBars(true);
        scrollPane.setOverscroll(false, false);
        scrollPane.setFlickScroll(false);

        stage.addActor(scrollPane);

        initializeMenu();
    }

    private void initializeMenu() {
        table = new Table();
//        table.debug();
//        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("background4.jpg")))));
        Texture background = new Texture(Gdx.files.internal("background4.jpg"));
        TextureRegion backgroundRegion = new TextureRegion(background, (int)(0.85f*background.getWidth()), (int)(0.85f*background.getHeight()));
        table.setBackground(new TextureRegionDrawable(backgroundRegion));

        scrollPane.setWidget(table);

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
                    ((Game) Gdx.app.getApplicationListener()).setScreen(parent);
                }
            });
            returnCell.setActor(textButton);
        }

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(10));

        table.row();
        //Titr label
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(15);
        Cell pageTitr = table.add().height(Util.multiplyToWorldFactor(100)).colspan(20);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Chapter3", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(1.1f));
            pageTitr.setActor(label);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(15);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(20));

        table.row();
        //function
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(3);
        Cell functionLabel = table.add().height(Util.multiplyToWorldFactor(100)).colspan(7).right();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Enter your function :  ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            functionLabel.setActor(label);
        }
        Cell functionTextCell =  table.add().height(Util.multiplyToWorldFactor(70)).colspan(17).fillX();
        {
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.font = font;
            textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("hScroll.png"))));
            textFieldStyle.fontColor = Color.BLACK;
            textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
            functionTextField = new TextField(function, textFieldStyle);
            functionTextField.setDisabled(true);
            functionTextCell.setActor(functionTextField);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(1);

        //number of points
        Cell numberOfPointsLabel = table.add().height(Util.multiplyToWorldFactor(100)).colspan(10).right();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Enter number of points :  ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            numberOfPointsLabel.setActor(label);
        }
        Cell numberOfPointsTextCell =  table.add().height(Util.multiplyToWorldFactor(70)).colspan(4).fillX();
        {
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.font = font;
            textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("hScroll.png"))));
            textFieldStyle.fontColor = Color.BLACK;
            textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
            numberOfPointsTextField = new TextField(String.valueOf(numberOfPoints), textFieldStyle);
            numberOfPointsTextField.setDisabled(true);
            numberOfPointsTextCell.setActor(numberOfPointsTextField);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(7);

        for (int i = 0; i < numberOfPoints; i++) {
            table.row();
            //padding
            table.add().colspan(0).height(Util.multiplyToWorldFactor(20));
            createFieldsOfPoints(i + 1);
        }

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(50));

        table.row();
        //choose
        table.add().height(Util.multiplyToWorldFactor(200)).colspan(12);
        Cell choose = table.add().height(Util.multiplyToWorldFactor(200)).colspan(10).right();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Choose  :  ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.7f));
            choose.setActor(label);
        }
        Cell choosingCell =  table.add().height(Util.multiplyToWorldFactor(50)).colspan(7);
        {
            List.ListStyle listStyle = new List.ListStyle();
            listStyle.font = font;
            listStyle.fontColorSelected = Color.BLACK;
            listStyle.fontColorUnselected = Color.BLACK;
            listStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture("hScrollKnob3.png")));
            listStyle.selection = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
//            listStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture("white.png")));
            List list = new List(listStyle);
            list.setItems(new String[]{"Bisection", "False position", "Secant", "Newton-Raphson", "Fixed point", "Generalized Newton-Raphson"});
//            methodsCell.setActor(list);

            SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle();
            selectBoxStyle.font = font;
            selectBoxStyle.fontColor = Color.BLACK;
            selectBoxStyle.listStyle = listStyle;
            selectBoxStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture("white.png")));
//            selectBoxStyle.backgroundOpen = new TextureRegionDrawable(new TextureRegion(new Texture("white.png")));
            selectBoxStyle.backgroundOver = new TextureRegionDrawable(new TextureRegion(new Texture("hScrollKnob3.png")));
            ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
//            scrollPaneStyle.vScrollKnob = new TextureRegionDrawable(new TextureRegion(new Texture("vScrollKnob2.png")));
//            scrollPaneStyle.vScroll = new TextureRegionDrawable(new TextureRegion(new Texture("vScroll.png")));
//            scrollPaneStyle.hScrollKnob = new TextureRegionDrawable(new TextureRegion(new Texture("hScrollKnob2.png")));
//            scrollPaneStyle.hScroll = new TextureRegionDrawable(new TextureRegion(new Texture("hScroll.png")));
            selectBoxStyle.scrollStyle = scrollPaneStyle;
            selectBox = new SelectBox(selectBoxStyle);
            selectBox.setItems(new String[]{"Interpolation", "Curve-fitting"});
            choosingCell.setActor(selectBox);
        }
        Cell next = table.add().height(Util.multiplyToWorldFactor(70)).colspan(8);
        {
            TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
            textButtonStyle.font  = font;
            textButtonStyle.fontColor = Color.BLACK;
            textButtonStyle.down = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/down.png")), 35,35,35,35));
            textButtonStyle.up = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/up.png")), 35,35,35,35));
            textButtonStyle.over = textButtonStyle.down;
            TextButton textButton = new TextButton("Next", textButtonStyle);
            textButton.getLabel().setFontScale(.9f * Util.multiplyToWorldFactor(1));
            textButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    openRelatedScreen();
                }
            });
            next.setActor(textButton);
        }
        table.add().height(Util.multiplyToWorldFactor(200)).colspan(13);

        table.top();
    }

    private void openRelatedScreen() {
        double[] pointx = new double[numberOfPoints];
        double[] pointy = new double[numberOfPoints];
        for (int i = 0; i < numberOfPoints; i++) {
            pointx[i] = Double.valueOf(pointxArray.get(i).getText());
        }
        if (function.equals("")) {
            for (int i = 0; i < numberOfPoints; i++) {
                pointy[i] = Double.valueOf(pointyArray.get(i).getText());
            }
        }
        int index = selectBox.getSelectedIndex();
        switch (index) {
            case 0:
                ((Game)Gdx.app.getApplicationListener()).setScreen(
                        new Chapter3InterpolationScreen(Chapter3GetPointsScreen.this, function, pointx, pointy));
                break;
            case 1:
                ((Game)Gdx.app.getApplicationListener()).setScreen(
                        new Chapter3CurveFittingScreen(Chapter3GetPointsScreen.this, function, pointx, pointy));
                break;
        }
    }

    public void createFieldsOfPoints(int number) {
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(5);
        Cell pointNumber = table.add().height(Util.multiplyToWorldFactor(100)).colspan(10).right();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Point " + String.valueOf(number) + " :  ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            pointNumber.setActor(label);
        }
        Cell x = table.add().height(Util.multiplyToWorldFactor(70)).colspan(10).fillX();
        {
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.font = font;
            textFieldStyle.messageFont = font;
            textFieldStyle.messageFontColor = Color.LIGHT_GRAY;
            textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
            textFieldStyle.fontColor = Color.BLACK;
            textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
            TextField textField = new TextField("", textFieldStyle);
            textField.setMessageText("Enter X");
            pointxArray.add(textField);
            x.setActor(textField);
        }
        Cell symbol = table.add().height(Util.multiplyToWorldFactor(100)).colspan(1);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label(" , ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            symbol.setActor(label);
        }
        Cell y = table.add().height(Util.multiplyToWorldFactor(70)).colspan(10).fillX();
        {
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.font = font;
            textFieldStyle.messageFont = font;
            textFieldStyle.messageFontColor = Color.LIGHT_GRAY;
            if (!function .equals("")) {
                textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("hScroll.png"))));
            } else {
                textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
            }
            textFieldStyle.fontColor = Color.BLACK;
            textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
            TextField textField = new TextField("", textFieldStyle);
            if (!function .equals("")) {
                textField.setDisabled(true);
            } else {
                textField.setMessageText("Enter Y");
                pointyArray.add(textField);
            }
            y.setActor(textField);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(14);
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
