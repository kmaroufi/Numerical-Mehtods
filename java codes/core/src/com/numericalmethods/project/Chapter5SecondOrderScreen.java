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
import com.mathworks.engine.MatlabEngine;
import com.numericalmethods.project.util.Constants;
import com.numericalmethods.project.util.Util;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;



/**
 * Created by asus-pc on 1/5/2017.
 */
public class Chapter5SecondOrderScreen implements Screen {
    private SpriteBatch batch;
    private Viewport viewport;
    private Stage stage;
    private Table table;
    private BitmapFont font;
    private ScrollPane scrollPane;
    private Screen parent;

    private String degree;

    private ArrayList<Cell> tableCells = new ArrayList<Cell>();
    private ArrayList<Cell> buttonsCells = new ArrayList<Cell>();
    private ArrayList<Button> buttons = new ArrayList<Button>();
    private ArrayList<Table> descriptionTables = new ArrayList<Table>();

    private TextField equationTextField;
    private TextField pointTextField;
    private TextField endPointTextField;
    private Label resultLabel;
    private TextField initialXTextField;
    private TextField initialYTextField;
    private TextField initialX2TextField;
    private TextField initialY2TextField;
    private TextField hTextField;
    private SelectBox selectBox;

    private String[] answerY;
    private String[] answerF;
    private String[] answerX;
    private int step;

    private TextureRegionDrawable textureRegionDrawable1 = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
    private TextureRegionDrawable textureRegionDrawable2 = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("hScroll.png"))));
    private TextButton previousButton;
    private TextButton nextButton;
    private TextButton jumpButton;
    private TextButton calculatingButton;

    public Chapter5SecondOrderScreen(Screen parent) {
        this.parent = parent;
        degree = "Second-Order ODE";
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
            Label label = new Label("Chapter5", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(1.1f));
            pageTitr.setActor(label);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(15);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(20));

        table.row();
        //degree label
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(15);
        Cell methodTitr = table.add().height(Util.multiplyToWorldFactor(100)).colspan(20);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label(degree, labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.9f));
            methodTitr.setActor(label);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(15);

        table.row();
        //method
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(15);
        Cell choose = table.add().height(Util.multiplyToWorldFactor(100)).colspan(10).right();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Choose your Method :  ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            choose.setActor(label);
        }
        Cell choosingCell =  table.add().height(Util.multiplyToWorldFactor(70)).colspan(8);
        {
            List.ListStyle listStyle = new List.ListStyle();
            listStyle.font = font;
            listStyle.fontColorSelected = Color.BLACK;
            listStyle.fontColorUnselected = Color.BLACK;
            listStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture("hScrollKnob3.png")));
            listStyle.selection = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
