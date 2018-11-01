/*******************************************************************************
 * Copyright (c) 2018-11-01 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform.
 ******************************************************************************/
package gitbucket.core.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * TextAvatarUtilHelper
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-01 auto generate by qdp.
 */
public class TextAvatarUtil {

	private static final int iconSize = 200;
	private static final int fontSize = 180;
	private static final int roundSize = 60;
	private static final int shadowSize = 20;
	private static final float bgSaturation = 0.68f;
	private static final float bgBlightness = 0.73f;
	private static final float shadowBlightness = 0.23f;
	private static final Font font = new Font(Font.SANS_SERIF, Font.PLAIN, fontSize);
	private static final Color transparent = new Color(0, 0, 0, 0);

	// https://www.w3.org/TR/2008/REC-WCAG20-20081211/#relativeluminancedef
	private static double relativeLuminance(Color c) {
		double[] rgb = new double[] { (double) c.getRed(), (double) c.getGreen(), (double) c.getBlue() };
		for (int i = 0; i < rgb.length; i++) {
			double x = (int) rgb[i] / 255.0D;
			if (x <= 0.03928) {
				rgb[i] = x / 12.92;
			} else {
				rgb[i] = Math.pow((x + 0.055) / 1.055, 2.4);
			}
		}
		return 0.2126 * rgb[0] + 0.7152 * rgb[1] + 0.0722 * rgb[2];
	}

	// https://www.w3.org/TR/2008/REC-WCAG20-20081211/#contrast-ratiodef
	private static double contrastRatio(Color c1, Color c2) {
		double l1 = relativeLuminance(c1);
		double l2 = relativeLuminance(c2);
		if (l1 > l2) {
			return (l1 + 0.05) / (l2 + 0.05);
		} else {
			return (l2 + 0.05) / (l1 + 0.05);
		}
	}

	private static Color goodContrastColor(Color base, Color c1, Color c2) {
		if (contrastRatio(base, c1) > contrastRatio(base, c2)) {
			return c1;
		} else {
			return c2;
		}
	}

	private static float strToHue(String text) {
		return Integer.parseInt(StringUtil.md5(text).substring(0, 2), 16) / 256f;
	}

	// gitbucket.core.util.TextAvatarUtil.getCenterToDraw
	private static int[] getCenterToDraw(String drawText, Font font, int w, int h) {
		FontRenderContext context = new FontRenderContext(new AffineTransform(), true, true);
		TextLayout txt = new TextLayout(drawText, font, context);

		Rectangle2D bounds = txt.getBounds();

		int x = Double.valueOf(((double) w - bounds.getWidth()) / 2 - bounds.getX()).intValue();
		int y = Double.valueOf(((double) h - bounds.getHeight()) / 2 - bounds.getY()).intValue();

		return new int[] { x, y };
	}

	private static byte[] textImage(String drawText, Color bgColor, Color fgColor) {
		BufferedImage canvas = new BufferedImage(iconSize, iconSize, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = canvas.createGraphics();
		int[] center = getCenterToDraw(drawText, font, iconSize, iconSize);

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(transparent);
		g.fillRect(0, 0, iconSize, iconSize);

		g.setColor(bgColor);
		g.fillRoundRect(0, 0, iconSize, iconSize, roundSize, roundSize);

		g.setColor(fgColor);
		g.setFont(font);
		g.drawString(drawText, center[0], center[1]);

		g.dispose();

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			ImageIO.write(canvas, "png", stream);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return stream.toByteArray();
	}

	public static byte[] textAvatar(String nameText) {
		String drawText = nameText.substring(0, 1);

		float bgHue = strToHue(nameText);
		Color bgColor = Color.getHSBColor(bgHue, bgSaturation, bgBlightness);
		Color fgColor = goodContrastColor(bgColor, Color.BLACK, Color.WHITE);

		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, fontSize);
		if (font.canDisplayUpTo(drawText) == -1) {
			return textImage(drawText, bgColor, fgColor);
		} else {
			return new byte[0];
		}
	}

	private static byte[] textGroupImage(String drawText, Color bgColor, Color fgColor, Color shadowColor) {
		BufferedImage canvas = new BufferedImage(iconSize, iconSize, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = canvas.createGraphics();
		int[] center = getCenterToDraw(drawText, font, iconSize - shadowSize, iconSize - shadowSize);

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(transparent);
		g.fillRect(0, 0, iconSize, iconSize);

		g.setColor(shadowColor);
		g.fillRect(shadowSize, shadowSize, iconSize, iconSize);

		g.setColor(bgColor);
		g.fillRect(0, 0, iconSize - shadowSize, iconSize - shadowSize);

		g.setColor(fgColor);

		g.setFont(font);
		g.drawString(drawText, center[0], center[1]);

		g.dispose();

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			ImageIO.write(canvas, "png", stream);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return stream.toByteArray();
	}

	public static byte[] textGroupAvatar(String nameText) {
		String drawText = nameText.substring(0, 1);

		float bgHue = strToHue(nameText);
		Color bgColor = Color.getHSBColor(bgHue, bgSaturation, bgBlightness);
		Color fgColor = goodContrastColor(bgColor, Color.BLACK, Color.WHITE);
		Color shadowColor = Color.getHSBColor(bgHue, bgSaturation, shadowBlightness);

		if (font.canDisplayUpTo(drawText) == -1) {
			return textGroupImage(drawText, bgColor, fgColor, shadowColor);
		} else {
			return new byte[0];
		}
	}
}
