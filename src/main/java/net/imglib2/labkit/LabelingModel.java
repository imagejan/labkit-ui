package net.imglib2.labkit;

import net.imglib2.labkit.color.ColorMapProvider;
import net.imglib2.labkit.labeling.Labeling;

public interface LabelingModel {

	Holder<String> selectedLabel();

	ColorMapProvider colorMapProvider();

	Holder<Labeling> labeling();

	void requestRepaint();

	Notifier<Runnable> dataChangedNotifier();
}