//            listStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture("white.png")));

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
            selectBox.setItems(new String[]{"Euler", "Runge-Kutta 4th Order"});
            choosingCell.setActor(selectBox);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(17);

        table.row();
        //function
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(7);
        Cell equationLabel = table.add().height(Util.multiplyToWorldFactor(100)).colspan(10).right();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("y\" =  ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            equationLabel.setActor(label);
        }
        Cell equationTextCell =  table.add().height(Util.multiplyToWorldFactor(70)).colspan(20).fillX();
        {
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.font = font;
            textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
            textFieldStyle.fontColor = Color.BLACK;
            textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
            equationTextField = new TextField("", textFieldStyle);
            equationTextCell.setActor(equationTextField);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(13);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(10));

        table.row();
        // initial point
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(7);
        Cell initialPoint = table.add().height(Util.multiplyToWorldFactor(100)).colspan(10).right();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Enter your initial condition(y) :  ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            initialPoint.setActor(label);
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
            initialXTextField = new TextField("", textFieldStyle);
            initialXTextField.setMessageText("Enter X");
            x.setActor(initialXTextField);
        }
        Cell symbol = table.add().height(Util.multiplyToWorldFactor(100)).colspan(1);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label(" , ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            symbol.setActor(label);
        }
        Cell y = table.add().height(Util.multiplyToWorldFactor(70)).colspan(9).fillX();
        {
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.font = font;
            textFieldStyle.messageFont = font;
            textFieldStyle.messageFontColor = Color.LIGHT_GRAY;
            textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
            textFieldStyle.fontColor = Color.BLACK;
            textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
            initialYTextField = new TextField("", textFieldStyle);
            initialYTextField.setMessageText("Enter Y");
            y.setActor(initialYTextField);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(13);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(10));

        table.row();
        // initial point
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(7);
        Cell initialPoint2 = table.add().height(Util.multiplyToWorldFactor(100)).colspan(10).right();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Enter your initial condition(y') :  ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            initialPoint2.setActor(label);
        }
        Cell x2 = table.add().height(Util.multiplyToWorldFactor(70)).colspan(10).fillX();
        {
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.font = font;
            textFieldStyle.messageFont = font;
            textFieldStyle.messageFontColor = Color.LIGHT_GRAY;
            textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
            textFieldStyle.fontColor = Color.BLACK;
            textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
            initialX2TextField = new TextField("", textFieldStyle);
            initialX2TextField.setMessageText("Enter X");
            x2.setActor(initialX2TextField);
        }
        Cell symbol2 = table.add().height(Util.multiplyToWorldFactor(100)).colspan(1);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label(" , ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            symbol2.setActor(label);
        }
        Cell y2 = table.add().height(Util.multiplyToWorldFactor(70)).colspan(9).fillX();
        {
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.font = font;
            textFieldStyle.messageFont = font;
            textFieldStyle.messageFontColor = Color.LIGHT_GRAY;
            textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
            textFieldStyle.fontColor = Color.BLACK;
            textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
            initialY2TextField = new TextField("", textFieldStyle);
            initialY2TextField.setMessageText("Enter Y'");
            y2.setActor(initialY2TextField);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(13);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(10));

        table.row();
        // h
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(15);
        Cell hLabel = table.add().height(Util.multiplyToWorldFactor(100)).colspan(10).right();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Enter your h :  ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            hLabel.setActor(label);
        }
        Cell hTextCell = table.add().height(Util.multiplyToWorldFactor(70)).colspan(6).fillX();
        {
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.font = font;
            textFieldStyle.messageFont = font;
            textFieldStyle.messageFontColor = Color.LIGHT_GRAY;
            textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
            textFieldStyle.fontColor = Color.BLACK;
            textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
            hTextField = new TextField("", textFieldStyle);
            hTextCell.setActor(hTextField);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(13);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(10));

        table.row();
        // point
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(6);
        Cell pointLabel = table.add().height(Util.multiplyToWorldFactor(100)).colspan(11).right();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Enter your point to calculate its value :  ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            pointLabel.setActor(label);
        }
        Cell pointTextCell =  table.add().height(Util.multiplyToWorldFactor(70)).colspan(20).fillX();
        {
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.font = font;
            textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
            textFieldStyle.fontColor = Color.BLACK;
            textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
            pointTextField = new TextField("", textFieldStyle);
            pointTextCell.setActor(pointTextField);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(13);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(10));

        table.row();
        //calculating
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(20);
        Cell calculateCell = table.add().height(Util.multiplyToWorldFactor(100)).colspan(10);
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
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            calculating();
                        }
                    });
                    thread.setPriority(10);
                    thread.start();
                }
            });
            calculateCell.setActor(calculatingButton);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(20);

        table.row();
        //result
        table.add().height(Util.multiplyToWorldFactor(130)).colspan(15);
        Cell resultCell = table.add().height(Util.multiplyToWorldFactor(130)).colspan(20);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            resultLabel = new Label("", labelStyle);
            resultLabel.setFontScale(Util.multiplyToWorldFactor(.6f));
            resultCell.setActor(resultLabel);
        }
        table.add().height(Util.multiplyToWorldFactor(130)).colspan(15);

        table.row();
        //previousStep
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(15);
        Cell previousStep = table.add().height(Util.multiplyToWorldFactor(70)).colspan(6);
        {
            TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
            textButtonStyle.font  = font;
            textButtonStyle.fontColor = Color.BLACK;
            textButtonStyle.down = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/down.png")), 35,35,35,35));
            textButtonStyle.up = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/up.png")), 35,35,35,35));
            textButtonStyle.over = textButtonStyle.down;
            previousButton = new TextButton("Previous Step", textButtonStyle);
            previousButton.getLabel().setFontScale(.5f * Util.multiplyToWorldFactor(1));
            previousButton.setVisible(false);
            previousButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    previousStep();
                }
            });
            previousStep.setActor(previousButton);
        }
        //jump
        Cell jump = table.add().height(Util.multiplyToWorldFactor(70)).colspan(8);
        {
            TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
            textButtonStyle.font  = font;
            textButtonStyle.fontColor = Color.BLACK;
            textButtonStyle.down = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/down.png")), 35,35,35,35));
            textButtonStyle.up = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/up.png")), 35,35,35,35));
            textButtonStyle.over = textButtonStyle.down;
            jumpButton = new TextButton("Jump To Last Step", textButtonStyle);
            jumpButton.getLabel().setFontScale(.5f * Util.multiplyToWorldFactor(1));
            jumpButton.setVisible(false);
            jumpButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    jump();
                }
            });
            jump.setActor(jumpButton);
        }
        //nextStep
        Cell nextStep = table.add().height(Util.multiplyToWorldFactor(70)).colspan(6);
        {
            TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
            textButtonStyle.font  = font;
            textButtonStyle.fontColor = Color.BLACK;
            textButtonStyle.down = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/down.png")), 35,35,35,35));
            textButtonStyle.up = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/up.png")), 35,35,35,35));
            textButtonStyle.over = textButtonStyle.down;
            nextButton = new TextButton("Next Step", textButtonStyle);
            nextButton.getLabel().setFontScale(.5f * Util.multiplyToWorldFactor(1));
            nextButton.setVisible(false);
            nextButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    nextStep();
                }
            });
            nextStep.setActor(nextButton);
        }

        table.row();
        // padding
        table.add().height(Util.multiplyToWorldFactor(50)).colspan(5);

        table.top();
    }

    private void previousStep() {
        step--;
        if (0 > step) {
            step = 0;
        }
        resultLabel.setText("y(" + String.valueOf(answerX[step]) + ") = " + String.valueOf(answerY[step]) + "         " +
                "y'(" + String.valueOf(answerX[step]) + ") = " + String.valueOf(answerF[step]));
    }

    public void nextStep() {
        step++;
        if (answerY.length - 1 < step) {
            step = answerY.length - 1;
        }
        resultLabel.setText("y(" + String.valueOf(answerX[step]) + ") = " + String.valueOf(answerY[step]) + "         " +
                "y'(" + String.valueOf(answerX[step]) + ") = " + String.valueOf(answerF[step]));
    }

    public void jump(){
        step = answerY.length - 1;
        resultLabel.setText("y(" + String.valueOf(answerX[step]) + ") = " + String.valueOf(answerY[step]) + "         " +
                "y'(" + String.valueOf(answerX[step]) + ") = " + String.valueOf(answerF[step]));
    }

    public void calculating() {
        calculatingButton.setDisabled(true);
        calculatingButton.setText("(please wait)");
        char[] input1 = equationTextField.getText().toCharArray();
        double input2 = Double.parseDouble(initialXTextField.getText());
        double input3 = Double.parseDouble(initialYTextField.getText());
        double input4 = Double.parseDouble(initialY2TextField.getText());
        double input5 = Double.parseDouble(pointTextField.getText());
        double input6 = selectBox.getSelectedIndex() + 1;
        double input7 = Double.parseDouble(hTextField.getText());
        int input8 = Gdx.app.getPreferences("NumericalMethodsProject").getInteger("floating point digits");
        MatlabEngine engine = ((Project) Gdx.app.getApplicationListener()).getEngine();
        try {
            Object[] result = engine.feval(3,  "secondOrderCalculator", input1,input2,input3,input4,input5,input6,input7,input8);
            String[] answerY = (String[]) result[0];
            String[] answerF = (String[]) result[1];
            String[] answerX = (String[]) result[2];
            showResult(answerY, answerF, answerX);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            calculatingButton.setDisabled(false);
            calculatingButton.setText("Calculate");
        }
    }

    private void showResult(String[] answerY, String[] answerF, String[] answerX) {
        this.answerY = answerY;
        this.answerF = answerF;
        this.answerX = answerX;
        step = 0;
        resultLabel.setText("y(" + String.valueOf(answerX[0]) + ") = " + String.valueOf(answerY[0]) + "         " +
                "y'(" + String.valueOf(answerX[0]) + ") = " + String.valueOf(answerF[0]));
        previousButton.setVisible(true);
        jumpButton.setVisible(true);
        nextButton.setVisible(true);
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
