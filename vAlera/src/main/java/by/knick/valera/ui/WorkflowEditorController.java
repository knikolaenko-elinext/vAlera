package by.knick.valera.ui;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.knick.valera.flow.CompositeStep;
import by.knick.valera.flow.DigStep;
import by.knick.valera.flow.Flow;
import by.knick.valera.flow.ListStep;
import by.knick.valera.flow.PaginationStep;
import by.knick.valera.flow.ScrapTextStep;
import by.knick.valera.flow.StartStep;
import by.knick.valera.flow.Step;
import by.knick.valera.flow.render.StepRenderer;
import by.knick.valera.flow.render.StepRenderersRegistry;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

public class WorkflowEditorController implements Initializable {
	protected static final Logger LOG = LoggerFactory.getLogger(WorkflowEditorController.class);

	@FXML
	private TreeView<Step> flowTreeView;
	@FXML
	private MenuItem removeStepMenuItem;

	@FXML
	private StackPane stepDetailsContainer;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		flowTreeView.setCellFactory(new Callback<TreeView<Step>, TreeCell<Step>>() {
			@Override
			public TreeCell<Step> call(TreeView<Step> param) {
				return new TreeCell<Step>() {
					@Override
					protected void updateItem(Step step, boolean empty) {
						super.updateItem(step, empty);
						if (step == null) {
							setText(null);
							return;
						}
						StepRenderer<Step> renderer = StepRenderersRegistry.getInstance().getRenderer(step);
						String text = renderer.renderToString(step);
						setText(text);
					}
				};
			}
		});

		flowTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			stepDetailsContainer.getChildren().clear();

			if (newValue != null) {
				Step selectedStep = newValue.getValue();
				StepRenderer<Step> stepRenderer = StepRenderersRegistry.getInstance().getRenderer(selectedStep);
				Parent stepWidget = stepRenderer.renderToDetailsWidget(selectedStep);
				stepDetailsContainer.getChildren().add(stepWidget);
			}
		});

		removeStepMenuItem.disableProperty().bind(Bindings.isNull(flowTreeView.getSelectionModel().selectedItemProperty()));
	}

	@FXML
	private void onStartBtnClicked(ActionEvent e) {
		String url = DashboardController.getInstance().getBrowserController().getCurrentUrl();
		StartStep step = new StartStep("", url);
		DashboardController.getInstance().getProject().getConfig().getFlow().addChildStep(step);
		refillTreeView();
	}

	@FXML
	private void onPaginationBtnClicked() {
		TreeItem<Step> selectedTreeItem = flowTreeView.getSelectionModel().getSelectedItem();
		if (selectedTreeItem == null) {
			return;
		}

		PaginationStep newStep = new PaginationStep();

		TreeItem<Step> newItem = new TreeItem<Step>(newStep);
		TreeItem<Step> targetItem = findNearestCompositeItem(selectedTreeItem);
		CompositeStep targetStep = (CompositeStep) targetItem.getValue();
		targetStep.addChildStep(newStep);
		targetItem.getChildren().add(newItem);
		flowTreeView.refresh();
		flowTreeView.getSelectionModel().clearSelection();
		flowTreeView.getSelectionModel().select(newItem);
	}

	@FXML
	private void onListBtnClicked(ActionEvent e) {
		TreeItem<Step> selectedTreeItem = flowTreeView.getSelectionModel().getSelectedItem();
		if (selectedTreeItem == null) {
			return;
		}

		ListStep newStep = new ListStep();

		TreeItem<Step> newItem = new TreeItem<Step>(newStep);
		TreeItem<Step> targetItem = findNearestCompositeItem(selectedTreeItem);
		CompositeStep targetStep = (CompositeStep) targetItem.getValue();
		targetStep.addChildStep(newStep);
		targetItem.getChildren().add(newItem);
		flowTreeView.refresh();
		flowTreeView.getSelectionModel().clearSelection();
		flowTreeView.getSelectionModel().select(newItem);
	}

	@FXML
	private void onDigBtnClicked() {
		TreeItem<Step> selectedTreeItem = flowTreeView.getSelectionModel().getSelectedItem();
		if (selectedTreeItem == null) {
			return;
		}

		DigStep newStep = new DigStep();

		TreeItem<Step> newItem = new TreeItem<Step>(newStep);
		TreeItem<Step> targetItem = findNearestCompositeItem(selectedTreeItem);
		CompositeStep targetStep = (CompositeStep) targetItem.getValue();
		targetStep.addChildStep(newStep);
		targetItem.getChildren().add(newItem);
		flowTreeView.refresh();
		flowTreeView.getSelectionModel().clearSelection();
		flowTreeView.getSelectionModel().select(newItem);
	}

	@FXML
	private void onScrapBtnClicked(ActionEvent e) {
		TreeItem<Step> selectedTreeItem = flowTreeView.getSelectionModel().getSelectedItem();
		if (selectedTreeItem == null) {
			return;
		}

		ScrapTextStep newStep = new ScrapTextStep();

		TreeItem<Step> newItem = new TreeItem<Step>(newStep);
		TreeItem<Step> targetItem = findNearestCompositeItem(selectedTreeItem);
		CompositeStep targetStep = (CompositeStep) targetItem.getValue();
		targetStep.addChildStep(newStep);
		targetItem.getChildren().add(newItem);
		flowTreeView.refresh();
		flowTreeView.getSelectionModel().clearSelection();
		flowTreeView.getSelectionModel().select(newItem);
	}

	private static TreeItem<Step> findNearestCompositeItem(TreeItem<Step> selectedTreeItem) {
		TreeItem<Step> targetItem = null;
		TreeItem<Step> currentItem = selectedTreeItem;

		while (targetItem == null) {
			if (currentItem.getValue() instanceof CompositeStep) {
				targetItem = currentItem;
				break;
			}
			targetItem = currentItem.getParent();
		}
		return targetItem;
	}

	public void refillTreeView() {
		Flow flow = DashboardController.getInstance().getProject().getConfig().getFlow();
		TreeItem<Step> item = builtTreeItem(flow);
		flowTreeView.setRoot(item);
	}

	private TreeItem<Step> builtTreeItem(Step step) {
		TreeItem<Step> item = new TreeItem<Step>(step);
		item.setExpanded(true);
		if (step instanceof CompositeStep) {
			CompositeStep compositeStep = (CompositeStep) step;
			for (Step childStep : compositeStep.getChildSteps()) {
				TreeItem<Step> childItem = builtTreeItem(childStep);
				item.getChildren().add(childItem);
			}
		}
		return item;
	}

	@FXML
	private void onRemoveStepMenuClicked() {
		TreeItem<Step> selectedItem = flowTreeView.getSelectionModel().getSelectedItem();
		TreeItem<Step> selectedItemParent = selectedItem.getParent();
		if (selectedItemParent != null) {
			Step selectedStep = selectedItem.getValue();
			CompositeStep parentStep = (CompositeStep) selectedItemParent.getValue();

			parentStep.getChildSteps().remove(selectedStep);
			selectedItemParent.getChildren().remove(selectedItem);
		}
	}

	public void refreshTreeView() {
		flowTreeView.refresh();
	}
}
