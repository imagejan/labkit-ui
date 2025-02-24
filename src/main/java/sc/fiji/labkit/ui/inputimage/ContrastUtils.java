/*-
 * #%L
 * The Labkit image segmentation tool for Fiji.
 * %%
 * Copyright (C) 2017 - 2022 Matthias Arzt
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package sc.fiji.labkit.ui.inputimage;

import bdv.util.BdvOptions;
import bdv.util.BdvSource;
import bdv.util.BdvStackSource;
import net.imagej.ImgPlus;
import net.imglib2.Interval;
import sc.fiji.labkit.ui.bdv.BdvShowable;
import net.imglib2.realtransform.AffineTransform3D;
import net.imglib2.type.numeric.NumericType;

/**
 * Helper for {@link DatasetInputImage} that picks the correct contrast when
 * showing an ImgPlus.
 */
class ContrastUtils {

	public static double getMin(ImgPlus<?> image) {
		double min = 0;
		for (int i = 0;; i++) {
			double value = image.getChannelMinimum(i);
			if (Double.isNaN(value)) break;
			min = Math.min(value, min);
		}
		return min;
	}

	public static double getMax(ImgPlus<? extends NumericType<?>> image) {
		double max = 0;
		for (int i = 0;; i++) {
			double value = image.getChannelMaximum(i);
			if (Double.isNaN(value)) break;
			max = Math.max(value, max);
		}
		return max;
	}

	public static BdvShowable showableAddSetDisplayRange(BdvShowable wrap,
		double min, double max)
	{
		return new BdvShowable() {

			@Override
			public Interval interval() {
				return wrap.interval();
			}

			@Override
			public AffineTransform3D transformation() {
				return wrap.transformation();
			}

			@Override
			public BdvStackSource<?> show(String title, BdvOptions options) {
				final BdvStackSource<?> result = wrap.show(title, options);
				result.setDisplayRange(min, max);
				return result;
			}
		};
	}
}
