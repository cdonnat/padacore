package org.padacore;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class AdaProjectNature implements IProjectNature {

	public static final String NATURE_ID = "org.padacore.adaProjectNature";
	public static final String ADA_BUILDER_ID = "org.padacore.builder.AdaProjectBuilder";
	
	private IProject project;

	@Override
	public void configure() throws CoreException {
		this.configureAdaBuilderForProject();
	}

	/**
	 * Configures the Ada builder for the project (adds Ada builder to the project if it is not already present, does nothing otherwise).
	 * 
	 * @throws CoreException
	 *             if the builder cannot be configured for the project
	 */
	private void configureAdaBuilderForProject() throws CoreException {

		IProjectDescription projectDesc = this.getProjectDescription();

		boolean adaBuilderAlreadyConfigured = this
				.isAdaBuilderConfiguredForProject(projectDesc);

		if (!adaBuilderAlreadyConfigured) {
			this.addAdaBuilderToProject(projectDesc);
		}
	}

	/**
	 * Adds the Ada builder to the project.
	 * 
	 * @param projectDesc
	 *            the project description to which Ada builder is added
	 * @throws CoreException
	 *             if the Ada builder cannot be added to project
	 */
	private void addAdaBuilderToProject(IProjectDescription projectDesc)
			throws CoreException {
		
		assert(!this.isAdaBuilderConfiguredForProject(projectDesc));

		ICommand adaBuilderCmd = createAdaBuilderCommand(projectDesc);

		ICommand[] builderCmdsForProject = this
				.addAdaBuilderCmdToProjectBuilderCmds(projectDesc,
						adaBuilderCmd);

		this.updateListOfBuilderCmds(projectDesc, builderCmdsForProject);
		
		assert(this.isAdaBuilderConfiguredForProject(projectDesc));
	}

	/**
	 * Creates the Ada builder command.
	 * 
	 * @param projectDesc
	 *            the description of the project for which the Ada builder
	 *            command is created
	 * @return
	 */
	private ICommand createAdaBuilderCommand(IProjectDescription projectDesc) {
		ICommand adaBuilderCmd = projectDesc.newCommand();
		adaBuilderCmd.setBuilderName(ADA_BUILDER_ID);

		return adaBuilderCmd;
	}

	/**
	 * Adds the Ada builder command to the list of builder commands of the
	 * project (in first place).
	 * 
	 * @param projectDesc
	 *            the description of the project to which Ada builder command is
	 *            added
	 * @param adaBuildCmd
	 *            the Ada builder command
	 * @return the new list of builder commands for the project which contains
	 *         all the current builder commands plus the Ada builder command in first place
	 */
	private ICommand[] addAdaBuilderCmdToProjectBuilderCmds(
			IProjectDescription projectDesc, ICommand adaBuildCmd) {
		ICommand[] currentBuildCmds = projectDesc.getBuildSpec();

		ICommand[] newBuildCmds = new ICommand[currentBuildCmds.length + 1];
		System.arraycopy(currentBuildCmds, 0, newBuildCmds, 1,
				currentBuildCmds.length);
		newBuildCmds[0] = adaBuildCmd;
		
		assert(newBuildCmds[0].getBuilderName().equals(ADA_BUILDER_ID));

		return newBuildCmds;
	}

	/**
	 * Retrieves the project description.
	 * 
	 * @return description the project description.
	 * @throws CoreException
	 *             if project description cannot be retrieved
	 */
	private IProjectDescription getProjectDescription() throws CoreException {

		return this.getProject().getDescription();
	}

	/**
	 * Checks if the Ada builder is already configured for this project.
	 * 
	 * @return true if the Ada builder is already configured for the project,
	 *         false otherwise.
	 * @throws CoreException
	 *             if the list of builder commands for the project cannot be
	 *             retrieved
	 */
	private boolean isAdaBuilderConfiguredForProject(
			IProjectDescription projectDesc) throws CoreException {
		ICommand[] buildCommands = projectDesc.getBuildSpec();
		int currentCmd = 0;
		boolean adaBuilderAlreadyConfigured = false;

		
		while(!adaBuilderAlreadyConfigured && currentCmd < buildCommands.length) {
			adaBuilderAlreadyConfigured = buildCommands[currentCmd].getBuilderName().equals(ADA_BUILDER_ID);
			currentCmd ++;
		}

		return adaBuilderAlreadyConfigured;
	}
	
	/**
	 * Deconfigures the Ada builder for project (removes the Ada builder from project if it is already present, does nothing otherwise).
	 * @throws CoreException if the Ada builder cannot be "deconfigured" for the project.
	 */
	private void deconfigureAdaBuilderFromProject() throws CoreException {
		IProjectDescription projectDesc = this.getProjectDescription();
		
		boolean adaBuilderAlreadyConfigured = this
				.isAdaBuilderConfiguredForProject(projectDesc);
		
		if(adaBuilderAlreadyConfigured) {
			this.removeAdaBuilderFromProject(projectDesc);
		}
	}
	
	/**
	 * Removes the Ada builder command from the list of builder commands of the project.
	 *  
	 * @param projectDesc the description of the project from which Ada builder command is removed.
	 * @return the new list of builder commands for the project (from which Ada builder command has been removed).
	 */
	private ICommand[] removeAdaBuilerCmdFromProjectBuilderCmds(IProjectDescription projectDesc) {
		
		ICommand[] currentBuildCmds = projectDesc.getBuildSpec();
		ICommand[] newBuildCmds = new ICommand[currentBuildCmds.length - 1];
		int adaBuilderIndex = currentBuildCmds.length;
		
		for (int currentCmd = 0; currentCmd < newBuildCmds.length; currentCmd++) {
			if(currentBuildCmds[currentCmd].getBuilderName().equals(ADA_BUILDER_ID)) {
				adaBuilderIndex = currentCmd;
			}
			
			if(currentCmd > adaBuilderIndex) {
				newBuildCmds[currentCmd - 1] = currentBuildCmds[currentCmd];
			} else if(currentCmd < adaBuilderIndex) {
				newBuildCmds[currentCmd] = currentBuildCmds[currentCmd];
			}
		}
		
		return newBuildCmds;
	}

	/**
	 * Removes the Ada builder from the project.
	 * 
	 * @param projectDesc
	 *            the project description from which Ada builder is removed
	 * @throws CoreException
	 *             if the Ada builder cannot be removed from project
	 */
	private void removeAdaBuilderFromProject(IProjectDescription projectDesc) throws CoreException {

		assert(this.isAdaBuilderConfiguredForProject(projectDesc));
		
		ICommand[] builderCmdsForProject = this.removeAdaBuilerCmdFromProjectBuilderCmds(projectDesc);

		this.updateListOfBuilderCmds(projectDesc, builderCmdsForProject);
		
		assert(!this.isAdaBuilderConfiguredForProject(projectDesc));
	}
	
	/**
	 * Updates the list of builder commands for the project.
	 * 
	 * @param projectDesc desription of the project for which builder commands are updated.
	 * @param newListOfBuilderCmds the new list of builder commands for the project.
	 * @throws CoreException if the list of builder commands cannot be updated.
	 */
	private void updateListOfBuilderCmds(IProjectDescription projectDesc, ICommand[] newListOfBuilderCmds) throws CoreException {
		projectDesc.setBuildSpec(newListOfBuilderCmds);
		this.getProject().setDescription(projectDesc, null);
	}

	@Override
	public void deconfigure() throws CoreException {
		this.deconfigureAdaBuilderFromProject();
	}

	@Override
	public IProject getProject() {
		return this.project;
	}

	@Override
	public void setProject(IProject project) {
		this.project = project;
	}

}
