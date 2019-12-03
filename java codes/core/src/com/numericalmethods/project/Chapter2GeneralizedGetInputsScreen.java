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
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mathworks.engine.MatlabEngine;
import com.numericalmethods.project.util.Constants;
import com.numericalmethods.project.util.Util;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by asus-pc on 12/7/2016.
 */
public class Chapter2GeneralizedGetInputsScreen implements Screen {
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

    private Integer numberOfVariables;

    private TextField numberOfVariablesTextField;
    private ArrayList<TextField> variablesArray = new ArrayList<TextField>();
    private ArrayList<TextField> functionsArray = new ArrayList<TextField>();
    private ArrayList<Label> resultLabels = new ArrayList<Label>();
    private TextButton calculatingButton;

    public Chapter2GeneralizedGetInputsScreen(Screen parent, Integer numberOfVariables) {
        this.parent = parent;
        this.numberOfVariables = numberOfVariables;
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
            Label label = new Label("Chapter2", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(1.1f));
            pageTitr.setActor(label);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(15);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(20));

        table.row();
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(16);
        //number of points
        Cell numberOfVariablesLabel = table.add().height(Util.multiplyToWorldFactor(100)).colspan(10).right();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Number of variables :  ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            numberOfVariablesLabel.setActor(label);
        }
        Cell numberOfVariablesTextCell =  table.add().height(Util.multiplyToWorldFactor(70)).colspan(4).fillX();
        {
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.font = font;
            textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("hScroll.png"))));
            textFieldStyle.fontColor = Color.BLACK;
            textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
            numberOfVariablesTextField = new TextField(String.valueOf(numberOfVariables), textFieldStyle);
            numberOfVariablesTextField.setDisabled(true);
            numberOfVariablesTextCell.setActor(numberOfVariablesTextField);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(20);

        for (int i = 0; i < numberOfVariables; i++) {
            table.row();
            //padding
            table.add().colspan(0).height(Util.multiplyToWorldFactor(20));
            createFieldsOfVariablesAndFunctions(i + 1);
        }

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(50));

        table.row();
        //calculating
        table.add().height(Util.multiplyToWorldFactor(170)).colspan(15);
        Cell calculatingFields = table.add().height(Util.multiplyToWorldFactor(170)).colspan(20);
        {
            TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
            textButtonStyle.font  = font;
            textButtonStyle.fontColor = Color.BLACK;
            textButtonStyle.down = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/down.png")), 35,35,35,35));
            textButtonStyle.up = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/up.png")), 35,35,35,35));
            textButtonStyle.over = textButtonStyle.down;
            calculatingButton = new TextButton("Calculate", textButtonStyle);
            calculatingButton.getLabel().setFontScale(.8f * Util.multiplyToWorldFactor(1));
            calculatingButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    calculating();
                }
            });
            calculatingFields.setActor(calculatingButton);
        }
        table.add().height(Util.multiplyToWorldFactor(170)).colspan(15);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(50));

        for (int i = 0; i < numberOfVariables; i++) {
            table.row();
            createResultLabels();
        }

        table.top();
    }

    public void calculating() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                calculatingButton.setDisabled(true);
                calculatingButton.setText("(please wait)");
                for (TextField variable : variablesArray) {
                    variable.setDisabled(true);
                }
                for (TextField function : functionsArray) {
                    function.setDisabled(true);
                }
                double input1 = numberOfVariables;
                char[][] variables = new char[numberOfVariables][30];
                char[][] functions = new char[numberOfVariables][30];
                for (int i = 0; i < numberOfVariables; i++) {
                    variables[i] = variablesArray.get(i).getText().toCharArray();
                }
                for (int i = 0; i < numberOfVariables; i++) {
                    functions[i] = functionsArray.get(i).getText().toCharArray();
                }
                char[][] input2 = variables;
                char[][] input3 = functions;
                MatlabEngine engine = ((Project) Gdx.app.getApplicationListener()).getEngine();
                try {
                    Object object = engine.feval(1,  "generalizedNewtonRaphson", input1,(Object)input2,(Object)input3);
                    double[] answers;
                    if (object instanceof Double) {
                        answers = new double[1];
                        answers[0] = (Double) object;
                    } else {
                        answers = (double[]) object;
                    }
                    showResult(answers);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } finally {
                    calculatingButton.setDisabled(false);
                    calculatingButton.setText("Calculate");
                    for (TextField variable : variablesArray) {
                        variable.setDisabled(false);
                    }
                    for (TextField function : functionsArray) {
                        function.setDisabled(false);
                    }
                }
            }
        }).start();
    }

    private void showResult(double[] answers) {
        for (double answer : answers) {
            Gdx.app.log("answer", String.valueOf(answer));
        }
        for (int i = 0; i < answers.length; i++) {
            resultLabels.get(i).setText(variablesArray.get(i).getText() + " = " + String.valueOf(answers[i]));
        }
    }

    public void createFieldsOfVariablesAndFunctions(int number) {
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(9);
        Cell variableNumber = table.add().height(Util.multiplyToWorldFactor(100)).colspan(5).right();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Variable " + String.valueOf(number) + " :  ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            variableNumber.setActor(label);
        }
        Cell variable = table.add().height(Util.multiplyToWorldFactor(70)).colspan(10).fillX();
        {
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.font = font;
            textFieldStyle.messageFont = font;
            textFieldStyle.messageFontColor = Color.LIGHT_GRAY;
            textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
            textFieldStyle.fontColor = Color.BLACK;
            textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
            TextField textField = new TextField("", textFieldStyle);
            textField.setMessageText("");
            variablesArray.add(textField);
            variable.setActor(textField);
        }
        Cell functionNumber = table.add().height(Util.multiplyToWorldFactor(100)).colspan(5).right();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Function " + String.valueOf(number) + " :  ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            functionNumber.setActor(label);
        }
        Cell function = table.add().height(Util.multiplyToWorldFactor(70)).colspan(10).fillX();
        {
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.font = font;
            textFieldStyle.messageFont = font;
            textFieldStyle.messageFontColor = Color.LIGHT_GRAY;
            textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
            textFieldStyle.fontColor = Color.BLACK;
            textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
            TextField textField = new TextField("", textFieldStyle);
            textField.setMessageText("");
            functionsArray.add(textField);
            function.setActor(textField);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(11);
    }

    public void createResultLabels() {
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(15);
        Cell variable = table.add().height(Util.multiplyToWorldFactor(100)).colspan(20);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.7f));
            resultLabels.add(label);
            variable.setActor(label);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(15);
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
