package com.numericalmethods.project;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mathworks.engine.MatlabEngine;
import com.numericalmethods.project.util.Constants;
import com.numericalmethods.project.util.Util;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by asus-pc on 12/6/2016.
 */
public class Chapter1Screen implements Screen {
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

    private TextField equationTextField;
    private double[] result;
    private boolean requiredUpdate = false;
    private Array<Cell> cellsfromPreviousUpdate = new Array<Cell>();
    private boolean isInProcess = false;
    private Array<TextField> variablesValues = new Array<TextField>();
    private Array<TextField> variablesErrors = new Array<TextField>();

    private Label valueLabel;
    private Label parametricAbsoluteErrorLabel;
    private Label numericAbsoluteErrorLabel;
    private Label parametricRelativeErrorLabel;
    private Label numericRelativeErrorLabel;
    private Integer step;
    private String[] absErrArray;
    private String[] valueAbsErrArray;
    private String[] relErrArray;
    private String[] valueRelErrArray;
    private ArrayList<Label> paramValue = new ArrayList<Label>();
    private ArrayList<Label> paramAbsErr = new ArrayList<Label>();
    private ArrayList<Label> paramRelErr = new ArrayList<Label>();
    private TextButton calculatingButton;

    public Chapter1Screen(Screen parent) {
        this.parent = parent;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        viewport = new ScreenViewport();
        font = new BitmapFont(Gdx.files.internal("LittleTitr.fnt"));
        font.getData().setScale(.5f);
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
            Label label = new Label("Chapter1", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(1.1f));
            pageTitr.setActor(label);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(15);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(20));

