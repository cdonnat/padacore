package org.padacore.ui.editor;

import org.ada4j.api.model.ICompilationUnit;
import org.ada4j.api.model.INamedUnit;
import org.ada4j.api.model.IPackage;
import org.ada4j.api.model.ISubprogram;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.google.common.base.Preconditions;

/**
 * This class provides labels for the standard "Outline" view used with Ada
 * editor (packages, procedures...).
 * 
 * @author RS
 *
 */
public class AdaOutlineLabelProvider implements ILabelProvider {

	private final static String ICONS_PATH = "../../../../../icons/";
	private final static String PUBLIC_SUBPROGRAM_ICON_PATH = ICONS_PATH
			+ "methpub_obj.gif";
	private final static String PRIVATE_SUBPROGRAM_ICON_PATH = ICONS_PATH
			+ "methpri_obj.gif";
	private final static String PACKAGE_ICON_PATH = ICONS_PATH
			+ "package_obj.gif";
	private final static String FILE_ICON_PATH = ICONS_PATH + "file_obj.gif";

	private Image image;

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
		if (this.image != null && !this.image.isDisposed()) {
			this.image.dispose();
		}
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

	@Override
	public Image getImage(Object element) {
		if (ISubprogram.class.isAssignableFrom(element.getClass())) {
			ISubprogram subprogram = (ISubprogram) element;
			if (subprogram.isPrivate()) {
				this.image = new Image(Display.getCurrent(), this.getClass()
						.getResourceAsStream(PRIVATE_SUBPROGRAM_ICON_PATH));
			} else {
				this.image = new Image(Display.getCurrent(), this.getClass()
						.getResourceAsStream(PUBLIC_SUBPROGRAM_ICON_PATH));
			}
		} else if (IPackage.class.isAssignableFrom(element.getClass())) {
			this.image = new Image(Display.getCurrent(),
					this.getClass().getResourceAsStream(PACKAGE_ICON_PATH));
		} else if (ICompilationUnit.class
				.isAssignableFrom(element.getClass())) {
			this.image = new Image(Display.getCurrent(),
					this.getClass().getResourceAsStream(FILE_ICON_PATH));
		}

		return this.image;
	}

	@Override
	public String getText(Object element) {
		Preconditions.checkArgument(
				INamedUnit.class.isAssignableFrom(element.getClass()));

		return ((INamedUnit) element).getName();
	}

}
