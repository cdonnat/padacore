package org.padacore.core.utils;

import org.eclipse.core.runtime.jobs.Job;

public abstract class PadacoreJob extends Job {

	public PadacoreJob(String name) {
		super(name);
	}

	public static final String PADACORE_JOB_FAMILY = "padacoreJobFamily";

	@Override
	public boolean belongsTo(Object family) {
		return family.equals(PADACORE_JOB_FAMILY);
	}

}
