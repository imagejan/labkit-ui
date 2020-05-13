
package demo.mats_1;

import bdv.util.BdvFunctions;
import bdv.util.BdvOptions;
import bdv.util.BdvStackSource;
import ij.ImagePlus;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.VirtualStackAdapter;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.labkit.labeling.Label;
import net.imglib2.labkit.labeling.Labeling;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.ARGBType;

import java.io.IOException;
import java.util.Arrays;

/**
 * This demo shows an simplified "Labkit" window. The "drawings" made by the
 * user are shown after the main window is closed.
 */
public class MatsDemo1 {

	public static void main(String... args) throws IOException {

		// open the image
		// String pathOrURL = "https://imagej.nih.gov/ij/images/blobs.gif");
		// String pathOrURL =
		// "https://cgcweb.med.tu-dresden.de/cloud/index.php/s/G2ML3C6yYtnZW82/download?path=%2F&files=original1.tif");
		// String pathOrURL =
		// "https://cgcweb.med.tu-dresden.de/cloud/index.php/s/G2ML3C6yYtnZW82/download?path=%2F&files=original2.tif");
		// String pathOrURL =
		// "/home/mats/Dokumente/postdoc/conferences/fiji_hackathon_dd2019/project_BayesImaging/sampledata/1h+S7_und_S8_tiff+S7+S7_NaCl+/original1.tif");
		// String pathOrURL =
		// "https://cgcweb.med.tu-dresden.de/cloud/index.php/s/G2ML3C6yYtnZW82/download?path=%2Fbsp3&files=nativ.tif");
		String pathOrURL = "/home/arzt/Desktop/original1.tif";
		final ImagePlus image = new ImagePlus(pathOrURL);
		// create empty labeling with a background label and the same size as the
		// image
		Labeling labeling = Labeling.createEmpty(Arrays.asList("background"),
			new FinalInterval(image.getWidth(), image.getHeight()));

		// set the labels color to blue
		Label backgroundLabel = labeling.getLabel("background");
		backgroundLabel.setColor(new ARGBType(0x0000cc));

		// show a window containing all the UI
		LabelingDialog dialog = new LabelingDialog(image, labeling);
		dialog.setSize(800, 600);
		dialog.setModal(true);
		dialog.setVisible(true);

		// get a black and white image, white = background
		RandomAccessibleInterval<BitType> background = labeling.getRegion(
			backgroundLabel);

		// convert to ImagePlus and show
		ImagePlus manual_segmented_image = ImageJFunctions.wrap(background,
			"segmented");

		//// The labeling can be saved to a file and loaded:
		// LabelingSerializer serializer = new LabelingSerializer(new Context());
		// serializer.save(labeling, "some_file.labeling");
		// Labeling labeling2 = serializer.open("some_file.labeling");

		/*START MATS PART HERE:
		* I am convinced this can be done better, but I am not a professional programmer.
		* What follows here is the an implementation in the RadFriends algorithm.
		* Check out: Skilling J. 2006. Nested Sampling for General Bayesian Computation. Bayesian Analysis - International Society for Bayesian Analysis 1:833–860.
		*  */

		BiisSegmenter segmenter = new BiisSegmenter();
		segmenter.train(image, manual_segmented_image);
		ImagePlus pgi = segmenter.apply(image);

		BdvStackSource<?> handle = BdvFunctions.show(VirtualStackAdapter.wrap(
			image), image.getTitle(), BdvOptions.options().is2D());
		BdvFunctions.show(VirtualStackAdapter.wrap(pgi), image.getTitle(),
			BdvOptions.options().addTo(handle.getBdvHandle())).setColor(new ARGBType(0x770000));
	}
}
