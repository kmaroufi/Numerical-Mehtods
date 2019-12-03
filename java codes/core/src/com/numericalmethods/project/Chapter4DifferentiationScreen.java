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
 * Created by asus-pc on 1/6/2017.
 */
public class Chapter4DifferentiationScreen implements Screen {
    private SpriteBatch batch;
    private Viewport viewport;
    private Stage stage;
    private Table table;
    private BitmapFont font;
    private ScrollPane scrollPane;
    private Screen parent;

    private String task;

    private ArrayList<Cell> tableCells = new ArrayList<Cell>();
    private ArrayList<Cell> buttonsCells = new ArrayList<Cell>();
    private ArrayList<Button> buttons = new ArrayList<Button>();
    private ArrayList<Table> descriptionTables = new ArrayList<Table>();

    private TextField functionTextField;
    private TextField pointTextField;
    private Label resultLabel;
    private TextField hTextField;
    private TextField oTextField;
    private TextField dTextField;

    private String[] answerY;
    private String[] answerX;
    private int step;

    private TextureRegionDrawable textureRegionDrawable1 = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
    private TextureRegionDrawable textureRegionDrawable2 = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("hScroll.png"))));
    private TextButton previousButton;
    private TextButton nextButton;
    private TextButton jumpButton;
    private TextButton calculatingButton;

    public Chapter4DifferentiationScreen(Screen parent) {
        this.parent = parent;
        task = "Differentiation";
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
            Label label = new Label("Chapter4", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(1.1f));
            pageTitr.setActor(label);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(15);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(20));

        table.row();
        //task label
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(15);
        Cell methodTitr = table.add().height(Util.multiplyToWorldFactor(100)).colspan(20);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label(task, labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.9f));
            methodTitr.setActor(label);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(15);

        table.row();
        //function
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(7);
        Cell functionLabel = table.add().height(Util.multiplyToWorldFactor(100)).colspan(10).right();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Enter your function :  ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            functionLabel.setActor(label);
        }
        Cell functionTextCell =  table.add().height(Util.multiplyToWorldFactor(70)).colspan(20).fillX();
        {
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.font = font;
            textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
            textFieldStyle.fontColor = Color.BLACK;
            textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
            functionTextField = new TextField("", textFieldStyle);
            functionTextCell.setActor(functionTextField);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(13);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(10));

        table.row();
        //point
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(7);
        Cell pointLabel = table.add().height(Util.multiplyToWorldFactor(100)).colspan(10).right();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Enter your point :  ", labelStyle);
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
        // d
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(14);
        Cell dLabel = table.add().height(Util.multiplyToWorldFactor(100)).colspan(10).right();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Enter derivative :  ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            dLabel.setActor(label);
        }
        Cell dTextCell = table.add().height(Util.multiplyToWorldFactor(70)).colspan(6).fillX();
        {
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.font = font;
            textFieldStyle.messageFont = font;
            textFieldStyle.messageFontColor = Color.LIGHT_GRAY;
            textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
            textFieldStyle.fontColor = Color.BLACK;
            textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
            dTextField = new TextField("", textFieldStyle);
            dTextCell.setActor(dTextField);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(13);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(10));

        table.row();
        // o
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(14);
        Cell oLabel = table.add().height(Util.multiplyToWorldFactor(100)).colspan(10).right();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Enter order :  ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            oLabel.setActor(label);
        }
        Cell oTextCell = table.add().height(Util.multiplyToWorldFactor(70)).colspan(6).fillX();
        {
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.font = font;
            textFieldStyle.messageFont = font;
            textFieldStyle.messageFontColor = Color.LIGHT_GRAY;
            textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
            textFieldStyle.fontColor = Color.BLACK;
            textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
            oTextField = new TextField("", textFieldStyle);
            oTextCell.setActor(oTextField);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(13);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(10));

        table.row();
        // h
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(14);
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
        resultLabel.setText("y(" + String.valueOf(answerX[step]) + ") = " + String.valueOf(answerY[step]));
    }

    public void nextStep() {
        step++;
        if (answerY.length - 1 < step) {
            step = answerY.length - 1;
        }
        resultLabel.setText("y(" + String.valueOf(answerX[step]) + ") = " + String.valueOf(answerY[step]));
    }

    public void jump(){
        step = answerY.length - 1;
        resultLabel.setText("y(" + String.valueOf(answerX[step]) + ") = " + String.valueOf(answerY[step]));
    }

    public void calculating() {
        calculatingButton.setDisabled(true);
        calculatingButton.setText("(please wait)");
        char[] input1 = functionTextField.getText().toCharArray();
        double input2 = Double.parseDouble(pointTextField.getText());
        double input3 = Double.parseDouble(dTextField.getText());
        double input4 = Double.parseDouble(oTextField.getText());
        double input5 = Double.parseDouble(hTextField.getText());
        int input6 = Gdx.app.getPreferences("NumericalMethodsProject").getInteger("floating point digits");
        MatlabEngine engine = ((Project) Gdx.app.getApplicationListener()).getEngine();
        String method = "Differentiation";
        try {
            Object[] result = engine.feval(3,  method, input1,input2,input3,input4,input5, input6);
            Gdx.app.log("input1", String.valueOf(input1));
            Gdx.app.log("input2", String.valueOf(input2));
            Gdx.app.log("input3", String.valueOf(input3));
            Gdx.app.log("input4", String.valueOf(input4));
            Gdx.app.log("input5", String.valueOf(input5));
            String answer1 = (String) result[0];
            String answer2 = (String) result[1];
            String answer3 = (String) result[2];
//            double answer1 = (Double) result[0];
//            double answer2 = (Double) result[1];
            showResult(answer1, answer2, answer3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }finally {
            calculatingButton.setDisabled(false);
            calculatingButton.setText("Calculate");
        }
    }

    private void showResult(String answer1, String answer2, String answer3) {
        this.answerY = answerY;
        this.answerX = answerX;
        step = 0;
        resultLabel.setText("g(h1) = " + answer1 + " , g(h1/2) = " + answer2 + "      -       " + "richard : " + answer3);
        previousButton.setVisible(false);
        jumpButton.setVisible(false);
        nextButton.setVisible(false);
    }

    private void showResult(double answer1, double answer2) {
        this.answerY = answerY;
        this.answerX = answerX;
        step = 0;
        resultLabel.setText("answer = " + answer1 + "      -       " + "richard : " + answer2);
        previousButton.setVisible(false);
        jumpButton.setVisible(false);
        nextButton.setVisible(false);
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
