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
public class Chapter2FixedPointScreen implements Screen {
    private SpriteBatch batch;
    private Viewport viewport;
    private Stage stage;
    private Table table;
    private BitmapFont font;
    private ScrollPane scrollPane;
    private Screen parent;

    private String method;

    private ArrayList<Cell> tableCells = new ArrayList<Cell>();
    private ArrayList<Cell> buttonsCells = new ArrayList<Cell>();
    private ArrayList<Button> buttons = new ArrayList<Button>();
    private ArrayList<Table> descriptionTables = new ArrayList<Table>();

    private TextField functionTextField;
    private TextField startPointTextField;
    private TextField endPointTextField;
    private TextField initialPointTextField;
    private TextField numberOfIterations;
    private Label resultLabel;
    private TextButton calculatingButton;

    public Chapter2FixedPointScreen(Screen parent, String method) {
        this.parent = parent;
        this.method = method;
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
            Label label = new Label("Chapter2", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(1.1f));
            pageTitr.setActor(label);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(15);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(20));

        table.row();
        //method label
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(15);
        Cell methodTitr = table.add().height(Util.multiplyToWorldFactor(100)).colspan(20);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Method : " + method, labelStyle);
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
        //start point
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(7);
        Cell startPointLabel = table.add().height(Util.multiplyToWorldFactor(100)).colspan(10).right();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Enter your start point :  ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            startPointLabel.setActor(label);
        }
        Cell startPointTextCell =  table.add().height(Util.multiplyToWorldFactor(70)).colspan(20).fillX();
        {
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.font = font;
            textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
            textFieldStyle.fontColor = Color.BLACK;
            textFieldStyle.messageFont = font;
            textFieldStyle.messageFontColor = Color.LIGHT_GRAY;
            textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
            startPointTextField = new TextField("", textFieldStyle);
            startPointTextField.setMessageText(" optional");
            startPointTextCell.setActor(startPointTextField);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(13);

        table.row();
        //end point
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(7);
        Cell endPointLabel = table.add().height(Util.multiplyToWorldFactor(100)).colspan(10).right();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Enter your end point :  ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            endPointLabel.setActor(label);
        }
        Cell endPointTextCell =  table.add().height(Util.multiplyToWorldFactor(70)).colspan(20).fillX();
        {
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.font = font;
            textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
            textFieldStyle.fontColor = Color.BLACK;
            textFieldStyle.messageFont = font;
            textFieldStyle.messageFontColor = Color.LIGHT_GRAY;
            textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
            endPointTextField = new TextField("", textFieldStyle);
            endPointTextField.setMessageText(" optional");
            endPointTextCell.setActor(endPointTextField);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(13);

        table.row();
        //initial point
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(7);
        Cell initialPointLabel = table.add().height(Util.multiplyToWorldFactor(100)).colspan(10).right();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Enter your initial point :  ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            initialPointLabel.setActor(label);
        }
        Cell initialPointTextCell =  table.add().height(Util.multiplyToWorldFactor(70)).colspan(20).fillX();
        {
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.font = font;
            textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
            textFieldStyle.fontColor = Color.BLACK;
            textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
            initialPointTextField = new TextField("", textFieldStyle);
            initialPointTextCell.setActor(initialPointTextField);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(13);

        table.row();
        //number of iterations;
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(7);
        Cell numOfIterationsPointLabel = table.add().height(Util.multiplyToWorldFactor(100)).colspan(10).right();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Enter your number of iterations :  ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            numOfIterationsPointLabel.setActor(label);
        }
        Cell numOfIterationsTextCell =  table.add().height(Util.multiplyToWorldFactor(70)).colspan(20).fillX();
        {
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.font = font;
            textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
            textFieldStyle.fontColor = Color.BLACK;
            textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
            numberOfIterations = new TextField("", textFieldStyle);
            numOfIterationsTextCell.setActor(numberOfIterations);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(13);

        table.row();
        //calculating
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(20);
        Cell findRoot = table.add().height(Util.multiplyToWorldFactor(100)).colspan(10);
        {
            TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
            textButtonStyle.font  = font;
            textButtonStyle.fontColor = Color.BLACK;
            textButtonStyle.down = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/down.png")), 35,35,35,35));
            textButtonStyle.up = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/up.png")), 35,35,35,35));
            textButtonStyle.over = textButtonStyle.down;
            calculatingButton = new TextButton("Find root", textButtonStyle);
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
            findRoot.setActor(calculatingButton);
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

        table.top();
    }

    public void calculating() {
        calculatingButton.setDisabled(true);
        calculatingButton.setText("(please wait)");
        String methodName = "fixedPointBonus";
        char[] input1 = functionTextField.getText().toCharArray();
        double input2 = Double.parseDouble(initialPointTextField.getText());
        double input3 = Double.parseDouble(numberOfIterations.getText());
        double input4 = 0;
        if (startPointTextField.getText().equals("")) {
            methodName = "fixedPoint";
        } else {
            input4 = Double.parseDouble(startPointTextField.getText());
        }
        double input5 = 0;
        if (endPointTextField.getText().equals("")) {
            methodName = "fixedPoint";
        } else {
            input5 = Double.parseDouble(endPointTextField.getText());
        }
        MatlabEngine engine = ((Project) Gdx.app.getApplicationListener()).getEngine();
        try {
            Object[]  object = new Object[1];
            if (methodName.equals("fixedPoint")) {
                object = engine.feval(3,  methodName, input1,input2,input3);
            } else if (methodName.equals("fixedPointBonus")) {
                object = engine.feval(3,  methodName, input1,input2,input3, input4, input5);
            }
            boolean isRootFinded = (Boolean) object[0];
            double root = (Double) object[1];
            Gdx.app.log("isRootFinded", String.valueOf(isRootFinded));
            Gdx.app.log("root", String.valueOf(root));
            showResult(isRootFinded, root);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            calculatingButton.setDisabled(false);
            calculatingButton.setText("Calculate");
        }
    }

    private void showResult(boolean isRootFinded, double root) {
        if (isRootFinded) {
            resultLabel.setText("Root : " + String.valueOf(root));
        } else {
            resultLabel.setText("Root was not found. Check your initial interval or \n maybe your chosen method is not appropriate.");
        }
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