        table.row();
        //equation
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(5);
        Cell equationLabel = table.add().height(Util.multiplyToWorldFactor(100)).colspan(7).right();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Enter your equation :  ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            equationLabel.setActor(label);
        }
        Cell equationTextCell =  table.add().height(Util.multiplyToWorldFactor(70)).colspan(25).fillX();
        {
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.font = font;
            textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
            textFieldStyle.fontColor = Color.BLACK;
            textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
            this.equationTextField = new TextField("", textFieldStyle);
            equationTextCell.setActor(this.equationTextField);
            this.equationTextField.setTextFieldListener(new TextField.TextFieldListener() {
                @Override
                public void keyTyped(TextField textField, char c) {
//                        characterRecognizer(equationTextField.getText());
                }
            });
        }
        Cell variablesRecognition = table.add().height(Util.multiplyToWorldFactor(70)).colspan(8);
        {
            TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
            textButtonStyle.font  = font;
            textButtonStyle.fontColor = Color.BLACK;
            textButtonStyle.down = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/down.png")), 35,35,35,35));
            textButtonStyle.up = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/up.png")), 35,35,35,35));
            textButtonStyle.over = textButtonStyle.down;
            TextButton textButton = new TextButton("Variables Recognition", textButtonStyle);
            textButton.getLabel().setFontScale(.5f * Util.multiplyToWorldFactor(1));
            textButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    characterRecognizer(equationTextField.getText());
                }
            });
            variablesRecognition.setActor(textButton);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(5);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(10));

        table.top();
    }

    public void calculating() {
        char[] input1 = equationTextField.getText().toCharArray();
        int numberOfVariables = 0;
        for (double v : result) {
            if (v == 0) {
                continue;
            }
            numberOfVariables++;
        }
        char[][] input2 = new char[numberOfVariables][1];
        int counter = 0;
        for (int i = 0; i < result.length; i++) {
            if (result[i] == 0) {
                continue;
            }
            switch (i) {
                case 0:
                    input2[counter][0] = 'a';
                    break;
                case 1:
                    input2[counter][0] = 'b';
                    break;
                case 2:
                    input2[counter][0] = 'c';
                    break;
                case 3:
                    input2[counter][0] = 'd';
                    break;
                case 4:
                    input2[counter][0] = 'e';
                    break;
                case 5:
                    input2[counter][0] = 'f';
                    break;
            }
            counter++;
        }
        double[] input3 = new double[numberOfVariables];
        double[] input4 = new double[numberOfVariables];
        for (int i = 0; i < input3.length; i++) {
            if (variablesValues.get(i).getText().equals("")){
                input3[i] = 0;
                input4[i] = 0;
            } else {
                input3[i] = Double.parseDouble(variablesValues.get(i).getText());
                input4[i] = 1;
            }
        }
        double[] input5 = new double[numberOfVariables];
        double[] input6 = new double[numberOfVariables];
        for (int i = 0; i < input5.length; i++) {
            if (variablesErrors.get(i).getText().equals("")){
                input5[i] = 0;
                input6[i] = 0;
            } else {
                input5[i] = Double.parseDouble(variablesErrors.get(i).getText());
                input6[i] = 1;
            }
        }
        int input7 = Gdx.app.getPreferences("NumericalMethodsProject").getInteger("floating point digits");
        MatlabEngine engine = ((Project) Gdx.app.getApplicationListener()).getEngine();
        try {
            Object[] object = engine.feval(8, "calculating", input1, (Object)input2,input3,input4, input5, input6, input7);
//            Character finalValue = (Character) object[0];
//            Character finalError = (Character) object[1];
            String finalValue = (String) object[0];
            String[] absErr = (String[]) object[1];
            String[] valueAbsErr = (String[]) object[2];
            String[] relErr = (String[]) object[3];
            String[] valueRelErr = (String[]) object[4];
            String[] initialValues = (String[]) object[5];
            String[] initialErrors = (String[]) object[6];
            String[] initialRelativeErrors = (String[]) object[7];
            absErrArray = absErr;
            valueAbsErrArray = valueAbsErr;
            relErrArray = relErr;
            valueRelErrArray = valueRelErr;
            showResult(finalValue, absErr, valueAbsErr, relErr, valueRelErr, input2, initialValues, initialErrors, initialRelativeErrors);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void showResult(String finalValue, String[] absErr, String[] valueAbsErr, String[] relErr, String[] valueRelErr, char[][] parameters, String[] initialValues, String[] initialErrors, String[] initialRelativeErrors) {
        step = 0;
        cellsfromPreviousUpdate.get(cellsfromPreviousUpdate.size - 1).clearActor();
        table.getCells().removeIndex(table.getCells().size - 1);
        cellsfromPreviousUpdate.removeIndex(cellsfromPreviousUpdate.size - 1);
        cellsfromPreviousUpdate.get(cellsfromPreviousUpdate.size - 1).clearActor();
        table.getCells().removeIndex(table.getCells().size - 1);
        cellsfromPreviousUpdate.removeIndex(cellsfromPreviousUpdate.size - 1);
        cellsfromPreviousUpdate.get(cellsfromPreviousUpdate.size - 1).clearActor();
        table.getCells().removeIndex(table.getCells().size - 1);
        cellsfromPreviousUpdate.removeIndex(cellsfromPreviousUpdate.size - 1);
        table.invalidateHierarchy();

        paramRelErr.clear();
        paramAbsErr.clear();
        paramValue.clear();

        //value
        cellsfromPreviousUpdate.add(table.add().height(Util.multiplyToWorldFactor(100)).colspan(15));
        Cell finalValueCell = table.add().height(Util.multiplyToWorldFactor(100)).colspan(20);
        cellsfromPreviousUpdate.add(finalValueCell);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            valueLabel = new Label("Value :  " + finalValue, labelStyle);
            valueLabel.setFontScale(Util.multiplyToWorldFactor(.7f));
            finalValueCell.setActor(valueLabel);
        }
        cellsfromPreviousUpdate.add(table.add().height(Util.multiplyToWorldFactor(100)).colspan(15));

        table.row();
        // numeric absolute error
        cellsfromPreviousUpdate.add(table.add().height(Util.multiplyToWorldFactor(100)).colspan(5));
        Cell numericErrorCell = table.add().height(Util.multiplyToWorldFactor(100)).colspan(40);
        cellsfromPreviousUpdate.add(numericErrorCell);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            numericAbsoluteErrorLabel = new Label("Absolute error : " + valueAbsErr[valueAbsErr.length - 1], labelStyle);
            numericAbsoluteErrorLabel.setFontScale(Util.multiplyToWorldFactor(.7f));
            numericErrorCell.setActor(numericAbsoluteErrorLabel);
        }
        cellsfromPreviousUpdate.add(table.add().height(Util.multiplyToWorldFactor(100)).colspan(5));

        table.row();
        // numeric relative error
        cellsfromPreviousUpdate.add(table.add().height(Util.multiplyToWorldFactor(100)).colspan(5));
        Cell numericRelativeErrorCell = table.add().height(Util.multiplyToWorldFactor(100)).colspan(40);
        cellsfromPreviousUpdate.add(numericRelativeErrorCell);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            numericRelativeErrorLabel = new Label("Relative error : " + valueRelErr[valueRelErr.length - 1], labelStyle);
            numericRelativeErrorLabel.setFontScale(Util.multiplyToWorldFactor(.7f));
            numericRelativeErrorCell.setActor(numericRelativeErrorLabel);
        }
        cellsfromPreviousUpdate.add(table.add().height(Util.multiplyToWorldFactor(100)).colspan(5));

        table.row();
        // padding
        cellsfromPreviousUpdate.add(table.add().height(Util.multiplyToWorldFactor(50)).colspan(5));

        if (Gdx.app.getPreferences("NumericalMethodsProject").getString("step wise").equals("Yes")) {
            table.row();
            // step wise label
            cellsfromPreviousUpdate.add(table.add().height(Util.multiplyToWorldFactor(100)).colspan(5));
            Cell stepWiseCell = table.add().height(Util.multiplyToWorldFactor(100)).colspan(40);
            cellsfromPreviousUpdate.add(stepWiseCell);
            {
                Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
                Label label = new Label("Step Wise Representation", labelStyle);
                label.setFontScale(Util.multiplyToWorldFactor(.9f));
                stepWiseCell.setActor(label);
            }
            cellsfromPreviousUpdate.add(table.add().height(Util.multiplyToWorldFactor(100)).colspan(5));

            table.row();
            // parametric absolute error
            cellsfromPreviousUpdate.add(table.add().height(Util.multiplyToWorldFactor(100)).colspan(5));
            Cell parametricErrorCell = table.add().height(Util.multiplyToWorldFactor(100)).colspan(40);
            cellsfromPreviousUpdate.add(parametricErrorCell);
            {
                Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
                parametricAbsoluteErrorLabel = new Label("Absolute error : " + absErr[0], labelStyle);
                parametricAbsoluteErrorLabel.setFontScale(Util.multiplyToWorldFactor(.7f));
                parametricErrorCell.setActor(parametricAbsoluteErrorLabel);
            }
            cellsfromPreviousUpdate.add(table.add().height(Util.multiplyToWorldFactor(100)).colspan(5));

            table.row();
            // parametric relative error
            cellsfromPreviousUpdate.add(table.add().height(Util.multiplyToWorldFactor(100)).colspan(5));
            Cell parametricRelativeErrorCell = table.add().height(Util.multiplyToWorldFactor(100)).colspan(40);
            cellsfromPreviousUpdate.add(parametricRelativeErrorCell);
            {
                Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
                parametricRelativeErrorLabel = new Label("Relative error : " + relErr[0], labelStyle);
                parametricRelativeErrorLabel.setFontScale(Util.multiplyToWorldFactor(.7f));
                parametricRelativeErrorCell.setActor(parametricRelativeErrorLabel);
            }
            cellsfromPreviousUpdate.add(table.add().height(Util.multiplyToWorldFactor(100)).colspan(5));

            table.row();
            //previousStep
            cellsfromPreviousUpdate.add(table.add().height(Util.multiplyToWorldFactor(100)).colspan(15));
            Cell previousStep = table.add().height(Util.multiplyToWorldFactor(70)).colspan(6);
            cellsfromPreviousUpdate.add(previousStep);
            {
                TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
                textButtonStyle.font  = font;
                textButtonStyle.fontColor = Color.BLACK;
                textButtonStyle.down = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/down.png")), 35,35,35,35));
                textButtonStyle.up = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/up.png")), 35,35,35,35));
                textButtonStyle.over = textButtonStyle.down;
                TextButton textButton = new TextButton("Previous Step", textButtonStyle);
                textButton.getLabel().setFontScale(.5f * Util.multiplyToWorldFactor(1));
                textButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        previousStep();
                    }
                });
                previousStep.setActor(textButton);
            }
            //jump
            Cell jump = table.add().height(Util.multiplyToWorldFactor(70)).colspan(8);
            cellsfromPreviousUpdate.add(jump);
            {
                TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
                textButtonStyle.font  = font;
                textButtonStyle.fontColor = Color.BLACK;
                textButtonStyle.down = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/down.png")), 35,35,35,35));
                textButtonStyle.up = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/up.png")), 35,35,35,35));
                textButtonStyle.over = textButtonStyle.down;
                TextButton textButton = new TextButton("Jump To Last Step", textButtonStyle);
                textButton.getLabel().setFontScale(.5f * Util.multiplyToWorldFactor(1));
                textButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        jump();
                    }
                });
                jump.setActor(textButton);
            }
            //nextStep
            Cell nextStep = table.add().height(Util.multiplyToWorldFactor(70)).colspan(6);
            cellsfromPreviousUpdate.add(nextStep);
            {
                TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
                textButtonStyle.font  = font;
                textButtonStyle.fontColor = Color.BLACK;
                textButtonStyle.down = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/down.png")), 35,35,35,35));
                textButtonStyle.up = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/up.png")), 35,35,35,35));
                textButtonStyle.over = textButtonStyle.down;
                TextButton textButton = new TextButton("Next Step", textButtonStyle);
                textButton.getLabel().setFontScale(.5f * Util.multiplyToWorldFactor(1));
                textButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        nextStep();
                    }
                });
                nextStep.setActor(textButton);
            }

            table.row();
            // padding
            cellsfromPreviousUpdate.add(table.add().height(Util.multiplyToWorldFactor(50)).colspan(5));
        }

        table.row();
        // parameters cell
        cellsfromPreviousUpdate.add(table.add().height(Util.multiplyToWorldFactor(100)).colspan(5));
        Cell parametersCell = table.add().height(Util.multiplyToWorldFactor(100)).colspan(40);
        cellsfromPreviousUpdate.add(parametersCell);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Parameters", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.9f));
            parametersCell.setActor(label);
        }
        cellsfromPreviousUpdate.add(table.add().height(Util.multiplyToWorldFactor(100)).colspan(5));

        for (int i = 0; i < parameters.length; i++) {
            table.row();
            //variable
            createVariablesResult(parameters[i][0], initialValues[i], initialErrors[i], initialRelativeErrors[i]);

            table.row();
            //padding
            cellsfromPreviousUpdate.add(table.add().colspan(0).height(Util.multiplyToWorldFactor(10)));
        }

        table.row();
        //save all data
        cellsfromPreviousUpdate.add(table.add().height(Util.multiplyToWorldFactor(100)).colspan(19));
        Cell saveDataCell = table.add().height(Util.multiplyToWorldFactor(100)).colspan(12);
        cellsfromPreviousUpdate.add(saveDataCell);
        {
            TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
            textButtonStyle.font  = font;
            textButtonStyle.fontColor = Color.BLACK;
            textButtonStyle.down = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/down.png")), 35,35,35,35));
            textButtonStyle.up = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/up.png")), 35,35,35,35));
            textButtonStyle.over = textButtonStyle.down;
            final TextButton textButton = new TextButton("Save all to file!", textButtonStyle);
            textButton.getLabel().setFontScale(.7f * Util.multiplyToWorldFactor(1));
            textButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    textButton.setText("Saved");
                    save();
                }
            });
            saveDataCell.setActor(textButton);
        }
        cellsfromPreviousUpdate.add(table.add().height(Util.multiplyToWorldFactor(100)).colspan(19));

        table.row();
        //padding
        cellsfromPreviousUpdate.add(table.add().colspan(0).height(Util.multiplyToWorldFactor(20)));
    }

    private void save() {
        FileHandle fileHandle = Gdx.files.local("save/chapter1.txt");
        fileHandle.writeString("", false);
        fileHandle.writeString("equation : " + equationTextField.getText() + "\n", true);
        fileHandle.writeString(valueLabel.getText() + "\n", true);
        fileHandle.writeString(numericAbsoluteErrorLabel.getText() + "\n", true);
        fileHandle.writeString(numericRelativeErrorLabel.getText() + "\n", true);
        for (int i = 0; i < paramValue.size(); i++) {
            fileHandle.writeString(paramValue.get(i).getText() + " , " +
                    paramAbsErr.get(i).getText() + " , " + paramRelErr.get(i).getText() + "\n", true);
        }
    }

    public void createVariablesResult(char variableName, String initialValue, String initialError, String initialRelativeError) {
        cellsfromPreviousUpdate.add(table.add().height(Util.multiplyToWorldFactor(100)).colspan(4));
        Cell value = table.add().height(Util.multiplyToWorldFactor(100)).colspan(14).left().fillX();
        cellsfromPreviousUpdate.add(value);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            labelStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture("param.png")));
            Label label = new Label(variableName + " = " + initialValue, labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            paramValue.add(label);
            value.setActor(label);
        }
        cellsfromPreviousUpdate.add(table.add().height(Util.multiplyToWorldFactor(100)).colspan(1));
        Cell absoluteError = table.add().height(Util.multiplyToWorldFactor(100)).colspan(14).left().fillX();
        cellsfromPreviousUpdate.add(absoluteError);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            labelStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture("param.png")));
            Label label = new Label("error(" + variableName + ") = " + initialError, labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            paramAbsErr.add(label);
            absoluteError.setActor(label);
        }
        cellsfromPreviousUpdate.add(table.add().height(Util.multiplyToWorldFactor(100)).colspan(1));
        Cell relativeError = table.add().height(Util.multiplyToWorldFactor(100)).colspan(14).left().fillX();
        cellsfromPreviousUpdate.add(relativeError);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            labelStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture("param.png")));
            Label label = new Label("delta(" + variableName + ") = " + initialRelativeError, labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            paramRelErr.add(label);
            relativeError.setActor(label);
        }
        cellsfromPreviousUpdate.add(table.add().height(Util.multiplyToWorldFactor(100)).colspan(4));
    }

    private void previousStep() {
        step--;
        if (0 > step) {
            step = 0;
        }
        parametricAbsoluteErrorLabel.setText("Absolute error : " + absErrArray[step]);
        parametricRelativeErrorLabel.setText("Relative error : " + relErrArray[step]);
    }

    public void nextStep() {
        step++;
        if (absErrArray.length - 1 < step) {
            step = absErrArray.length - 1;
        }
        parametricAbsoluteErrorLabel.setText("Absolute error : " + absErrArray[step]);
        parametricRelativeErrorLabel.setText("Relative error : " + relErrArray[step]);
    }

    public void jump(){
        step = absErrArray.length - 1;
        parametricAbsoluteErrorLabel.setText("Absolute error : " + absErrArray[step]);
        parametricRelativeErrorLabel.setText("Relative error : " + relErrArray[step]);
    }

    private void characterRecognizer(final String equation) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized ((Object)isInProcess) {
                    isInProcess = true;
                    MatlabEngine engine = ((Project) Gdx.app.getApplicationListener()).getEngine();
                    try {
                        Object[] objects = engine.feval(6, "characterReconizer", equation);
                        result = new double[6];
                        for (int i = 0; i < 6; i++) {
                            result[i] = (Double) objects[i];
                            Gdx.app.log("res", String.valueOf(result[i]));
                        }
                        requiredUpdate = true;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    isInProcess = false;
                }
            }
        }).start();
    }

    private void updateVariables() {
        for (Cell cell : cellsfromPreviousUpdate) {
            cell.clearActor();
        }
        table.getCells().removeAll(cellsfromPreviousUpdate, true);
        table.invalidateHierarchy();
        variablesValues.clear();
        variablesErrors.clear();
        cellsfromPreviousUpdate.clear();
        if (result[0] == 1) {
            table.row();
            //variable
            createVariable("a");

            table.row();
            //padding
            cellsfromPreviousUpdate.add(table.add().colspan(0).height(Util.multiplyToWorldFactor(10)));
        }

        if (result[1] == 1) {
            table.row();
            //variable
            createVariable("b");

            table.row();
            //padding
            cellsfromPreviousUpdate.add(table.add().colspan(0).height(Util.multiplyToWorldFactor(10)));
        }

        if (result[2] == 1) {
            table.row();
            //variable
            createVariable("c");

            table.row();
            //padding
            cellsfromPreviousUpdate.add(table.add().colspan(0).height(Util.multiplyToWorldFactor(10)));
        }

        if (result[3] == 1) {
            table.row();
            //variable
            createVariable("d");

            table.row();
            //padding
            cellsfromPreviousUpdate.add(table.add().colspan(0).height(Util.multiplyToWorldFactor(10)));
        }

        if (result[4] == 1) {
            table.row();
            //variable
            createVariable("e");

            table.row();
            //padding
            cellsfromPreviousUpdate.add(table.add().colspan(0).height(Util.multiplyToWorldFactor(10)));
        }

        if (result[5] == 1) {
            table.row();
            //variable
            createVariable("f");

            table.row();
            //padding
            cellsfromPreviousUpdate.add(table.add().colspan(0).height(Util.multiplyToWorldFactor(10)));
        }

        table.row();
        //calculating
        cellsfromPreviousUpdate.add(table.add().height(Util.multiplyToWorldFactor(100)).colspan(20));
        Cell calculating = table.add().height(Util.multiplyToWorldFactor(100)).colspan(10);
        cellsfromPreviousUpdate.add(calculating);
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
//                    Thread thread = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            calculating();
//                        }
//                    });
//                    thread.setPriority(10);
//                    thread.start();
                    calculating();
//                    ((Game) Gdx.app.getApplicationListener()).setScreen(new Chapter1ResultScreen(Chapter1Screen.this));
                }
            });
            calculating.setActor(calculatingButton);
        }
        cellsfromPreviousUpdate.add(table.add().height(Util.multiplyToWorldFactor(100)).colspan(20));
        requiredUpdate = false;
    }

    private void createVariable (String variableName) {
        cellsfromPreviousUpdate.add(table.add().height(Util.multiplyToWorldFactor(100)).colspan(5));
        Cell variable = table.add().height(Util.multiplyToWorldFactor(100)).colspan(10).right();
        cellsfromPreviousUpdate.add(variable);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Variable " + variableName + " :  ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            variable.setActor(label);
        }
        Cell value = table.add().height(Util.multiplyToWorldFactor(70)).colspan(10).fillX();
        cellsfromPreviousUpdate.add(value);
        {
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.font = font;
            textFieldStyle.messageFont = font;
            textFieldStyle.messageFontColor = Color.LIGHT_GRAY;
            textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
            textFieldStyle.fontColor = Color.BLACK;
            textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
            TextField textField = new TextField("", textFieldStyle);
            textField.setMessageText("Enter the value of variable");
            value.setActor(textField);
            variablesValues.add(textField);
        }
        Cell errorSymbol = table.add().height(Util.multiplyToWorldFactor(100)).colspan(1);
        cellsfromPreviousUpdate.add(errorSymbol);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label(" , ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            errorSymbol.setActor(label);
        }
        Cell error = table.add().height(Util.multiplyToWorldFactor(70)).colspan(10).fillX();
        cellsfromPreviousUpdate.add(error);
        {
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.font = font;
            textFieldStyle.messageFont = font;
            textFieldStyle.messageFontColor = Color.LIGHT_GRAY;
            textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
            textFieldStyle.fontColor = Color.BLACK;
            textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
            TextField textField = new TextField("", textFieldStyle);
            textField.setMessageText("Enter the absolute error of variable");
            error.setActor(textField);
            variablesErrors.add(textField);
        }
        cellsfromPreviousUpdate.add(table.add().height(Util.multiplyToWorldFactor(100)).colspan(14));
    }

    @Override
    public void render(float delta) {
        Color color = Color.LIGHT_GRAY;
        Gdx.gl.glClearColor(color.r, color.b, color.g, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        viewport.setScreenBounds(0, 0 , Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport.apply(true);

        if (requiredUpdate) {
            updateVariables();
        }

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
