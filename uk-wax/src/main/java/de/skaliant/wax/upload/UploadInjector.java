package de.skaliant.wax.upload;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

import de.skaliant.wax.app.FileUpload;
import de.skaliant.wax.util.beans.exp.BeanExpression;
import de.skaliant.wax.util.beans.exp.BeanExpressionParser;
import de.skaliant.wax.util.logging.Log;


/**
 * What the Injector does for parameter and path information, the UploadInjector
 * does for file uploads: try to inject data on controller properties. This
 * time, data is uploaded files.
 *
 * @author Udo Kastilan
 */
public class UploadInjector {
	private final Log LOG = Log.get(UploadInjector.class);


	/**
	 * Inject uploaded file parts into the instance.
	 * 
	 * @param instance
	 *          Bean
	 * @param multi
	 *          MultipartParser instance handling the request
	 */
	public void injectUploads(Object instance, MultipartParser multi) {
		try {
			for (Part p : multi.getAllParts()) {
				if (p.isFile() && (p.getSize() > 0)) {
					injectPart(p, instance, p.getName());
				}
			}
		}
		catch (IOException | UploadFormatException ex) {
			LOG.error("Upload error", ex);
		}
	}


	private void injectPart(Part part, Object bean, String property) {
		BeanExpression exp = BeanExpressionParser.parse(property);

		try {
			Type targetType = exp.getTypeIn(bean.getClass());
			Object injectionValue = null;

			if (File.class.equals(targetType)) {
				try {
					injectionValue = part.getAsFile();
				}
				catch (Exception ex) {
					Log.get(UploadInjector.class)
							.warn("Cannot provide upload as temporary file", ex);
				}
			} else if (FileUpload.class.equals(targetType)) {
				injectionValue = new FileUploadPartWrapper(part);
			}

			if (injectionValue != null) {
				exp.writeTo(bean, injectionValue);
			}
		}
		catch (Exception ex) {
			LOG.warn("Caught exception for injecting upload into \"" + part.getName()
					+ "\" of " + bean.getClass().getName(), ex);
		}
	}
}
