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
import com.badlogic.gdx.utils.Align;
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
public class Chapter6Methods2Screen implements Screen {
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
    private Integer numberOfVariables;
    private ArrayList<TextField> initialValuesTextField = new ArrayList<TextField>();
    private TextField numberOfStepsTextField;
    private ArrayList<Label> resultLabels = new ArrayList<Label>();


    private ArrayList<TextField[]> aArray = new ArrayList<TextField[]>();
    private ArrayList<TextField> bArray = new ArrayList<TextField>();
    private TextButton calculatingButton;

    public Chapter6Methods2Screen(Screen parent, String function, Integer numberOfVariables) {
        this.parent = parent;
        this.function = function;
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
            Label label = new Label("Chapter6", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(1.1f));
            pageTitr.setActor(label);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(15);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(20));

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(20));

        table.row();
        //method label
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(15);
        Cell methodTitr = table.add().height(Util.multiplyToWorldFactor(100)).colspan(20);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Method : " + function, labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.9f));
            methodTitr.setActor(label);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(15);

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(10));

        for (int i = 0; i < numberOfVariables; i++) {
            table.row();
            //padding
//            table.add().colspan(0).height(Util.multiplyToWorldFactor(20));
            createFieldsOfEquations();
        }

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(60));

        table.row();
        // initial cell
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(5);
        Cell initialsCell = table.add().height(Util.multiplyToWorldFactor(100)).colspan(40);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Initial guesses", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.7f));
            initialsCell.setActor(label);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(5);

        for (int i = 0; i < numberOfVariables; i++) {
            table.row();
            createInitialFields("a" + String.valueOf(i + 1));
        }

        table.row();
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(10));

        table.row();
        //number of steps
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(20);
        Cell numberOfStepsLabel = table.add().height(Util.multiplyToWorldFactor(100)).colspan(5).right();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Enter number of steps :  ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            numberOfStepsLabel.setActor(label);
        }
        Cell numberOfStepsTextCell =  table.add().height(Util.multiplyToWorldFactor(70)).colspan(5).fillX();
        {
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.font = font;
            textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
            textFieldStyle.fontColor = Color.BLACK;
            textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
            numberOfStepsTextField = new TextField("", textFieldStyle);
            numberOfStepsTextCell.setActor(numberOfStepsTextField);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(13);

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
        //padding
        table.add().colspan(0).height(Util.multiplyToWorldFactor(30));

        table.row();
        // parameters cell
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(5);
        Cell parametersCell = table.add().height(Util.multiplyToWorldFactor(100)).colspan(40);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label("Answers", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.7f));
            resultLabels.add(label);
            label.setVisible(false);
            parametersCell.setActor(label);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(5);

        for (int i = 0; i < numberOfVariables; i++) {
            table.row();
            //variable
            createVariablesResult("a" + String.valueOf(i + 1), "?");

            table.row();
            //padding
            table.add().colspan(0).height(Util.multiplyToWorldFactor(10));
        }

        table.top();
    }

    public void createInitialFields(String variableName) {
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(14);
        Cell variableLabel = table.add().height(Util.multiplyToWorldFactor(100)).colspan(5).right();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label(variableName + " =  ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            variableLabel.setActor(label);
        }
        Cell variableTextCell =  table.add().height(Util.multiplyToWorldFactor(70)).colspan(13).fillX();
        {
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.font = font;
            textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
            textFieldStyle.fontColor = Color.BLACK;
            textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
            TextField textField = new TextField("", textFieldStyle);
            initialValuesTextField.add(textField);
            variableTextCell.setActor(textField);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(18);
    }

    public void createVariablesResult(String variableName, String value) {
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(18);
        Cell valueCell = table.add().height(Util.multiplyToWorldFactor(100)).colspan(14).left().fillX();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            labelStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture("param.png")));
            Label label = new Label(variableName + " = " + value, labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            label.setAlignment(Align.center);
            resultLabels.add(label);
            label.setVisible(false);
            valueCell.setActor(label);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(18);
    }

    private void calculating() {
        calculatingButton.setDisabled(true);
        calculatingButton.setText("(please wait)");
        Integer input1 = numberOfVariables;

        double[][] input2 = new double[numberOfVariables][numberOfVariables];
        for (int i = 0; i < numberOfVariables; i++) {
            TextField[] textFields = aArray.get(i);
            for (int j = 0; j < numberOfVariables; j++) {
                input2[i][j] = Double.parseDouble(textFields[j].getText());
            }
        }

        double[][] input3 = new double[numberOfVariables][1];
        for (int i = 0; i < numberOfVariables; i++) {
            input3[i][0] = Double.parseDouble(bArray.get(i).getText());
        }

        double[][] input4 = new double[numberOfVariables][1];
        for (int i = 0; i < numberOfVariables; i++) {
            input4[i][0] = Double.parseDouble(initialValuesTextField.get(i).getText());
        }

        Integer input5 = Integer.valueOf(numberOfStepsTextField.getText());

        int input6 = Gdx.app.getPreferences("NumericalMethodsProject").getInteger("floating point digits");
        MatlabEngine engine = ((Project) Gdx.app.getApplicationListener()).getEngine();
        String method = null;
        if (function.equals("Jacobi")){
            method = "Jacobi";
        } else if (function.equals("Gauss Seidel")) {
            method = "GaussSeidel";
        }
        try {
            Object[] result = engine.feval(2, method, input1,input2,input3, input4, input5, input6);
            String[] answer = (String[]) result[0];
            String error = (String) result[1];
            showResult(answer, error);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }   finally {
            calculatingButton.setDisabled(false);
            calculatingButton.setText("Calculate");
        }
    }

    private void showResult(String[] result, String error) {
        if (error.equals("nothing")) {
            for (int i = 0; i < resultLabels.size(); i++) {
                resultLabels.get(i).setVisible(true);
                if (i == 0) {
                    resultLabels.get(0).setText("Answers:");
                    continue;
                }
                resultLabels.get(i).setText("a" + String.valueOf(i) + " = " + result[i - 1]);
            }
        } else {
            for (int i = 0; i < resultLabels.size(); i++) {
                resultLabels.get(i).setVisible(false);
            }
            resultLabels.get(0).setVisible(true);
            resultLabels.get(0).setText("Error : " + error);
        }
    }

    public void createFieldsOfEquations() {
        int offset = (int) ((47 - 5 * numberOfVariables) / 2.0f);
        if (offset < 2) {
            offset = 2;
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(offset);
        TextField[] textFields = new TextField[numberOfVariables];
        for (int i = 0; i < numberOfVariables; i++) {
            Cell coefficient = table.add().height(Util.multiplyToWorldFactor(70)).colspan(3).fillX();
            {
                TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
                textFieldStyle.font = font;
                textFieldStyle.messageFont = font;
                textFieldStyle.messageFontColor = Color.LIGHT_GRAY;
                textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("white.png"))));
                textFieldStyle.fontColor = Color.BLACK;
                textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
                TextField textField = new TextField("", textFieldStyle);
//                textField.setMessageText("");
                textFields[i] = textField;
                coefficient.setActor(textField);
            }
            Cell variable = table.add().height(Util.multiplyToWorldFactor(100)).colspan(1).left();
            {
                Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
                Label label = new Label("a" + String.valueOf(i + 1), labelStyle);
                label.setFontScale(Util.multiplyToWorldFactor(.5f));
                variable.setActor(label);
            }
            if (i + 1 == numberOfVariables) {
                break;
            }
            Cell symbol = table.add().height(Util.multiplyToWorldFactor(100)).colspan(1);
            {
                Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
                Label label = new Label(" + ", labelStyle);
                label.setFontScale(Util.multiplyToWorldFactor(.5f));
                symbol.setActor(label);
            }
        }
        aArray.add(textFields);

        Cell symbol = table.add().height(Util.multiplyToWorldFactor(100)).colspan(1);
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
            Label label = new Label(" = ", labelStyle);
            label.setFontScale(Util.multiplyToWorldFactor(.5f));
            symbol.setActor(label);
        }
        Cell coefficient = table.add().height(Util.multiplyToWorldFactor(70)).colspan(3).fillX();
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
            bArray.add(textField);
            coefficient.setActor(textField);
        }
        table.add().height(Util.multiplyToWorldFactor(100)).colspan(offset);
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
