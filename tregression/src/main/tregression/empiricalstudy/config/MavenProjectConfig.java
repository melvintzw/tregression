package tregression.empiricalstudy.config;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.SystemUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;


public class MavenProjectConfig extends ProjectConfig {

	public final static String M2AFFIX = ".m2" + File.separator + "repository";

	public MavenProjectConfig(String srcTestFolder, String srcSourceFolder, String bytecodeTestFolder,
			String bytecodeSourceFolder, String buildFolder, String projectName, String regressionID) {
		super(srcTestFolder, srcSourceFolder, bytecodeTestFolder, bytecodeSourceFolder, buildFolder, projectName,
				regressionID);
	}

	public static boolean check(String path) {

		File f = new File(path);
		if (f.exists() && f.isDirectory()) {
			for (String file : f.list()) {
				if (file.toString().equals("pom.xml")) {
					return true;
				}
			}
		}

		return false;
	}


	public static ProjectConfig getConfig(String projectName, String regressionID) {
		return new MavenProjectConfig("src"+File.separator+"test"+File.separator+"java", 
				"src"+File.separator+"main"+File.separator+"java", 
				"target"+File.separator+"test-classes", 
				"target"+File.separator+"classes", 
				"target", 
				projectName, 
				regressionID);
	}
	
	public static List<String> getMavenDependencies(String path){
		String pomPath = path + File.separator + "pom.xml";
		File pomFile = new File(pomPath);

		try {
			List<String> dependencies = readAllDependency(pomFile);
			
			return dependencies;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ArrayList<String>();
	}

	@SuppressWarnings("unchecked")
	public static List<String> readAllDependency(File pom) throws Exception {
		MavenXpp3Reader mavenReader = new MavenXpp3Reader();
		Model pomModel = mavenReader.read(new FileReader(pom));
		List<Dependency> dependencies = pomModel.getDependencies();
		List<String> result = new ArrayList<>();
		String usrHomePath = getUserHomePath();
		for (Dependency dependency : dependencies) {
			StringBuilder sb = new StringBuilder(usrHomePath);
			sb.append(File.separator).append(M2AFFIX).append(File.separator)
					.append(dependency.getGroupId().replace(".", File.separator)).append(File.separator)
					.append(dependency.getArtifactId()).append(File.separator).append(dependency.getVersion())
					.append(File.separator).append(dependency.getArtifactId()).append("-")
					.append(dependency.getVersion()).append(".").append(dependency.getType());
			result.add(sb.toString());
		}
		return result;
	}

	private static String getUserHomePath() {
		return SystemUtils.getUserHome().toString();
	}

}
