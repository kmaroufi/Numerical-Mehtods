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
 * Created by asus-pc on 12/7/2016.
 */
public class Chapter3InterpolationScreen implements Screen {
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
    private double[] pointx;
    private double[] pointy;

    private SelectBox selectBox;
    private TextField pointTextField;
    private Label resultPolynomialLabel;
    private Label resultValueLabel;
    private TextButton calculatingButton;

    public Chapter3InterpolationScreen(Screen parent, String function, double[] pointx, double[] pointy) {
        this.parent = parent;
        this.function = function;
        this.pointx = pointx;
        this.pointy = pointy;
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
        table.add().colspan(0).height(Util.multiplyToWorldFactor(50));

        table.row();
        //method label
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(15);
        Cell choose = table.add().height(Util.multiplyToWorldFactor(200)).colspan(10).right();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Choose your Method :  ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.7f));
            choose.setActor(label);
        }
        Cell choosingCell =  table.add().height(Util.multiplyToWorldFactor(70)).colspan(12);
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
            selectBox.setItems(new String[]{"Lagrange", "Newton Divided Differences",
                    "Newton Forward Differences", "Newton Backward Differences","Newton Central Differences"});
            choosingCell.setActor(selectBox);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(13);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(20));


        table.row();
        // optional point
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(11);
        Cell optionalPoint = table.add().height(Util.multiplyToWorldFactor(100)).colspan(14).right();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Enter your point to calculate its value :  ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            optionalPoint.setActor(label);
        }
        Cell x = table.add().height(Util.multiplyToWorldFactor(70)).colspan(12).fillX();
        {
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.font = font;
            textFieldStyle.messageFont = font;
            textFieldStyle.messageFontColor = Color.LIGHT_GRAY;
            textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
            textFieldStyle.fontColor = Color.BLACK;
            textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
            pointTextField = new TextField("", textFieldStyle);
            pointTextField.setMessageText("Enter X (optional)");
            x.setActor(pointTextField);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(13);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(20));

        table.row();
        //calculating
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(20);
        Cell calculating = table.add().height(Util.multiplyToWorldFactor(100)).colspan(10);
        {
            TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
            textButtonStyle.font  = font;
            textButtonStyle.fontColor = Color.BLACK;
            textButtonStyle.down = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/down.png")), 35,35,35,35));
            textButtonStyle.up = new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("button/up.png")), 35,35,35,35));
            textButtonStyle.over = textButtonStyle.down;
            calculatingButton = new TextButton("Interpolating", textButtonStyle);
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
//                    ((Game) Gdx.app.getApplicationListener()).setScreen(new Chapter1ResultScreen(Chapter1Screen.this));
                }
            });
            calculating.setActor(calculatingButton);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(20);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(20));

        table.row();
        //result
        table.add().height(Util.multiplyToWorldFactor(130)).colspan(15);
        Cell resultPolynomialCell = table.add().height(Util.multiplyToWorldFactor(130)).colspan(20);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            resultPolynomialLabel = new Label("", labelStyle);
            resultPolynomialLabel.setFontScale(Util.multiplyToWorldFactor(.6f));
            resultPolynomialCell.setActor(resultPolynomialLabel);
        }
        table.add().height(Util.multiplyToWorldFactor(130)).colspan(15);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(20));

        table.row();
        //result
        table.add().height(Util.multiplyToWorldFactor(130)).colspan(15);
        Cell resultValueCell = table.add().height(Util.multiplyToWorldFactor(130)).colspan(20);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            resultValueLabel = new Label("", labelStyle);
            resultValueLabel.setFontScale(Util.multiplyToWorldFactor(.6f));
            resultValueCell.setActor(resultValueLabel);
        }
        table.add().height(Util.multiplyToWorldFactor(130)).colspan(15);

        table.top();
    }


    public void calculating() {
        calculatingButton.setDisabled(true);
        calculatingButton.setText("(please wait)");
        String method = (String) selectBox.getSelected();
        //"Lagrange", "Newton Divided Differences",
        //"Newton Forward Differences", "Newton Backward Differences","Newton Central Differences"
        char[] input1 = function.toCharArray();
        double[] input2 = pointx;
        double[] input3 = pointy;
        boolean input4 = !pointTextField.getText().equals("");
        double input5 = 0;
        if (input4) {
            input5 = Double.valueOf(pointTextField.getText());
        }
        int input6 = Gdx.app.getPreferences("NumericalMethodsProject").getInteger("floating point digits");
        boolean input7 = false;
        if (!function.equals("")) {
            input7 = true;
        }
        MatlabEngine engine = ((Project) Gdx.app.getApplicationListener()).getEngine();
        try {
            String methodName = "";
            if (method.equals("Lagrange")) {
                methodName = "Lagrange";
            } else if (method.equals("Newton Divided Differences")) {
                methodName = "Newton_Divided_Differences";
            } else if (method.equals("Newton Forward Differences")) {
                methodName =  "Newton_Forward";
            } else if (method.equals("Newton Backward Differences")) {
                methodName =  "Newton_Backward";
            } else if (method.equals("Newton Central Differences")) {
                methodName =  "Newton_Central";
            }
            Object[] object = engine.feval(2,  methodName, input1,input2,input3, input4, input5, input6, input7);
            String result = (String) object[0];
            double value = (Double) object[1];
            Gdx.app.log("result", result);
            Gdx.app.log("value", String.valueOf(value));
            showResult(result, value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            calculatingButton.setDisabled(false);
            calculatingButton.setText("Calculate");
        }
    }

    private void showResult(String result, double value) {
        resultPolynomialLabel.setText("Polynomial : " + result);
        if (!pointTextField.getText().equals("")) {
            resultValueLabel.setText("f(x=" + pointTextField.getText() + ") = " + String.valueOf(value));
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
