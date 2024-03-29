package fr.mrcraftcod.queue;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.Taskbar;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-12.
 *
 * @author Thomas Couchoud
 * @since 2018-11-12
 */
public class Main extends Application{
	private final static NumberFormat RESULT_FORMAT = new DecimalFormat("##.#####");
	private final static NumberFormat PERCENT_FORMAT = new DecimalFormat("##.##%");
	private Stage stage;
	
	public static void main(String[] args){
		Application.launch(args);
	}
	
	public void start(Stage stage) throws Exception{
		this.stage = stage;
		Scene scene = new Scene(createContent());
		// scene.setFill(Color.TRANSPARENT);
		stage.setTitle("Queues");
		stage.setScene(scene);
		// stage.initStyle(StageStyle.TRANSPARENT);
		stage.setResizable(false);
		stage.sizeToScene();
		final var icon = getIcon();
		if(Objects.nonNull(icon)){
			setIcon(icon);
		}
		//stage.setResizable(false);
		stage.getScene().getStylesheets().add(Main.class.getResource("/jfx/base.css").toExternalForm());
		stage.show();
	}
	
	private Image getIcon(){
		try{
			return SwingFXUtils.toFXImage(ImageIO.read(Main.class.getResource("/jfx/mohand.png")), null);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("Duplicates")
	private Parent createContent(){
		final var noErrorImage1 = new Image(this.getClass().getResource("/done.png").toExternalForm());
		final var errorImage1 = new Image(this.getClass().getResource("/noDone.png").toExternalForm());
		final var doneImageView = new ImageView();
		doneImageView.setImage(noErrorImage1);
		doneImageView.setPreserveRatio(true);
		doneImageView.setFitHeight(500);
		
		final var noErrorImage2 = new Image(this.getClass().getResource("/noError.png").toExternalForm());
		final var errorImage2 = new Image(this.getClass().getResource("/error.jpg").toExternalForm());
		final var errorImageView = new ImageView();
		errorImageView.setImage(noErrorImage2);
		errorImageView.setPreserveRatio(true);
		errorImageView.setFitHeight(500);
		
		var lineIndex = -1;
		final var s = new Text("Server count: ");
		final var sInput = new NumberField<>(1);
		sInput.getStylesheets().add(Main.class.getResource("/jfx/base.css").toExternalForm());
		sInput.setMaxWidth(Double.MAX_VALUE);
		GridPane.setConstraints(s, 0, ++lineIndex);
		GridPane.setConstraints(sInput, 1, lineIndex);
		GridPane.setFillWidth(sInput, true);
		GridPane.setHgrow(sInput, Priority.ALWAYS);
		
		final var queueLimit = new Text("System limit: ");
		final var queueLimitInput = new NumberField<Double>(null);
		queueLimitInput.setMaxWidth(Double.MAX_VALUE);
		GridPane.setConstraints(queueLimit, 0, ++lineIndex);
		GridPane.setConstraints(queueLimitInput, 1, lineIndex);
		GridPane.setFillWidth(queueLimitInput, true);
		GridPane.setHgrow(queueLimitInput, Priority.ALWAYS);
		
		final var lambda = new Text("Lambda: ");
		final var lambdaInput = new DecimalNumberField(0.9D);
		lambdaInput.setMaxWidth(Double.MAX_VALUE);
		GridPane.setConstraints(lambda, 0, ++lineIndex);
		GridPane.setConstraints(lambdaInput, 1, lineIndex);
		GridPane.setFillWidth(lambdaInput, true);
		GridPane.setHgrow(lambdaInput, Priority.ALWAYS);
		
		final var mu = new Text("Mu: ");
		final var muInput = new DecimalNumberField(1D);
		muInput.setMaxWidth(Double.MAX_VALUE);
		GridPane.setConstraints(mu, 0, ++lineIndex);
		GridPane.setConstraints(muInput, 1, lineIndex);
		GridPane.setFillWidth(muInput, true);
		GridPane.setHgrow(muInput, Priority.ALWAYS);
		
		final var calculating = new Image(this.getClass().getResource("/loading.gif").toExternalForm());
		final var noCalculating = new Image(this.getClass().getResource("/noCalc.gif").toExternalForm());
		final var gifLoading = new ImageView();
		gifLoading.setImage(calculating);
		gifLoading.setPreserveRatio(true);
		gifLoading.setFitWidth(400);
		final var validButton = new Button("Run");
		validButton.setMaxWidth(Double.MAX_VALUE);
		validButton.setDefaultButton(true);
		GridPane.setConstraints(validButton, 0, ++lineIndex, 2, 1);
		GridPane.setFillWidth(validButton, true);
		GridPane.setHgrow(validButton, Priority.ALWAYS);
		GridPane.setConstraints(gifLoading, 0, ++lineIndex, 2, 1);
		GridPane.setFillWidth(gifLoading, true);
		GridPane.setHgrow(gifLoading, Priority.ALWAYS);
		
		final var l = new Text("Average system size: ");
		final var lOutput = new Text();
		final var lProgress = new CircleProgress();
		GridPane.setConstraints(l, 0, ++lineIndex);
		GridPane.setConstraints(lOutput, 1, lineIndex);
		GridPane.setConstraints(lProgress, 0, ++lineIndex, 2, 1);
		GridPane.setFillWidth(lProgress, true);
		GridPane.setHgrow(lProgress, Priority.ALWAYS);
		
		final var lq = new Text("Average queue size: ");
		final var lqOutput = new Text();
		final var lqProgress = new CircleProgress();
		GridPane.setConstraints(lq, 0, ++lineIndex);
		GridPane.setConstraints(lqOutput, 1, lineIndex);
		GridPane.setConstraints(lqProgress, 0, ++lineIndex, 2, 1);
		GridPane.setFillWidth(lqProgress, true);
		GridPane.setHgrow(lqProgress, Priority.ALWAYS);
		
		final var ref = new Text("Average refused: ");
		final var refOutput = new Text();
		GridPane.setConstraints(ref, 0, ++lineIndex);
		GridPane.setConstraints(refOutput, 1, lineIndex);
		
		final var w = new Text("Average wait time in system: ");
		final var wOutput = new Text();
		GridPane.setConstraints(w, 0, ++lineIndex);
		GridPane.setConstraints(wOutput, 1, lineIndex);
		
		final var wq = new Text("Average wait time in queue: ");
		final var wqOutput = new Text();
		GridPane.setConstraints(wq, 0, ++lineIndex);
		GridPane.setConstraints(wqOutput, 1, lineIndex);
		
		final Callable<Void> runnable = () -> {
			try{
				sInput.getStyleClass().remove("invalidState");
				queueLimitInput.getStyleClass().remove("invalidState");
				lambdaInput.getStyleClass().remove("invalidState");
				muInput.getStyleClass().remove("invalidState");
				lOutput.setText("");
				lProgress.clear();
				lqOutput.setText("");
				lqProgress.clear();
				wOutput.setText("");
				wqOutput.setText("");
				refOutput.setText("");
				doneImageView.setImage(noErrorImage1);
				errorImageView.setImage(noErrorImage2);
				gifLoading.setImage(calculating);
				
				final var output = Computation.compute(new QueueInput(ProbabilityLaw.POISSON, ProbabilityLaw.EXP, sInput.getInt(), lambdaInput.getDouble(), muInput.getDouble(), queueLimitInput.getInt()));
				lOutput.setText(RESULT_FORMAT.format(output.getL()));
				lProgress.setCount(output.getL());
				lqOutput.setText(RESULT_FORMAT.format(output.getLq()));
				lqProgress.setCount(output.getLq());
				wOutput.setText(RESULT_FORMAT.format(output.getW()));
				wqOutput.setText(RESULT_FORMAT.format(output.getWq()));
				refOutput.setText(Optional.ofNullable(output.getRef()).map(PERCENT_FORMAT::format).orElse("Undefined"));
			}
			catch(BadInputException e2){
				doneImageView.setImage(errorImage1);
				errorImageView.setImage(errorImage2);
				gifLoading.setImage(noCalculating);
				for(var i : e2.getFields()){
					switch(i){
						case S:
							sInput.getStyleClass().add("invalidState");
							break;
						case MU:
							muInput.getStyleClass().add("invalidState");
							break;
						case LAMBDA:
							lambdaInput.getStyleClass().add("invalidState");
							break;
						case QUEUE_LIMIT:
							queueLimitInput.getStyleClass().add("invalidState");
							break;
						default:
					}
				}
				throw e2;
			}
			catch(Exception e2){
				e2.printStackTrace();
				throw e2;
			}
			return null;
		};
		
		ChangeListener<String> changeListener = (observableValue, s1, t1) -> {
			try{
				runnable.call();
			}
			catch(BadInputException e2){
				System.err.println(e2.getMessage());
			}
			catch(Exception e2){
				e2.printStackTrace();
			}
		};
		
		sInput.textProperty().addListener(changeListener);
		queueLimitInput.textProperty().addListener(changeListener);
		lambdaInput.textProperty().addListener(changeListener);
		muInput.textProperty().addListener(changeListener);
		
		validButton.setOnAction(e -> {
			try{
				runnable.call();
			}
			catch(BadInputException e2){
				System.err.println(e2.getMessage());
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Computation error");
				alert.setHeaderText("Some fields are not completed correctly or lead to an invalid state");
				alert.setContentText(e2.getMessage());
				alert.showAndWait();
			}
			catch(Exception e2){
				e2.printStackTrace();
			}
		});
		
		GridPane inputs = new GridPane();
		inputs.setVgap(10);
		inputs.getChildren().addAll(s, sInput, queueLimit, queueLimitInput, lambda, lambdaInput, mu, muInput, /*validButton,*/gifLoading, l, lOutput, lProgress, lq, lqOutput, lqProgress, w, wOutput, wq, wqOutput, ref, refOutput);
		inputs.setMaxWidth(Double.MAX_VALUE);
		
		HBox root = new HBox(10);
		root.getChildren().addAll(doneImageView, inputs, errorImageView);
		
		VBox.setVgrow(inputs, Priority.ALWAYS);
		HBox.setHgrow(inputs, Priority.ALWAYS);
		
		validButton.fire();
		
		return root;
	}
	
	private void setIcon(Image icon){
		try{
			this.stage.getIcons().clear();
			this.stage.getIcons().add(icon);
			Taskbar.getTaskbar().setIconImage(SwingFXUtils.fromFXImage(icon, null));
		}
		catch(UnsupportedOperationException e){
		
		}
	}
}
